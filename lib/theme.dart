import 'package:flutter/material.dart';

// Ethiopian Orthodox-inspired palette: Rock-hewn earthy tones with vibrant accents
const primaryColor = Color(0xFF8B4513); // Brown (rock-hewn)
const secondaryColor = Color(0xFFD4AF37); // Gold (was accentColor)
const tertiaryColor = Color(0xFF078930); // Green
const backgroundColor = Color(0xFFF5F5F5); // Light grayish
const textColor = Color(0xFF333333);

ThemeData appTheme() {
  return ThemeData(
    primaryColor: primaryColor,
    scaffoldBackgroundColor: backgroundColor,
    colorScheme: ColorScheme.fromSeed(
      seedColor: primaryColor,
      primary: primaryColor,
      secondary: secondaryColor,
      tertiary: tertiaryColor,
    ),
    appBarTheme: const AppBarTheme(
      color: primaryColor,
      elevation: 0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.vertical(bottom: Radius.circular(30)),
      ),
    ),
    cardTheme: CardThemeData(
      // Changed from CardTheme to CardThemeData
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(20)),
      elevation: 0, // Use glass instead
    ),
    textTheme: const TextTheme(
      headlineMedium: TextStyle(
        color: textColor,
        fontWeight: FontWeight.bold,
        fontSize: 24,
      ),
      bodyMedium: TextStyle(color: textColor),
    ),
    // Curvy buttons
    elevatedButtonTheme: ElevatedButtonThemeData(
      style: ElevatedButton.styleFrom(
        shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(30)),
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 15),
      ),
    ),
  );
}
