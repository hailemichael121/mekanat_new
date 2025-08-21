import 'dart:async';
import 'dart:math';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:firebase_core/firebase_core.dart';
import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'package:mekanat/providers/auth_provider.dart';
import 'package:mekanat/providers/church_provider.dart';
import 'package:mekanat/screens/home_screen.dart';
// import 'package:mekanat/screens/bible_screen.dart';
// import 'package:mekanat/screens/prayer_screen.dart';
// import 'package:mekanat/screens/events_screen.dart';
// import 'package:mekanat/screens/profile_screen.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  await Firebase.initializeApp();
  runApp(MekanatApp());
}

class MekanatApp extends StatelessWidget {
  const MekanatApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        ChangeNotifierProvider(create: (_) => AuthProvider()),
        ChangeNotifierProvider(create: (_) => ChurchProvider()),
      ],
      child: MaterialApp(
        title: 'Mekanat',
        theme: ThemeData(
          primaryColor: const Color(0xFFD4A017),
          colorScheme: ColorScheme.fromSwatch().copyWith(
            primary: const Color(0xFFD4A017),
            secondary: const Color(0xFFB71C1C),
            surface: Colors.white,
            background: const Color(0xFFF5F5F5),
          ),
          scaffoldBackgroundColor: const Color(0xFFF5F5F5),
          appBarTheme: AppBarTheme(
            backgroundColor: const Color(0xFFD4A017),
            foregroundColor: Colors.white,
            elevation: 4,
            shadowColor: Colors.black.withOpacity(0.2),
          ),
          textTheme: const TextTheme(
            bodyMedium: TextStyle(fontFamily: 'Roboto'),
            headlineMedium: TextStyle(
                fontFamily: 'Roboto',
                fontSize: 18,
                fontWeight: FontWeight.bold),
            headlineLarge: TextStyle(
                fontFamily: 'Roboto',
                fontSize: 32,
                fontWeight: FontWeight.bold,
                color: Colors.white),
          ),
        ),
        home: const SplashScreen(),
      ),
    );
  }
}

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  _SplashScreenState createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen>
    with SingleTickerProviderStateMixin {
  late AnimationController _controller;
  late Animation<double> _scaleAnimation;
  String _displayText = '';
  final String _fullText = 'Mekanat';
  int _textIndex = 0;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(
      duration: const Duration(seconds: 2),
      vsync: this,
    )..repeat();
    _scaleAnimation = Tween<double>(begin: 1.0, end: 1.1).animate(
      CurvedAnimation(parent: _controller, curve: Curves.easeInOut),
    );

    // Typing animation
    Timer.periodic(const Duration(milliseconds: 300), (timer) {
      if (_textIndex < _fullText.length) {
        setState(() {
          _displayText = _fullText.substring(0, ++_textIndex);
        });
      } else {
        timer.cancel();
      }
    });

    // Navigate to MainScreen after 5 seconds
    Timer(const Duration(seconds: 5), () {
      Navigator.pushReplacement(
          context, MaterialPageRoute(builder: (_) => const MainScreen()));
    });
  }

  @override
  void dispose() {
    _controller.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: const Color(0xFFD4A017),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            AnimatedBuilder(
              animation: _scaleAnimation,
              builder: (context, child) {
                return Transform.scale(
                  scale: _scaleAnimation.value,
                  child: const Icon(
                    Icons.church,
                    size: 150,
                    color: Colors.white,
                  ),
                );
              },
            ),
            const SizedBox(height: 20),
            Text(
              _displayText,
              style: Theme.of(context).textTheme.headlineLarge,
            ),
            const SizedBox(height: 20),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: List.generate(3, (index) {
                return AnimatedBuilder(
                  animation: _controller,
                  builder: (context, child) {
                    double angle =
                        2 * pi * index / 3 + _controller.value * 2 * pi;
                    return Transform.translate(
                      offset: Offset(15 * cos(angle), 15 * sin(angle)),
                      child: Container(
                        width: 10,
                        height: 10,
                        margin: const EdgeInsets.symmetric(horizontal: 5),
                        decoration: BoxDecoration(
                          shape: BoxShape.circle,
                          color: Colors.white,
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withOpacity(0.2),
                              blurRadius: 5,
                              offset: const Offset(0, 2),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                );
              }),
            ),
          ],
        ),
      ),
    );
  }
}

