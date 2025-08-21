import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_osm_plugin/flutter_osm_plugin.dart';
import 'package:geolocator/geolocator.dart';
import 'package:mekanat/models/church.dart';
import 'package:mekanat/providers/church_provider.dart';
import 'package:mekanat/screens/church_detail_screen.dart';
import 'package:mekanat/screens/suggest_screen.dart'; // Add this import
import 'package:mekanat/widgets/custom_search_bar.dart';
import 'package:mekanat/widgets/glass_card.dart';

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  late MapController _mapController;
  Position? _currentPosition;
  List<Church> _filteredChurches = [];
  List<GeoPoint> _currentMarkers = []; // Track current markers for removal

  @override
  void initState() {
    super.initState();
    _mapController = MapController.withUserPosition(
      trackUserLocation: const UserTrackingOption(
        enableTracking: true,
        unFollowUser: false,
      ),
    );
    _getLocation();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      Provider.of<ChurchProvider>(context, listen: false).fetchChurches();
    });
  }

  Future<void> _getLocation() async {
    bool serviceEnabled = await Geolocator.isLocationServiceEnabled();
    if (!serviceEnabled) return;

    LocationPermission permission = await Geolocator.checkPermission();
    if (permission == LocationPermission.denied) {
      permission = await Geolocator.requestPermission();
      if (permission == LocationPermission.denied) return;
    }

    _currentPosition = await Geolocator.getCurrentPosition();
    await _mapController.changeLocation(GeoPoint(
        latitude: _currentPosition!.latitude,
        longitude: _currentPosition!.longitude));
  }

  void _search(String query) {
    final churches =
        Provider.of<ChurchProvider>(context, listen: false).churches;
    setState(() {
      _filteredChurches = churches
          .where((church) =>
              church.name.toLowerCase().contains(query.toLowerCase()))
          .toList();
    });
  }

  Future<void> _showNearby() async {
    if (_currentPosition == null) return;
    final nearby = await Provider.of<ChurchProvider>(context, listen: false)
        .getNearbyChurches(_currentPosition!, 50); // 50km
    _addMarkers(nearby);
  }

  Future<void> _addMarkers(List<Church> churches) async {
    // Remove existing markers
    for (var marker in _currentMarkers) {
      await _mapController.removeMarker(marker);
    }
    _currentMarkers.clear();

    // Add new markers
    for (var church in churches) {
      final point = GeoPoint(latitude: church.lat, longitude: church.lng);
      await _mapController.addMarker(point);
      _currentMarkers.add(point);
    }
  }

  @override
  Widget build(BuildContext context) {
    final churches = Provider.of<ChurchProvider>(context).churches;
    _filteredChurches =
        _filteredChurches.isEmpty ? churches : _filteredChurches;

    return Scaffold(
      appBar: AppBar(
        title: Text('Mekanat', style: TextStyle(color: Colors.white)),
        actions: [
          IconButton(
              icon: Icon(Icons.add),
              onPressed: () => Navigator.push(
                  context, MaterialPageRoute(builder: (_) => SuggestScreen()))),
          IconButton(icon: Icon(Icons.near_me), onPressed: _showNearby),
        ],
      ),
      body: Stack(
        children: [
          OSMFlutter(
            controller: _mapController,
            osmOption: const OSMOption(
              // Required parameter
              zoomOption:
                  ZoomOption(initZoom: 8, minZoomLevel: 3, maxZoomLevel: 19),
              showDefaultInfoWindow: true,
            ),
            onGeoPointClicked: (geoPoint) {
              // Find church at point (approximate match due to floating point)
              final church = churches.firstWhere(
                (c) =>
                    (c.lat - geoPoint.latitude).abs() < 0.0001 &&
                    (c.lng - geoPoint.longitude).abs() < 0.0001,
                orElse: () => Church(id: '', name: '', lat: 0, lng: 0),
              );
              if (church.id.isNotEmpty) {
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (_) => ChurchDetailScreen(church: church)));
              }
            },
          ),
          Positioned(
            top: 20,
            left: 20,
            right: 20,
            child: CustomSearchBar(onSearch: _search),
          ),
          if (_filteredChurches.isNotEmpty)
            Positioned(
              bottom: 20,
              left: 20,
              right: 20,
              child: GlassCard(
                child: ListView.builder(
                  shrinkWrap: true,
                  itemCount: _filteredChurches.length > 5
                      ? 5
                      : _filteredChurches.length, // Show top 5
                  itemBuilder: (ctx, i) => ListTile(
                    title: Text(_filteredChurches[i].name),
                    onTap: () => Navigator.push(
                        context,
                        MaterialPageRoute(
                            builder: (_) => ChurchDetailScreen(
                                church: _filteredChurches[i]))),
                  ),
                ),
              ),
            ),
        ],
      ),
    );
  }
}
