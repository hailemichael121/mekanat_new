import 'dart:io';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_storage/firebase_storage.dart';
import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:mekanat/models/church.dart';
import 'package:geolocator/geolocator.dart';

class ChurchProvider with ChangeNotifier {
  List<Church> _churches = [];
  List<Church> get churches => _churches;

  final FirebaseFirestore _db = FirebaseFirestore.instance;

  Future<void> fetchChurches() async {
    try {
      final snapshot = await _db.collection('churches').get();
      _churches = snapshot.docs.map((doc) => Church.fromFirestore(doc)).toList();
      notifyListeners();
    } catch (e) {
      debugPrint('Fetch error: $e');
    }
  }

  Future<void> suggestNewChurch(Church church) async {
    try {
      await _db.collection('churches').add(church.toMap());
      await fetchChurches();
    } catch (e) {
      debugPrint('Suggest error: $e');
    }
  }

  Future<void> suggestEdit(String id, Map<String, dynamic> updates) async {
    try {
      await _db.collection('churches').doc(id).update(updates);
      await fetchChurches();
    } catch (e) {
      debugPrint('Edit error: $e');
    }
  }

  Future<String?> uploadPhoto(String churchId, XFile image) async {
    try {
      final ref = FirebaseStorage.instance.ref('church_photos/$churchId/${DateTime.now().millisecondsSinceEpoch}.jpg');
      await ref.putFile(File(image.path));
      final url = await ref.getDownloadURL();
      await _db.collection('churches').doc(churchId).update({
        'photos': FieldValue.arrayUnion([url])
      });
      await fetchChurches();
      return url;
    } catch (e) {
      debugPrint('Upload error: $e');
      return null;
    }
  }

  Future<List<Church>> getNearbyChurches(Position position, double radiusKm) async {
    // Simple distance filter (for demo; optimize with GeoFirestore if needed)
    return _churches.where((church) {
      final distance = Geolocator.distanceBetween(position.latitude, position.longitude, church.lat, church.lng) / 1000;
      return distance <= radiusKm;
    }).toList();
  }
}