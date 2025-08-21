import 'package:flutter/material.dart';
import 'package:mekanat/widgets/glass_card.dart';  // Add this import

class CustomSearchBar extends StatelessWidget {
  final Function(String) onSearch;

  const CustomSearchBar({super.key, required this.onSearch});

  @override
  Widget build(BuildContext context) {
    return GlassCard(
      child: Padding(
        padding: const EdgeInsets.all(8.0),
        child: TextField(
          decoration: InputDecoration(
            hintText: 'Search churches...',
            border: InputBorder.none,
            icon: Icon(Icons.search, color: Theme.of(context).colorScheme.secondary),
          ),
          onChanged: onSearch,
        ),
      ),
    );
  }
}