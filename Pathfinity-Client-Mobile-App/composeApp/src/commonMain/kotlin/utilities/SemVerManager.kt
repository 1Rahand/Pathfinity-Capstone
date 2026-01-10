/**
 * Object that provides semantic versioning (SemVer) functionality
 * including version comparison according to SemVer rules
 */
object SemVerManager {
   /**
    * Class representing a parsed semantic version
    */
   data class SemVersion(
      val major: Int,
      val minor: Int,
      val patch: Int,
      val preRelease: String?, // Pre-release version (e.g., "alpha.1")
      val buildMetadata: String? // Build metadata (e.g., "build.123")
   )

   /**
    * Compares two semantic version strings according to SemVer rules
    *
    * Format: MAJOR.MINOR.PATCH[-PRERELEASE][+BUILD]
    *
    * @param version1 First version string
    * @param version2 Second version string
    * @return -1 if version1 < version2, 0 if equal, 1 if version1 > version2
    */
   fun compareVersions(version1: String, version2: String): Int {
      val v1Components = parseVersion(version1)
      val v2Components = parseVersion(version2)

      // Compare major version
      val majorComparison = v1Components.major.compareTo(v2Components.major)
      if (majorComparison != 0) return majorComparison

      // Compare minor version
      val minorComparison = v1Components.minor.compareTo(v2Components.minor)
      if (minorComparison != 0) return minorComparison

      // Compare patch version
      val patchComparison = v1Components.patch.compareTo(v2Components.patch)
      if (patchComparison != 0) return patchComparison

      // Compare pre-release versions (if they exist)
      if (v1Components.preRelease != null && v2Components.preRelease == null) {
         return -1 // Pre-release version is less than release version
      } else if (v1Components.preRelease == null && v2Components.preRelease != null) {
         return 1 // Release version is greater than pre-release version
      } else if (v1Components.preRelease != null && v2Components.preRelease != null) {
         // Compare pre-release identifiers
         return comparePreRelease(v1Components.preRelease, v2Components.preRelease)
      }

      // Versions are equal (build metadata doesn't affect precedence)
      return 0
   }

   /**
    * Parse a version string into a SemVersion object
    */
   private fun parseVersion(version: String): SemVersion {
      // Extract build metadata if present
      val buildIndex = version.indexOf('+')
      val versionWithoutBuild = if (buildIndex >= 0) version.substring(0, buildIndex) else version
      val buildMetadata = if (buildIndex >= 0) version.substring(buildIndex + 1) else null

      // Extract pre-release version if present
      val preReleaseIndex = versionWithoutBuild.indexOf('-')
      val versionNumbersOnly = if (preReleaseIndex >= 0) versionWithoutBuild.substring(0, preReleaseIndex) else versionWithoutBuild
      val preRelease = if (preReleaseIndex >= 0) versionWithoutBuild.substring(preReleaseIndex + 1) else null

      // Parse version numbers
      val versionParts = versionNumbersOnly.split('.')
      val major = versionParts.getOrNull(0)?.toIntOrNull() ?: 0
      val minor = versionParts.getOrNull(1)?.toIntOrNull() ?: 0
      val patch = versionParts.getOrNull(2)?.toIntOrNull() ?: 0

      return SemVersion(major, minor, patch, preRelease, buildMetadata)
   }

   /**
    * Compare pre-release versions according to SemVer rules
    */
   private fun comparePreRelease(preRelease1: String, preRelease2: String): Int {
      val identifiers1 = preRelease1.split('.')
      val identifiers2 = preRelease2.split('.')

      // Compare each identifier in sequence
      for (i in 0 until minOf(identifiers1.size, identifiers2.size)) {
         val id1 = identifiers1[i]
         val id2 = identifiers2[i]

         // If both are numeric, compare as numbers
         val num1 = id1.toIntOrNull()
         val num2 = id2.toIntOrNull()

         if (num1 != null && num2 != null) {
            val numComparison = num1.compareTo(num2)
            if (numComparison != 0) return numComparison
         } else if (num1 != null && num2 == null) {
            // Numeric identifiers have lower precedence
            return -1
         } else if (num1 == null && num2 != null) {
            // Numeric identifiers have lower precedence
            return 1
         } else {
            // Compare as strings
            val strComparison = id1.compareTo(id2)
            if (strComparison != 0) return strComparison
         }
      }

      // If all comparable identifiers are equal, the one with fewer identifiers comes first
      return identifiers1.size.compareTo(identifiers2.size)
   }
}