class LoadingScreen extends StatelessWidget {
  const LoadingScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.black.withOpacity(0.5),
      child: Center(
        child: ClipRRect(
          borderRadius: BorderRadius.circular(20),
          child: BackdropFilter(
            filter: ImageFilter.blur(sigmaX: 10, sigmaY: 10),
            child: Container(
              padding: const EdgeInsets.all(20),
              decoration: BoxDecoration(
                color: const Color(0xFFD4A017).withOpacity(0.2),
                borderRadius: BorderRadius.circular(20),
                border: Border.all(color: Colors.white.withOpacity(0.2)),
              ),
              child: const Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(
                    'Loading...',
                    style: TextStyle(
                        color: Colors.white,
                        fontSize: 18,
                        fontWeight: FontWeight.bold),
                  ),
                  SizedBox(height: 10),
                  CircularProgressIndicator(
                    valueColor: AlwaysStoppedAnimation<Color>(Colors.white),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}

class MainScreen extends StatefulWidget {
  const MainScreen({super.key});

  @override
  _MainScreenState createState() => _MainScreenState();
}

class _MainScreenState extends State<MainScreen>
    with SingleTickerProviderStateMixin {
  int _selectedIndex = 0;
  late AnimationController _animationController;
  late Animation<double> _scaleAnimation;

  final List<Widget> _screens = [
    HomeScreen(),
    const BibleScreen(),
    const PrayerScreen(),
    const EventsScreen(),
    const ProfileScreen(),
    const Center(child: Text('Contact Us', style: TextStyle(fontSize: 16))),
    const Center(child: Text('Settings', style: TextStyle(fontSize: 16))),
    const Center(child: Text('Help & FAQ', style: TextStyle(fontSize: 16))),
  ];

  final List<Map<String, dynamic>> _navItems = [
    {'icon': Icons.map, 'label': 'Map'},
    {'icon': Icons.book, 'label': 'Bible'},
    {'icon': Icons.nights_stay, 'label': 'Prayer'}, // Changed from prayer_times
    {'icon': Icons.event, 'label': 'Events'},
    {'icon': Icons.person, 'label': 'Profile'},
  ];

  final List<Map<String, dynamic>> _drawerItems = [
    {'title': 'Map', 'icon': Icons.map, 'index': 0},
    {'title': 'Bible', 'icon': Icons.book, 'index': 1},
    {
      'title': 'Prayer',
      'icon': Icons.nights_stay,
      'index': 2
    }, // Changed from prayer_times
    {'title': 'Events', 'icon': Icons.event, 'index': 3},
    {'title': 'Profile', 'icon': Icons.person, 'index': 4},
    {'title': 'Contact Us', 'icon': Icons.mail, 'index': 5},
    {'title': 'Settings', 'icon': Icons.settings, 'index': 6},
    {'title': 'Help & FAQ', 'icon': Icons.help, 'index': 7},
  ];

  @override
  void initState() {
    super.initState();
    _animationController = AnimationController(
      duration: const Duration(milliseconds: 300),
      vsync: this,
    );
    _scaleAnimation = Tween<double>(begin: 1.0, end: 1.2).animate(
      CurvedAnimation(parent: _animationController, curve: Curves.easeInOut),
    );
  }

  @override
  void dispose() {
    _animationController.dispose();
    super.dispose();
  }

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
    _animationController.forward().then((_) => _animationController.reverse());
  }

  @override
  Widget build(BuildContext context) {
    final Size screenSize = MediaQuery.of(context).size;
    final double screenWidth = screenSize.width;

    return Scaffold(
      appBar: AppBar(
        backgroundColor: const Color(0xFFD4A017),
        leading: Builder(
          builder: (context) => IconButton(
            icon: const Icon(
              Icons.menu,
              color: Colors.white,
              size: 28,
            ),
            onPressed: () => Scaffold.of(context).openDrawer(),
          ),
        ),
        title: const Text('Mekanat', style: TextStyle(color: Colors.white)),
      ),
      body: _screens[_selectedIndex],
      drawer: Drawer(
        width: screenWidth * 0.6,
        backgroundColor: const Color(0xFFD4A017).withOpacity(0.9),
        child: ListView(
          padding: EdgeInsets.zero,
          children: [
            DrawerHeader(
              decoration: BoxDecoration(
                color: const Color(0xFFD4A017),
                boxShadow: [
                  BoxShadow(
                    color: Colors.black.withOpacity(0.2),
                    blurRadius: 10,
                    offset: const Offset(0, 4),
                  ),
                ],
              ),
              child: Stack(
                children: [
                  Positioned(
                    top: 8,
                    right: 8,
                    child: IconButton(
                      icon: const Icon(Icons.close,
                          color: Colors.white, size: 30),
                      onPressed: () => Navigator.pop(context),
                    ),
                  ),
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    children: [
                      Stack(
                        alignment: Alignment.center,
                        children: [
                          Container(
                            width: 70,
                            height: 70,
                            decoration: BoxDecoration(
                              shape: BoxShape.circle,
                              color: Colors.white.withOpacity(0.2),
                              boxShadow: [
                                BoxShadow(
                                  color: Colors.black.withOpacity(0.2),
                                  blurRadius: 8,
                                  offset: const Offset(0, 2),
                                ),
                              ],
                            ),
                          ),
                          const CircleAvatar(
                            backgroundColor: Colors.white,
                            child: Icon(Icons.person,
                                size: 40, color: Color(0xFFD4A017)),
                            radius: 30,
                          ),
                        ],
                      ),
                      const SizedBox(width: 16),
                      Flexible(
                        child: Column(
                          mainAxisAlignment: MainAxisAlignment.center,
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Text(
                              'Aberash Abera',
                              style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                                color: Colors.white,
                              ),
                            ),
                            Text(
                              'Orthodox Christian',
                              style: TextStyle(
                                fontSize: 14,
                                color: Colors.white70,
                              ),
                            ),
                          ],
                        ),
                      ),
                    ],
                  ),
                ],
              ),
            ),
            ..._drawerItems.map((item) => AppDrawerButton(
                  title: item['title'],
                  isSelected: _selectedIndex == item['index'],
                  leadingIcon: item['icon'],
                  onItemTapped: () {
                    _onItemTapped(item['index']);
                    Navigator.pop(context);
                  },
                )),
          ],
        ),
      ),
      bottomNavigationBar: ClipRRect(
        borderRadius: const BorderRadius.vertical(top: Radius.circular(20)),
        child: BackdropFilter(
          filter: ImageFilter.blur(sigmaX: 10, sigmaY: 10),
          child: CurvedNavigationBar(
            index: _selectedIndex < 5 ? _selectedIndex : 0,
            backgroundColor: Colors.transparent,
            color: const Color(0xFFD4A017).withOpacity(0.8),
            buttonBackgroundColor: const Color(0xFFB71C1C),
            height: 60,
            animationDuration: const Duration(milliseconds: 500),
            animationCurve: Curves.easeInOut,
            items: _navItems
                .map((item) => AnimatedBuilder(
                      animation: _animationController,
                      builder: (context, child) {
                        bool isSelected =
                            _selectedIndex == _navItems.indexOf(item);
                        return Transform.scale(
                          scale: isSelected ? _scaleAnimation.value : 1.0,
                          child: Icon(
                            item['icon'],
                            size: isSelected ? 28 : 24,
                            color: isSelected ? Colors.white : Colors.white70,
                          ),
                        );
                      },
                    ))
                .toList(),
            onTap: _onItemTapped,
          ),
        ),
      ),
    );
  }
}

class AppDrawerButton extends StatelessWidget {
  final String title;
  final bool isSelected;
  final IconData leadingIcon;
  final VoidCallback onItemTapped;

