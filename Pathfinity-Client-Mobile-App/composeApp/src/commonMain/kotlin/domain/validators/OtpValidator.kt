package domain.validators

object OtpValidator {
   fun isOtpValid(otp: String): Boolean {
      return otp.length == 6 && otp.all { it.isDigit() }
   }
}