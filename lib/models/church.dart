import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:intl/intl.dart';

class Church {
  final String id;
  final String name;
  final double lat;
  final double lng;
  final String story;
  final List<String> tabots;  // List of tabots (e.g., ['Tabot of Mary', 'Tabot of St. George'])
  final List<Event> events;  // Festivals/events
  final List<String> photos;  // URLs of uploaded photos

  Church({
    required this.id,
    required this.name,
    required this.lat,
    required this.lng,
    this.story = '',
    this.tabots = const [],
    this.events = const [],
    this.photos = const [],
  });

  factory Church.fromFirestore(DocumentSnapshot doc) {
    Map data = doc.data() as Map;
    return Church(
      id: doc.id,
      name: data['name'] ?? '',
      lat: data['lat'] ?? 0.0,
      lng: data['lng'] ?? 0.0,
      story: data['story'] ?? '',
      tabots: List<String>.from(data['tabots'] ?? []),
      events: (data['events'] as List? ?? []).map((e) => Event.fromMap(e)).toList(),
      photos: List<String>.from(data['photos'] ?? []),
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'name': name,
      'lat': lat,
      'lng': lng,
      'story': story,
      'tabots': tabots,
      'events': events.map((e) => e.toMap()).toList(),
      'photos': photos,
    };
  }
}

class Event {
  final String name;
  final DateTime date;
  final String description;

  Event({required this.name, required this.date, this.description = ''});

  factory Event.fromMap(Map map) {
    return Event(
      name: map['name'] ?? '',
      date: DateTime.parse(map['date'] ?? DateTime.now().toIso8601String()),
      description: map['description'] ?? '',
    );
  }

  Map<String, dynamic> toMap() {
    return {
      'name': name,
      'date': date.toIso8601String(),
      'description': description,
    };
  }

  String formattedDate() => DateFormat('MMMM dd, yyyy').format(date);
}