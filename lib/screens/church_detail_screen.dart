import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:flutter_osm_plugin/flutter_osm_plugin.dart';
import 'package:geolocator/geolocator.dart';
import 'package:image_picker/image_picker.dart';
import 'package:mekanat/models/church.dart';
import 'package:mekanat/providers/church_provider.dart';
import 'package:mekanat/widgets/glass_card.dart';
import 'package:share_plus/share_plus.dart';  // Ensure share_plus in pubspec

class ChurchDetailScreen extends StatefulWidget {
  final Church church;

  const ChurchDetailScreen({super.key, required this.church});

  @override
  _ChurchDetailScreenState createState() => _ChurchDetailScreenState();
}

class _ChurchDetailScreenState extends State<ChurchDetailScreen> {
  late MapController _mapController;

  @override
  void initState() {
    super.initState();
    _mapController = MapController.withPosition(
      initPosition: GeoPoint(latitude: widget.church.lat, longitude: widget.church.lng),
    );
  }

  Future<void> _getRoute() async {
    Position current = await Geolocator.getCurrentPosition();
    await _mapController.drawRoad(
      GeoPoint(latitude: current.latitude, longitude: current.longitude),
      GeoPoint(latitude: widget.church.lat, longitude: widget.church.lng),
      roadType: RoadType.car,
    );
  }

  void _shareLocation() {
    Share.share('Check out ${widget.church.name} at https://www.openstreetmap.org/?mlat=${widget.church.lat}&mlon=${widget.church.lng}');
  }

  Future<void> _uploadPhoto() async {
    final picker = ImagePicker();
    final image = await picker.pickImage(source: ImageSource.gallery);
    if (image != null) {
      await Provider.of<ChurchProvider>(context, listen: false).uploadPhoto(widget.church.id, image);
    }
  }

  void _suggestEdit() {
    // Open dialog for edits, then call suggestEdit
    showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: Text('Suggest Edit'),
        content: Text('Implement form here for updates (e.g., story, tabots).'),
        actions: [TextButton(onPressed: () => Navigator.pop(ctx), child: Text('Submit'))],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text(widget.church.name)),
      body: SingleChildScrollView(
        child: Column(
          children: [
            SizedBox(
              height: 300,
              child: OSMFlutter(
                controller: _mapController,
                osmOption: const OSMOption(  // Required parameter
                  zoomOption: ZoomOption(initZoom: 15),
                  showDefaultInfoWindow: true,
                ),
              ),
            ),
            GlassCard(
              child: Padding(
                padding: EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text('Story: ${widget.church.story}', style: Theme.of(context).textTheme.headlineMedium),
                    SizedBox(height: 10),
                    Text('Tabots:'),
                    ...widget.church.tabots.map((t) => Text('- $t')),
                    SizedBox(height: 10),
                    Text('Events/Festivals:'),
                    ...widget.church.events.map((e) => Text('- ${e.name} on ${e.formattedDate()}: ${e.description}')),
                    SizedBox(height: 10),
                    Text('Photos:'),
                    Wrap(
                      children: widget.church.photos.map((url) => Image.network(url, width: 100, height: 100)).toList(),
                    ),
                  ],
                ),
              ),
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                ElevatedButton(onPressed: _getRoute, child: Text('Get Route')),
                ElevatedButton(onPressed: _shareLocation, child: Text('Share Location')),
                ElevatedButton(onPressed: _uploadPhoto, child: Text('Upload Photo')),
                ElevatedButton(onPressed: _suggestEdit, child: Text('Suggest Edit')),
              ],
            ),
          ],
        ),
      ),
    );
  }
}