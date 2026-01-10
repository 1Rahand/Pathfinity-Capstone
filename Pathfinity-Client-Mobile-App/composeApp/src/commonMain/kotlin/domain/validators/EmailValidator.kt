package domain.validators

object EmailValidator {
   fun isValidInstagramEmail(email: String): Boolean {
      // Basic format check using regular expression
      val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

      // Check if email matches the regex pattern
      if (!emailRegex.matches(email)) return false

      // Additional checks for length
      val parts = email.split("@")
      if (parts.size != 2) return false // Should contain exactly one '@' character

      val localPart = parts[0]
      val domainPart = parts[1]

      // Length checks
      if (localPart.length > 64 || domainPart.length > 255) return false

      // Domain must contain at least one dot ('.') after the '@' symbol
      if (!domainPart.contains(".")) return false

      // Check domain part does not start or end with a hyphen ('-') or dot ('.')
      if (domainPart.startsWith("-") || domainPart.endsWith("-") ||
         domainPart.startsWith(".") || domainPart.endsWith(".")
      ) return false

      return true
   }
}