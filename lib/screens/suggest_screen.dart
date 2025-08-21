import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:mekanat/models/church.dart';
import 'package:mekanat/providers/church_provider.dart';
import 'package:mekanat/widgets/glass_card.dart';

class SuggestScreen extends StatefulWidget {
  @override
  _SuggestScreenState createState() => _SuggestScreenState();
}

class _SuggestScreenState extends State<SuggestScreen> {
  final _formKey = GlobalKey<FormState>();
  String _name = '';
  double _lat = 0.0;
  double _lng = 0.0;
  String _story = '';

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Suggest New Church')),
      body: GlassCard(
        child: Form(
          key: _formKey,
          child: Padding(
            padding: EdgeInsets.all(16),
            child: Column(
              children: [
                TextFormField(decoration: InputDecoration(labelText: 'Name'), onSaved: (v) => _name = v ?? ''),
                TextFormField(decoration: InputDecoration(labelText: 'Latitude'), keyboardType: TextInputType.number, onSaved: (v) => _lat = double.tryParse(v ?? '0') ?? 0),
                TextFormField(decoration: InputDecoration(labelText: 'Longitude'), keyboardType: TextInputType.number, onSaved: (v) => _lng = double.tryParse(v ?? '0') ?? 0),
                TextFormField(decoration: InputDecoration(labelText: 'Story'), onSaved: (v) => _story = v ?? ''),
                ElevatedButton(
                  onPressed: () {
                    _formKey.currentState?.save();
                    final church = Church(id: '', name: _name, lat: _lat, lng: _lng, story: _story);
                    Provider.of<ChurchProvider>(context, listen: false).suggestNewChurch(church);
                    Navigator.pop(context);
                  },
                  child: Text('Submit'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}