  const AppDrawerButton({
    super.key,
    required this.title,
    required this.isSelected,
    required this.leadingIcon,
    required this.onItemTapped,
  });

  @override
  Widget build(BuildContext context) {
    return ListTile(
      leading: Icon(
        leadingIcon,
        color: isSelected ? Colors.white : Colors.white70,
        size: 24,
      ),
      title: Text(
        title,
        style: TextStyle(
          color: isSelected ? Colors.white : Colors.white70,
          fontWeight: isSelected ? FontWeight.bold : FontWeight.normal,
          fontSize: 16,
        ),
      ),
      tileColor:
          isSelected ? Colors.white.withOpacity(0.1) : Colors.transparent,
      onTap: onItemTapped,
    );
  }
}

// Placeholder screens
class BibleScreen extends StatelessWidget {
  const BibleScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Ethiopian Orthodox Bible'),
        centerTitle: true,
      ),
      body: const Center(
        child: Text(
          'Bible Screen: Access 81 books, search, bookmarks, offline audio',
          textAlign: TextAlign.center,
          style: TextStyle(fontSize: 16),
        ),
      ),
    );
  }
}

class PrayerScreen extends StatelessWidget {
  const PrayerScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Prayer'),
        centerTitle: true,
      ),
      body: const Center(
        child: Text(
          'Prayer Screen: Daily prayers, hymns, fasting guidelines',
          textAlign: TextAlign.center,
          style: TextStyle(fontSize: 16),
        ),
      ),
    );
  }
}

class EventsScreen extends StatelessWidget {
  const EventsScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Events'),
        centerTitle: true,
      ),
      body: const Center(
        child: Text(
          'Events Screen: Feasts, church events, Ethiopian Calendar',
          textAlign: TextAlign.center,
          style: TextStyle(fontSize: 16),
        ),
      ),
    );
  }
}

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Profile'),
        centerTitle: true,
      ),
      body: const Center(
        child: Text(
          'Profile Screen: Saved churches, prayers, donation history',
          textAlign: TextAlign.center,
          style: TextStyle(fontSize: 16),
        ),
      ),
    );
  }
}
