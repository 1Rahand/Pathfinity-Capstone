import 'package:flutter/foundation.dart';

class SupabaseConfig {
  // Supabase URL and anon key
  static const String supabaseUrl = 'https://njqaahqfgymkwxkyqifq.supabase.co';
  static const String supabaseAnonKey =
      'sb_publishable_6ApSIHMTMqgOs8JSMID4zA_9a8MFuEt';

  // Debug mode flag
  static bool get isDebugMode => kDebugMode;
}
