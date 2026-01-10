package presentation

import domain.Lang
import domain.validators.PasswordValidator

object StringResources {
   fun accessToAllCourses(lang: Lang) = when (lang) {
      Lang.Krd -> "دەستگەیشتن بە هەموو کۆرسەکان"
      Lang.Eng -> "Access to all courses"
   }

   fun hdVideoQuality(lang: Lang) = when (lang) {
      Lang.Krd -> "کوالیتی ڤیدیۆی HD"
      Lang.Eng -> "HD video quality"
   }

   fun downloadableMaterials(lang: Lang) = when (lang) {
      Lang.Krd -> "داگرتنی کەرەستەکان"
      Lang.Eng -> "Downloadable materials"
   }

   fun alumni(lang: Lang) = when(lang) {
        Lang.Krd -> "دەرچوو"
        Lang.Eng -> "Alumni"
   }

   fun messages(lang: Lang) = when(lang) {
      Lang.Krd -> "پەیامەکان"
      Lang.Eng -> "Messages"
   }

   fun noMessages(lang: Lang) = when(lang) {
      Lang.Krd -> "هیچ پەیامێک نییە"
      Lang.Eng -> "No messages"
   }

   fun certificateOfCompletion(lang: Lang) = when (lang) {
      Lang.Krd -> "بڕوانامەی تەواوکردن"
      Lang.Eng -> "Certificate of completion"
   }

   fun accessOnMultipleDevices(lang: Lang) = when (lang) {
      Lang.Krd -> "دەستگەیشتن لەسەر چەند ئامێرێک"
      Lang.Eng -> "Access on multiple devices"
   }

   fun monthly(lang: Lang) = when (lang) {
      Lang.Krd -> "مانگانە"
      Lang.Eng -> "Monthly"
   }

   fun annual(lang: Lang) = when (lang) {
      Lang.Krd -> "ساڵانە"
      Lang.Eng -> "Annual"
   }

   fun monthlyAccess(lang: Lang) = when (lang) {
      Lang.Krd -> "دەستگەیشتنی مانگانە"
      Lang.Eng -> "Monthly access"
   }

   fun yearlyAccess(lang: Lang) = when (lang) {
      Lang.Krd -> "دەستگەیشتنی ساڵانە"
      Lang.Eng -> "Yearly access"
   }

   fun month(lang: Lang) = when (lang) {
      Lang.Krd -> "مانگ"
      Lang.Eng -> "month"
   }

   fun quizHistory(lang: Lang) = when (lang) {
      Lang.Krd -> "مێژووی تاقیکردنەوە"
      Lang.Eng -> "Quiz History"
   }
   fun signIn(lang: Lang) = when (lang) {
      Lang.Krd -> "چوونەژوورەوە"
      Lang.Eng -> "Sign In"
   }

   fun setEmail(lang : Lang) = when(lang) {
      Lang.Eng -> "Set Email"
      Lang.Krd -> "ئیمەیڵ دابنێ"
   }

   fun signUp(lang: Lang) = when (lang) {
      Lang.Krd -> "تۆماربوون"
      Lang.Eng -> "Sign Up"
   }

   fun totalUsers(lang : Lang) = when (lang) {
      Lang.Krd -> "بەکارهێنەر"
      Lang.Eng -> "Users"
   }

   fun totalAnsweredQuestions(lang : Lang) = when (lang) {
      Lang.Krd -> "پرسیارە وەڵامدراوەکان"
      Lang.Eng -> "Answered Questions"
   }

   fun sheekarStatistics(lang : Lang) = when (lang) {
      Lang.Krd -> "ئاماری شیکار"
      Lang.Eng -> "Sheekar Statistics"
   }

   fun verified(lang: Lang) = when (lang) {
      Lang.Krd -> "پشکنینکراو"
      Lang.Eng -> "Verified"
   }

   fun company(lang: Lang) = when (lang) {
      Lang.Krd -> "کۆمپانیا"
      Lang.Eng -> "Company"
   }

   fun chooseYourGender(lang : Lang) = when (lang){
      Lang.Eng -> "Choose your gender"
      Lang.Krd -> "ڕەگەزەکەت هەڵبژێرە"
   }


   fun seeYourStatisticsHere(lang : Lang) = when (lang) {
      Lang.Krd -> "ئاماری خۆت لێرەوە ببینە"
      Lang.Eng -> "See your statistics here"
   }

   fun adminAccountCantBeDeleted(lang : Lang) = when (lang) {
      Lang.Krd -> "هەژماری ئەدمین ناتوانرێت بسڕدرێتەوە ڕاستەوخۆ، ئەگەر دەتەوێت هەژمارەکە بسڕیتەوە تکایە پەیوەندیمان پێوە بکە"
      Lang.Eng -> "Admin account can't be deleted direcly, if you want to delete the account please contact us"
   }

   fun adminAccountEmailCantBeChanged(lang : Lang) = when (lang) {
      Lang.Krd -> "ئیمەیڵی هەژماری ئەدمین ناتوانرێت بگۆڕێت، ئەگەر دەتەوێت ئیمەیڵەکە بگۆڕیت تکایە پەیوەندیمان پێوە بکە"
      Lang.Eng -> "Admin account email can't be changed, if you want to change the email please contact us"
   }

   fun lastChance(lang : Lang) = when (lang) {
      Lang.Krd -> "کۆتا دەرفەت !"
      Lang.Eng -> "Last Chance !"
   }

   fun signUpAction(lang: Lang) = when (lang) {
      Lang.Krd -> "تۆماربکە"
      Lang.Eng -> "Sign Up"
   }

   fun pleaseEnterYourValidData(lang: Lang) = when (lang) {
      Lang.Krd -> "تکایە زانیاریەکانی تۆماربوونت بنووسە"
      Lang.Eng -> "Please enter your valid data"
   }

   fun pleaseEnterYourEmailWeWillCreateAnAccountForYouIfItIsYourFirstTime(lang: Lang) = when (lang) {
      Lang.Krd -> "تکایە ئیمەیڵەکەت بنووسە، ئەگەر بۆ یەکەم جارتە هەژمارێکت بۆ دروست دەکەین"
      Lang.Eng -> "Please enter your email, we will create an account for you if it is your first time"
   }

   fun createAnAccountIfItsYourFirstTimeOrYouCanSignInIfYouAlreadyHaveAnAccount(lang: Lang) = when (lang) {
      Lang.Krd -> "ئەگەر بۆ یەکەم جارتە هەژمارێکت دروست دەکەین یان ئەگەر هەژمارێکت هەیە دەتوانیت بچیتە ژوورەوە"
      Lang.Eng -> "Create an account if it's your first time or you can sign in if you already have an account"
   }

   fun alreadyHaveAnAccount(lang: Lang) = when (lang) {
      Lang.Krd -> "ئەگەر هەژمارێک هەیە ؟"
      Lang.Eng -> "Already have an account ?"
   }

   fun requirements(lang: Lang) = when (lang) {
      Lang.Krd -> "پێویستەکان:"
      Lang.Eng -> "Requirements:"
   }

   fun eightCharactersOrMore(lang: Lang) = when (lang) {
      Lang.Krd -> "هەشت پیت یان زیاتر"
      Lang.Eng -> "Minimum eight characters"
   }

   fun oneOrMoreUppercaseLetters(lang: Lang) = when (lang) {
      Lang.Krd -> "پیتی گەورە (A-Z)"
      Lang.Eng -> "Uppercase letter (A-Z)"
   }

   fun oneOrMoreLowercaseLetters(lang: Lang) = when (lang) {
      Lang.Krd -> "پیتی بچووك (a-z)"
      Lang.Eng -> "Lowercase letter (a-z)"
   }

   fun oneOrMoreNumbers(lang: Lang) = when (lang) {
      Lang.Krd -> "ژمارە (0-9)"
      Lang.Eng -> "Number (0-9)"
   }

   fun oneOrMoreSpecialCharacters(lang: Lang) = when (lang) {
      Lang.Krd -> "پیتی تایبەت" + " (${PasswordValidator.specialCharacters})"
      Lang.Eng -> "Special character" + " (${PasswordValidator.specialCharacters})"
   }

   fun emailInvalid(lang: Lang) = when (lang) {
      Lang.Krd -> "ئیمەیلەکەت هەڵەیە"
      Lang.Eng -> "Your email is invalid"
   }

   fun emailIsAlreadyTaken(lang: Lang) = when (lang) {
      Lang.Krd -> "ئیمەیلەکەت پێشتر بەکارهێنراوە"
      Lang.Eng -> "Your email is already taken"
   }

   fun name(lang: Lang) = when (lang) {
      Lang.Krd -> "ناو"
      Lang.Eng -> "Name"
   }

   fun firstName(lang: Lang) = when (lang) {
      Lang.Krd -> "ناوی یەکەم"
      Lang.Eng -> "First Name"
   }

   fun lastName(lang: Lang) = when (lang) {
      Lang.Krd -> "ناوی سێێەم"
      Lang.Eng -> "Last Name"
   }

   fun gender(lang: Lang) = when (lang) {
      Lang.Krd -> "ڕەگەز"
      Lang.Eng -> "Gender"
   }

   fun governorate(lang: Lang) = when (lang) {
      Lang.Krd -> "پارێزگا"
      Lang.Eng -> "Governorate"
   }

   fun enterYourName(lang: Lang) = when (lang) {
      Lang.Krd -> "ناوەکەت بنووسە"
      Lang.Eng -> "Enter your name"
   }

   fun createAccount(lang: Lang) = when (lang) {
      Lang.Krd -> "هەژمار دروست بکە"
      Lang.Eng -> "Create Account"
   }

   fun personalInformation(lang: Lang) = when (lang) {
      Lang.Krd -> "زانیاری کەسیی"
      Lang.Eng -> "Personal Information"
   }

   fun pleaseEnterYourPersonalInformation(lang: Lang) = when (lang) {
      Lang.Krd -> "تکایە زانیاری کەسییت تۆماربکە"
      Lang.Eng -> "Please enter your personal information"
   }

   fun actionsCompleted(count: Int, total: Int, lang: Lang) = when (lang) {
      Lang.Eng -> "$count of $total actions completed"
      Lang.Krd -> "$count لە $total کردارەکان تەواوکراوە"
   }

   fun verifyEmail(lang: Lang) = when (lang) {
      Lang.Krd -> "ئیمەیڵەکەت بسەلمێنە"
      Lang.Eng -> "Verify your email"
   }

   fun aSixDigitCodeHasBeenSentToYourEmail(lang: Lang, email: String) = when (lang) {
      Lang.Krd -> "کۆدێکی شەش پیتی بۆ ئیمەیڵی $email نێردرا"
      Lang.Eng -> "A six digit code has been sent to email $email"
   }

   fun sixDigitCode(lang: Lang) = when (lang) {
      Lang.Krd -> "کۆدی شەش پیت"
      Lang.Eng -> "Six digit code"
   }

   fun code(lang: Lang) = when (lang) {
      Lang.Krd -> "کۆد"
      Lang.Eng -> "Code"
   }

   fun writeSixDigitCode(lang: Lang) = when (lang) {
      Lang.Krd -> "کۆدی شەش پیت بنووسە"
      Lang.Eng -> "Write the six digit code"
   }

   fun verify(lang: Lang) = when (lang) {
      Lang.Krd -> "بسەلمێنە"
      Lang.Eng -> "Verify"
   }

   fun mustContainAtLeastOneOrMoreOfTheseCharacters(lang: Lang) = when (lang) {
      Lang.Krd -> "پێویستە بەلایەنی کەمەوە یەک یان زیاتر هەبێت لەم پیتانە:"
      Lang.Eng -> "Must contain at least one or more of these characters:"
   }

   fun otpCodeNotCorrect(lang: Lang) = when (lang) {
      Lang.Krd -> "کۆدی OTP هەڵەیە"
      Lang.Eng -> "OTP code is not correct"
   }

   fun internetErrorPleaseTryAgain(lang: Lang) = when (lang) {
      Lang.Krd -> "❌  هەڵەی ئینتەرنێت، دووبارە هەوڵ بدەوە"
      Lang.Eng -> "❌  Internet error, please try again"
   }

   fun unknownErrorPleaseTryAgain(lang: Lang) = when (lang) {
      Lang.Krd -> "❌  هەڵەیەك ڕوویدا، تکایە دووبارە هەوڵ بدەوە"
      Lang.Eng -> "❌  Unknown error, please try again"
   }

   fun generalAdministration(lang: Lang) = when (lang) {
      Lang.Krd -> "بەڕێوبەرایەتی گشتی"
      Lang.Eng -> "General Administration"
   }

   fun administration(lang: Lang) = when (lang) {
      Lang.Krd -> "بەڕێوبەرایەتی پەروەردە"
      Lang.Eng -> "Administration"
   }

   fun pleaseChooseAGeneralAdministrationFirst(lang: Lang) = when (lang) {
      Lang.Krd -> "تکایە سەرەتا بەڕێوبەرایەتی گشتی هەڵبژێرە"
      Lang.Eng -> "Please choose a general administration first"
   }

   fun pleaseChooseAnAdministrationFirst(lang: Lang) = when (lang) {
      Lang.Krd -> "تکایە سەرەتا بەڕێوبەرایەتی پەروەردە هەڵبژێرە"
      Lang.Eng -> "Please choose an administration first"
   }

   fun schools(lang: Lang) = when (lang) {
      Lang.Krd -> "قوتابخانەکان"
      Lang.Eng -> "Schools"
   }

   fun area(lang: Lang) = when (lang) {
      Lang.Krd -> "ناوچە"
      Lang.Eng -> "Area"
   }

   fun next(lang: Lang) = when (lang) {
      Lang.Krd -> "دواتر"
      Lang.Eng -> "Next"
   }

   fun forgotPassword(lang: Lang) = when (lang) {
      Lang.Krd -> "وشەی نهێنیت لەبیرکرد؟"
      Lang.Eng -> "Forgot password?"
   }

   fun pleaseEnterYourLoginInformation(lang: Lang) = when (lang) {
      Lang.Krd -> "تکایە زانیاری هەژمارت بنووسە"
      Lang.Eng -> "Please enter your login information"
   }

   fun incorrectEmailOrPassword(lang: Lang) = when (lang) {
      Lang.Krd -> "ئیمەیڵ یان وشەی نهێنیت هەڵەیە"
      Lang.Eng -> "Incorrect email or password"
   }

   fun enterYourEmail(lang: Lang) = when (lang) {
      Lang.Krd -> "ئیمەیڵەکەت بنووسە"
      Lang.Eng -> "Enter your email"
   }

   fun enterYourEmailToGetASixDigitCode(lang: Lang) = when (lang) {
      Lang.Krd -> "ئیمەیڵەکەت بنووسە بۆ پێگەیشتنی کۆدێکی شەش پیتی"
      Lang.Eng -> "Enter your email to get a six digit code"
   }

   fun requestCode(lang: Lang) = when (lang) {
      Lang.Krd -> "کۆد داوابکە"
      Lang.Eng -> "Request Code"
   }

   fun thereIsNoAccountWithThisEmail(lang: Lang) = when (lang) {
      Lang.Krd -> "هیچ هەژمارێک نییە بەم ئیمەیڵە"
      Lang.Eng -> "There is no account with this email"
   }

   fun checkEmail(lang: Lang) = when (lang) {
      Lang.Krd -> "ئیمەیڵەکەت بپشکنە"
      Lang.Eng -> "Check email"
   }

   fun subjects(currentAppLang: Lang): String = when (currentAppLang) {
      Lang.Krd -> "بابەتەکان"
      Lang.Eng -> "Subjects"
   }

   fun exploreAllSubjects(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "هەموو بابەتەکان"
         Lang.Eng -> "Explore all"
      }
   }

   fun exploreAllProgress(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "هەموو پێشکەوتنەکان"
         Lang.Eng -> "See more"
      }
   }

   fun totalQuizzes(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "تاقیکردنەوەکان"
         Lang.Eng -> "Total Quizzes"
      }
   }

   fun answeredQuestions(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پرسیارە وەڵامدراوەکان"
         Lang.Eng -> "Answered Questions"
      }
   }

   fun correctAnswers(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "وەڵامە دروستەکان"
         Lang.Eng -> "Correct Answers"
      }
   }

   fun incorrectAnswers(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "وەڵامە هەڵەکان"
         Lang.Eng -> "Incorrect Answers"
      }
   }

   fun totalProgress(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پێشکەوتنی گشتی"
         Lang.Eng -> "Total Progress"
      }
   }

   fun seeMore(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "زیاتر ببینە"
         Lang.Eng -> "See More"
      }
   }

   fun seeLess(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کەمتر ببینە"
         Lang.Eng -> "See Less"
      }
   }

   fun subjectsProgress(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پێشکەوتنی بابەتەکان"
         Lang.Eng -> "Subjects Progress"
      }
   }

   fun language(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "زمان"
         Lang.Eng -> "Language"
      }
   }

   fun settings(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ڕێکخستنەکان"
         Lang.Eng -> "Settings"
      }
   }

   fun english(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ئینگلیزی"
         Lang.Eng -> "English"
      }
   }

   fun kurdish(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کوردی"
         Lang.Eng -> "Kurdish"
      }
   }

   fun selectLanguage(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "زمانێك هەڵبژێرە"
         Lang.Eng -> "Select Language"
      }
   }

   fun appearance(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ڕووکار"
         Lang.Eng -> "Appearance"
      }
   }

   fun light(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ڕووناک"
         Lang.Eng -> "Light"
      }
   }

   fun dark(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "تاریک"
         Lang.Eng -> "Dark"
      }
   }

   fun systemDefault(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ڕووکاری سیستەم"
         Lang.Eng -> "System Default"
      }
   }

   fun sheekar(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "شیکار"
         Lang.Eng -> "Sheekar"
      }
   }

   fun facebook(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "فەیسبووک"
         Lang.Eng -> "Facebook"
      }
   }

   fun instagram(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ئینستاگرام"
         Lang.Eng -> "Instagram"
      }
   }

   fun followOurFacebookPage(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پەیجی فەیسبووکمان فۆلۆو بکە"
         Lang.Eng -> "Follow our Facebook page"
      }
   }

   fun followOurInstagramPage(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پەیجی ئینستاگراممان فۆلۆو بکە"
         Lang.Eng -> "Follow our Instagram page"
      }
   }

   fun aboutUs(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "دەربارەی ئێمە"
         Lang.Eng -> "About Us"
      }
   }

   fun contactUs(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پەیوەندی بکە"
         Lang.Eng -> "Contact Us"
      }
   }

   fun rateUs(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "هەڵمانبسەنگێنە"
         Lang.Eng -> "Rate Us"
      }
   }

   fun noTopicsFound(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "هیچ بابەتێك نەدۆزرایەوە"
         Lang.Eng -> "No topics found"
      }
   }

   fun updateApp(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "نوێکردنەوەی ئاپ"
         Lang.Eng -> "Update App"
      }
   }

   fun upgrade(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "بەرزکردنەوە"
         Lang.Eng -> "Upgrade"
      }
   }

   fun free(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "بێ بەرامبەر"
         Lang.Eng -> "Free"
      }
   }

   fun pro(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "Pro"
         Lang.Eng -> "Pro"
      }
   }

   fun start(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "دەستپێبکە"
         Lang.Eng -> "Start"
      }
   }

   fun welcomeToSheekar(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "بەخێربێیت بۆ شیکار"
         Lang.Eng -> "Welcome to Sheekar"
      }
   }

   fun questionOutOf(currentAppLang: Lang, i: Int, i1: Int): String {
      return when (currentAppLang) {
         Lang.Krd -> "$i لە $i1 پرسیار"
         Lang.Eng -> "$i of $i1 Questions"
      }
   }

   fun showAnswer(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "وەڵامی دروست"
         Lang.Eng -> "Show Answer"
      }
   }

   fun explanation(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ڕوونکردنەوە"
         Lang.Eng -> "Explanation"
      }
   }

   fun sheekarWelcomeMessageSubtitle(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "شیکار ئەپێکە بۆ خوێندکارانی پۆلی دوانزەی ئامادەیی لە سەرانسەری هەرێمی کوردستان داڕێژراوە بۆ ئەوەی بە ئاسانی خۆیان لە بابەتەکانیان تاقی بکەنەوە بە شیکار کردنی پرسیاری تاقیکردنەوە نیشتمانییەکان."
         Lang.Eng -> "Sheekar is an application that has been designed for 12ᵗʰ grade high school students across the Kurdistan Region to enable them to easily test themselves on their subjects through the national exam questions."
      }
   }

   fun pathfinityWelcomeMessageSubtitle(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Pathfinity is your comprehensive career development platform that connects education with real-world opportunities. Explore courses tailored to your interests, receive guidance from industry mentors, access valuable internship programs, and engage with interactive live content to build your professional future."
         Lang.Krd -> "پاسفینیتی پلاتفۆرمێکی تەواوە بۆ گەشەپێدانی پیشەیی کە پەروەردە بە دەرفەتەکانی دنیای ڕاستەقینەوە دەبەستێتەوە. کۆرسی گونجاو لەگەڵ خولیاکانت بدۆزەوە، ڕێنمایی لە ڕاهێنەرانی بوارەکە وەربگرە، دەستت بە بەرنامەکانی ڕاهێنانی بەنرخ بگات، و بەشداری ناوەڕۆکی زیندووی کارلێکەر بکە بۆ بنیاتنانی داهاتووی پیشەییت."
      }
   }

   fun welcomeTo(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "بەخێربێیت بۆ"
         Lang.Eng -> "Welcome to"
      }
   }

   fun allTerms(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "گشت خولەکان"
         Lang.Eng -> "All Terms"
      }
   }

   fun allTermsSubtitle(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "شیکار گشت خولەکانی بەرایی و خولی یەکەم و دووەم و هەروەها خولەکانی تەواوکەری خولی دووەم (خولی سێیەم) لە ٢٠١٤-٢٠١٥ تا ٢٠٢٣-٢٠٢٤ لەخۆ دەگرێت."
         Lang.Eng -> "Sheekar includes all elementary, first, and second term exams, as well as the third term exams (corona term), from 2014-2015 to 2023-2024."
      }
   }

   fun kurdishAndEnglish(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کوردی و ئینگلیزی"
         Lang.Eng -> "Kurdish and English"
      }
   }

   fun kurdishAndEnglishSubtitle(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "شیکار پشتگیری لە بابەتە زانستییەکانی هەردوو سیستەمی ئینگلیزی و کوردی دەکات"
         Lang.Eng -> "Sheekar supports both Kurdish and English systems for the scientific subjects."
      }
   }

   fun upgradeToProToGetAllTheFeatures(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "بۆ پلەی پرۆ بەرزبکەرەوە بۆ کردنەوەی گشت وانەکان و بابەتەکان"
         Lang.Eng -> "Upgrade to Pro to get all the features and unlock all chapters and subjects"
      }
   }


   fun enter16DigitCode(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کۆدی ١٦ پیت بنووسە"
         Lang.Eng -> "Enter 16 digit code"
      }
   }

   fun notSixteenDigits(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کۆدەکە ١٦ پیت نییە"
         Lang.Eng -> "Code is not 16 digits"
      }
   }

   fun accessToAllCoursesAndFeatures(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "دەستگەیشتن بە هەموو کۆرسەکان و تایبەتمەندییەکان"
         Lang.Eng -> "Access to all courses and features"
      }
   }

   fun invalidCode(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کۆدەکە هەڵەیە"
         Lang.Eng -> "Code is invalid"
      }
   }

   fun invalidOrAlreadyRedeemedGiftCard(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کۆدەکە هەڵەیە یان پێشتر بەکارهێنراوە"
         Lang.Eng -> "Gift card is invalid or already redeemed"
      }
   }

   fun alreadyRedeemedCode(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کۆدەکە پێشتر بەکارهێنراوە"
         Lang.Eng -> "Code has been already redeemed before"
      }
   }

   fun getActivationCode(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کۆدی چالاك کردن بەدەستبێنە"
         Lang.Eng -> "Get Activation Code"
      }
   }

   fun accountStatus(currentAppLang: Lang, s: String): String {
      return when (currentAppLang) {
         Lang.Krd -> "حاڵەتی هەژمار: $s"
         Lang.Eng -> "Account Status: $s"
      }
   }

   fun expiresInXDays(currentAppLang: Lang, days: String): String {
      return when (currentAppLang) {
         Lang.Krd -> "بەسەرچوون لە $days ڕۆژ"
         Lang.Eng -> "Expires in $days days"
      }
   }

   fun youAreAlreadyPro(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "تۆ پرۆیت"
         Lang.Eng -> "You are a Pro"
      }
   }

   fun selectPaymentMethod(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "شێوازی پارەدان هەڵبژێرە"
         Lang.Eng -> "Select Payment Method"
      }
   }

   fun codeRedemption(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کۆد چالاککردن"
         Lang.Eng -> "Code Redemption"
      }
   }

   fun year(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ساڵ"
         Lang.Eng -> "Year"
      }
   }

   fun unlimitedAccessToAllFeatures(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "گشت خزمەتگوزاریەکان بێ سنوور"
         Lang.Eng -> "Unlimited access to all features"
      }
   }

   fun allSubjectsAndTopics(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "هەموو وانەکان و بابەتەکان"
         Lang.Eng -> "All subjects and topics"
      }
   }

   fun xQuestionsPlus(currentAppLang: Lang, i: Int): String {
      return when (currentAppLang) {
         Lang.Krd -> "$i+ پرسیار "
         Lang.Eng -> "$i+ Questions"
      }
   }

   fun xTerms(currentAppLang: Lang, i: Int): String {
      return when (currentAppLang) {
         Lang.Krd -> "$i خول"
         Lang.Eng -> "$i Terms"
      }
   }

   fun unlimitedQuizzes(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "تاقیکردنەوەی بێ سنوور"
         Lang.Eng -> "Unlimited Quizzes"
      }
   }

   fun comingSoon(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "بەمزوانە"
         Lang.Eng -> "Coming Soon"
      }
   }

   fun sendXThroughFIBToThisNumber(currentAppLang: Lang, s: String): String {
      return when (currentAppLang) {
         Lang.Krd -> "١. $s بنێرە بۆ ئەم ژمارەیە لە ڕێگەی FIB "
         Lang.Eng -> "1. Send $s through FIB to this number"
      }
   }

   fun sendXThroughOrYBasedOnYourDesiredPlanThroughFIBToThisNumber(currentAppLang: Lang, s: String, s1: String): String {
      return when (currentAppLang) {
         Lang.Krd -> "١. $s یان $s1 بنێرە بەپێی پلانی هەڵبژێردراوت لە ڕێگەی FIB بۆ ئەم ژمارەیە"
         Lang.Eng -> "1. Send $s or $s1 based on your desired plan through FIB to this number"
      }
   }

   fun sendUsTheScreenshotOfThePaymentThroughThisWhatsAppNumber(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "٢. وێنەی پارەدانەکە بنێرە بۆ ئەم ژمارەیە لە ڕێگەی وەتساپ"
         Lang.Eng -> "2. Send us the screenshot of the payment through this WhatsApp number"
      }
   }

   fun waitForUsToVerifyThePaymentAndSendYouTheCode(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "٣. چاوەڕێبکە هەتا دڵنیادەبینەوە لە پارەدان و کۆدەکەت بۆ دەنێرین"
         Lang.Eng -> "3. Wait for us to verify the payment and send you the code"
      }
   }

   fun purchaseCode(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کۆد کڕین"
         Lang.Eng -> "Purchase Code"
      }
   }

   fun terms(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "خولەکان"
         Lang.Eng -> "Terms"
      }
   }

   fun term(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "خول"
         Lang.Eng -> "Term"
      }
   }

   fun preparatory(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ئامەدەکاری - ئەم خولە تەنها یەکەم ساڵ هەبوو وەکو ئامادەکاری"
         Lang.Eng -> "Preparatory : This term was only available in the first year"
      }
   }

   fun elementary(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "بەرایی : ئەم خولە بۆ خوێندکارانی دەرەکین"
         Lang.Eng -> "Elementary : This term is for external students"
      }
   }

   fun clear(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "لایبەرە"
         Lang.Eng -> "Clear"
      }
   }

   fun linguistics(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "زمانەوانی"
         Lang.Eng -> "Linguistics"
      }
   }

   fun scientific(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "زانستی"
         Lang.Eng -> "Scientific"
      }
   }

   fun allSubjectsAllTerms(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "گشت وانەکان، گشت خولەکان"
         Lang.Eng -> "All Subjects, all Terms"
      }

   }

   fun progress(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پێشکەوتن"
         Lang.Eng -> "Progress"
      }
   }

   fun pleaseConnectToInternet(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "تکایە بە ئینتەرنێت پەیوەندی بکە"
         Lang.Eng -> "Please connect to the internet"
      }
   }

   fun pleaseWaitUntilDataIsFetched(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "تکایە چاوەڕێبە تا زانیاریەکان باربکرێن"
         Lang.Eng -> "Please wait until data is fetched"
      }
   }

   fun revealAnswer(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "وەڵامی دروست ببینە"
         Lang.Eng -> "Reveal Answer"
      }
   }

   fun previous(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پێشوو"
         Lang.Eng -> "Previous"
      }
   }

   fun gradeNow(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ئێستا ئەنجام ببینە"
         Lang.Eng -> "Grade Now"
      }
   }

   fun topic(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "بابەت"
         Lang.Eng -> "Topic"
      }
   }

   fun finish(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "تەواو"
         Lang.Eng -> "Finish"
      }
   }

   fun statistics(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ئامار"
         Lang.Eng -> "Statistics"
      }
   }

   fun result(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ئەنجام"
         Lang.Eng -> "Result"
      }
   }

   fun totalTime(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کاتی گشتی"
         Lang.Eng -> "Total Time"
      }
   }

   fun timePerAnsweredQuestion(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کات بۆ هەر پرسیارێکی شیکارکراو"
         Lang.Eng -> "Time per answered question"
      }
   }

   fun unansweredQuestions(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پرسیاری وەڵامنەدراو"
         Lang.Eng -> "Unanswered Questions"
      }
   }

   fun youHaveUnansweredQuestionsAreYouSureToGrade(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پرسیاری وەڵامنەدراوت هەیە، ئایا دڵنیای لە ئەنجام وەرگرتن؟"
         Lang.Eng -> "You have unanswered questions, are you sure to grade?"
      }
   }

   fun cancel(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "پاشگەزبوونەوە"
         Lang.Eng -> "Cancel"
      }
   }

   fun grade(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "ئەنجام بدە"
         Lang.Eng -> "Grade"
      }
   }

   fun thereIsAProblem(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "هەڵەیەک هەیە ؟"
         Lang.Eng -> "There is a problem ?"
      }
   }

   fun ifYouThinkThereIsAProblemWithinTheQuestionOrTheAnswerPleaseReportItHere(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "ئەگەر هەڵەیەك لە پرسیارەکە یان وەڵامەکە هەیە تکایە لێرە ئاگادارمان بکەرەوە."
         Lang.Eng -> "If you think there is a problem within the question or the answer, please report it here."
      }
   }

   fun submit(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "ناردن"
         Lang.Eng -> "Submit"
      }
   }

   fun xMoreWordToSubmit(x: Int, lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "تکایە $x وشەی زیاتر بنووسە بۆ ناردن"
         Lang.Eng -> "Please write $x more words to submit"
      }
   }

   fun pleaseReportItHere(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "تکایە لێرە ئاگادارمان بکەرەوە"
         Lang.Eng -> "Please report it here."
      }
   }

   fun lastQuiz(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "تاقیکردنەوەی پێشوو"
         Lang.Eng -> "Last quiz"
      }
   }

   fun noFavoriteQuestions(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "هیچ پرسیارێکی دڵخواز نییە"
         Lang.Eng -> "No favorite questions"
      }
   }

   fun favoriteQuestions(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پرسیاری دڵخواز"
         Lang.Eng -> "Favorite Questions"
      }
   }

   fun signedInOnAnotherDevice(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "لەسەر ئامێرێکی تر کراوەتەوە"
         Lang.Eng -> "You are signed in on another device"
      }
   }

   fun alreadySignedInOnAnotherDevicePleaseSignOutOnThisDeviceToFirst(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "لەسەر ئامێرێکی تر کراوەتەوە، تکایە لەم ئامێرە بچۆ دەرەوە"
         Lang.Eng -> "Already signed in on another device, please sign out on this device first"
      }
   }


   fun IfYouAreNotSignedInOnAnotherDevicePleaseContactSupport(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ئەگەر لەسەر ئامێرێکی تر نەکراوەتەوە، تکایە پەیوەندیمان پێوە بکە"
         Lang.Eng -> "If you are not signed in on another device, please contact support"
      }
   }

   fun tellUsYourInfo(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Tell us your info 😊️"
         Lang.Krd -> "زانیاریەکانت بنووسە 😊️"
      }
   }

   fun soWeCanAddressYouProperly(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "So we can address you properly."
         Lang.Krd -> "بۆ ئەوەی بتوانین بە ڕێگای دروست بانگت بکەین."
      }
   }

   fun fromWhichCity(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "From which city? 🗺️"
         Lang.Krd -> "لە کام شارەوە؟ 🗺️"
      }
   }

   fun pleaseTellUsYourGovernorate(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Please choose your governorate."
         Lang.Krd -> "تکایە پارێزگاکەت هەڵبژێرە."
      }
   }

   fun fromWhichSchool(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "From which school? 🏫"
         Lang.Krd -> "لە کام قوتابخانەوە؟ 🏫"
      }
   }

   fun pleaseChooseYourSchool(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Please choose your school."
         Lang.Krd -> "تکایە قوتابخانەکەت هەڵبژێرە."
      }
   }

   fun school(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "School"
         Lang.Krd -> "قوتابخانە"
      }
   }

   fun schoolName(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "School Name"
         Lang.Krd -> "ناوی قوتابخانە"
      }
   }


   fun schoolNotFound(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "School not found 😢"
         Lang.Krd -> "قوتابخانەکەت نەدۆزرایەوە 😢️"
      }
   }

   fun ifYourSchoolIsNotFoundPleaseWriteTheNameBelow(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Please either recheck the filters or write the name below."
         Lang.Krd -> "تکایە دووبارە ڕێکخستنەکان بپشکنە یان ناوی قوتابخانە بنووسە."
      }
   }

   fun editProfile(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Edit Profile"
         Lang.Krd -> "چاککردنی هەژمار"
      }
   }

   fun updateYourAppToTheLatestVersion(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Update your app to the latest version"
         Lang.Krd -> "ئاپەکەت نوێ بکەرەوە بۆ بەرزترین وەشان"
      }
   }

   fun update(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Update"
         Lang.Krd -> "نوێکردنەوە"
      }
   }

   fun becomeA(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Become a"
         Lang.Krd -> "ببە بە ئەندامێکی"
      }
   }

   fun toEnjoy(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "member to enjoy:"
         Lang.Krd -> "بۆ بەدەستهێنانی:"
      }
   }

   fun individual(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Individual"
         Lang.Krd -> "تاك"
      }
   }

   fun student(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Student"
         Lang.Krd -> "قوتابی"
      }
   }

   fun penta(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Penta"
         Lang.Krd -> "پێنجی"
      }
   }

   fun students(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Students"
         Lang.Krd -> "قوتابی"
      }
   }

   fun save(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Save"
         Lang.Krd -> "پاشەکەوت"
      }
   }

   fun redeem(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Redeem"
         Lang.Krd -> "چالاککردن"
      }
   }

   fun alreadyHaveACodeRedeemItHere(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Already have a code? Redeem it here"
         Lang.Krd -> "کۆدێک هەیە؟ لێرە چالاک بکە"
      }
   }

   fun chooseYourPlan(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Choose your plan"
         Lang.Krd -> "پلانەکەت هەڵبژێرە"
      }
   }

   fun adminPanel(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Admin Panel"
         Lang.Krd -> "پانێڵی ئەدمین"
      }
   }

   fun giftCards(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Gift Cards"
         Lang.Krd -> "کارتی داخڵبوون"
      }
   }

   fun selectAmount(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Select Amount"
         Lang.Krd -> "هەڵبژاردنی بڕ"
      }
   }

   fun generateAndCopy(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Generate & Copy"
         Lang.Krd -> "دروستکردن و کۆپیکردن"
      }
   }

   fun giftCardGenerated(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Gift card generated and copied to clipboard"
         Lang.Krd -> "کارتی داخڵبوون دروستکرا و کۆپیکرا"
      }
   }

   fun deleteAccount(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Delete Account"
         Lang.Krd -> "سڕینەوەی هەژمار"
      }
   }

   fun areYouSure(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Are you sure?"
         Lang.Krd -> "دڵنیایت؟"
      }
   }

   fun thisActionCantBeUndone(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "This action can't be undone!"
         Lang.Krd -> "ئەم کردارە ناتوانێ بگەڕێتەوە!"
      }
   }

   fun delete(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Delete"
         Lang.Krd -> "سڕینەوە"
      }
   }

   fun manageQuestions(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Manage Questions"
         Lang.Krd -> "بەڕێوبردنی پرسیارەکان"
      }
   }

   fun pathfinityDescription(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Pathfinity is a comprehensive educational platform designed specifically for students in Iraq seeking to enhance their career prospects. Our platform seamlessly integrates structured courses, personalized mentorship opportunities, valuable internships, and engaging live content to provide a holistic learning experience. Pathfinity serves as your bridge between academic knowledge and real-world professional skills, empowering you to navigate your career journey with confidence."
         Lang.Krd -> "پاسفینیتی پلاتفۆرمێکی فێرکاری تەواوە کە بەتایبەتی بۆ خوێندکارانی عێراق دروستکراوە کە دەیانەوێت هەلی کارەکانیان باشتر بکەن. پلاتفۆرمەکەمان بە شێوەیەکی ڕێکوپێک کۆرسە ڕێکخراوەکان، دەرفەتەکانی ڕێنیشاندانی تایبەت، ڕاهێنانی بەنرخ، و ناوەڕۆکی زیندووی ڕاکێش تێکەڵ دەکات بۆ پێشکەشکردنی ئەزموونێکی فێربوونی تەواو. پاسفینیتی وەک پردێک وایە لە نێوان زانیاری ئەکادیمی و کارامەییە پیشەییەکانی دنیای ڕاستەقینە، کە تواناتان پێدەدات بە متمانەوە گەشتی پیشەییتان ئاڕاستە بکەن."
      }
   }

   fun ourGoal(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Our Goal"
         Lang.Krd -> "ئامانجی ئێمە"
      }
   }

   fun pathfinityGoal(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Our goal at Pathfinity is to revolutionize education in Iraq by making high-quality, practical learning accessible to all ambitious students. We aim to create a new generation of skilled professionals equipped with both theoretical knowledge and hands-on experience. By connecting students directly with industry experts and employment opportunities, we seek to reduce unemployment, boost innovation, and contribute to Iraq's technological and economic growth. We envision a future where every student can chart their own path to success regardless of background or circumstances."
         Lang.Krd -> "ئامانجمان لە پاسفینیتی شۆڕشێکە لە بواری پەروەردەی عێراق لە ڕێگەی بەردەستکردنی فێربوونی کرداری و کوالێتی بەرز بۆ هەموو خوێندکارە خوازیارەکان. ئێمە هەوڵ دەدەین نەوەیەکی نوێی پیشەوەرانی شارەزا پێبگەیەنین کە هەم زانیاری تیۆری و هەم ئەزموونی دەستلێدانیان هەبێت. لە ڕێگەی پەیوەندیکردنی ڕاستەوخۆی خوێندکاران بە پسپۆڕانی بوارەکە و هەلی کارەوە، ئێمە هەوڵ دەدەین بێکاری کەم بکەینەوە، داهێنان بەهێز بکەین، و بەشداری بکەین لە گەشەی تەکنەلۆجی و ئابووری عێراق. ئێمە داهاتوویەک دەبینین کە تێیدا هەموو خوێندکارێک بتوانێت ڕێگای خۆی بەرەو سەرکەوتن دیاری بکات بەبێ گوێدانە پێشینە یان بارودۆخی."
      }
   }

   fun howItBegan(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "How it Began"
         Lang.Krd -> "چۆن دەستیپێکرد"
      }
   }

   fun pathfinityOrigin(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Pathfinity began as a passionate university project by three software engineering students who experienced firsthand the disconnect between their academic studies and industry requirements. Frustrated by limited access to practical learning opportunities in Iraq, they envisioned a platform that could provide the resources they wished they had during their educational journey. What started as a simple idea in a university dorm room has evolved into a comprehensive platform connecting thousands of students with learning resources, mentors, and career opportunities that were previously out of reach."
         Lang.Krd -> "پاسفینیتی وەک پڕۆژەیەکی پەرۆشی زانکۆ دەستی پێکرد لەلایەن سێ خوێندکاری ئەندازیاری سۆفتوێرەوە کە بە خۆیان هەستیان بە جیاوازی نێوان خوێندنە ئەکادیمییەکانیان و پێداویستییەکانی پیشەسازی کردبوو. بەهۆی دەستگەیشتنی سنووردار بە دەرفەتەکانی فێربوونی کرداری لە عێراق، ئەوان پلاتفۆرمێکیان بەخەیاڵ هات کە بتوانێت ئەو سەرچاوانە دابین بکات کە ئارەزوویان دەکرد لە کاتی گەشتی خوێندنیاندا هەبووایە. ئەوەی وەک بیرۆکەیەکی سادە لە ژوورێکی خوێندنگای زانکۆ دەستی پێکرد، بووە بە پلاتفۆرمێکی تەواو کە هەزاران خوێندکار بە سەرچاوەکانی فێربوون، ڕاهێنەران، و دەرفەتی پیشەییەوە دەبەستێتەوە کە پێشتر دەستیان پێی نەدەگەیشت."
      }
   }

   fun mathIsUnderChecking(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Math is under checking"
         Lang.Krd -> "بیرکاری لە ژێر چاککردندایە"
      }
   }

   fun custom(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Custom"
         Lang.Krd ->"تایبەت"
      }
   }

   fun limit(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Limit"
         Lang.Krd -> "سنوور"
      }
   }

   fun metadata(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Metadata"
         Lang.Krd -> "زانیاری"
      }
   }

   fun metadataDescription(currentAppLang: Lang) : String{
      return when (currentAppLang){
         Lang.Eng -> "Metadata can be any information you want, for example the name of the institute you are generating gift cards to, so we can refer to it later"
         Lang.Krd -> "زانیارییەکان دەتوانێت هەر زانیارییەک بێت کە تۆ دەتەوێت، بۆ نموونە ناوی ئەو دامەزراوەیەی کە کارتی داخڵبوونی بۆ دروست دەکەیت، بۆ ئەوەی دواتر بتوانین ئاماژەی پێ بکەین"
      }
   }

   fun metadataPlaceholder(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Eg. Yadgar Institute"
         Lang.Krd -> "نموونە: پەیمانگای یادگار"
      }
   }

   fun sendVerificationCode(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Send Verification Code"
         Lang.Krd -> "ناردنی کۆدی دڵنیابوونەوە"
      }
   }

   fun weWillSendVerificationCodeToYourEmail(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "We will send verification code to your email"
         Lang.Krd -> "کۆدی دڵینیابوونەوە بۆ ئیمەیڵەکەت دەنێرین"
      }
   }

   fun sentVerificationCodeToEmail(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Sent verification code to your email"
         Lang.Krd -> "کۆدی دڵنیابوونەوەمان بۆ ئیمەیڵەکەت نارد"
      }
   }

   fun enterTheVerificationCode(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Enter the verification code"
         Lang.Krd -> "کۆدی دڵنیابوونەوە بنووسە"
      }
   }

   fun didntReceiveTheCode(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Didn't receive the code?"
         Lang.Krd -> "کۆدەکەت بۆ نەهات؟"
      }
   }

   fun resendIn(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Resend in"
         Lang.Krd -> "دووبارە ناردن لە"
      }
   }

   fun resend(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Resend"
         Lang.Krd -> "بنێرەوە"
      }
   }

   fun emailUpdatedSuccessfully(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Email updated successfully 😄"
         Lang.Krd -> "ئیمەیڵەکە بە سەرکەوتوویی نوێکرا😄"
      }
   }


   fun continuee(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Continue"
         Lang.Krd -> "بەردەوامبە"
      }
   }

   fun syncNow(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Sync Now"
         Lang.Krd -> "باربکە"
      }
   }

   fun downloadData(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Download Data"
         Lang.Krd -> "بارکردنی زانیاری"
      }
   }

   fun reports(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Reports"
         Lang.Krd -> "راپۆرتەکان"
      }
   }

   fun noReportsFound(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "No reports found"
         Lang.Krd -> "هیچ راپۆرتێک نەدۆزرابوو"
      }
   }

   fun noMatchingReports(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "No matching reports found"
         Lang.Krd -> "هیچ راپۆرتێکی پەیوەندیدار نەدۆزرابوو"
      }
   }

   fun yourProgress(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Your Progress"
         Lang.Krd -> "پێشکەوتنی تۆ"
      }
   }

   fun completed(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Completed"
         Lang.Krd -> "تەواوکراوە"

      }
   }

   fun accuracy(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Accuracy"
         Lang.Krd -> "دروستی"
      }
   }

   fun searchQuizzes(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Search Quizzes"
         Lang.Krd -> "بەدواداچوون لە تاقیکردنەوەکان"
      }
   }

   fun questionCount(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Question Count"
         Lang.Krd -> "ژمارەی پرسیار"
      }
   }

   fun inProgress(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "In Progress"
         Lang.Krd -> "بەردەوام"
      }
   }

   fun noQuizzesFound(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "No quizzes found"
         Lang.Krd -> "هیچ تاقیکردنەوەیەک نەدۆزرابوو"
      }
   }

   fun tryAnotherFilter(currentAppLang: Lang): String {
      return when (currentAppLang){
         Lang.Eng -> "Try another filter"
         Lang.Krd -> "ڕێکخستنێکی تر تاقیبکەروە"
      }
   }

   fun allQuizzes(it: Lang) : String {
      return when (it) {
         Lang.Krd -> "هەموو تاقیکردنەوەکان"
         Lang.Eng -> "All Quizzes"
      }
   }

   fun completedQuizzes(it: Lang) : String{
      return when (it) {
         Lang.Krd -> "تاقیکردنەوەی تەواو کراوەکان"
         Lang.Eng -> "Completed Quizzes"
      }
   }

   // Add these to StringResources object
   fun inProgressQuizzes(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "تاقیکردنەوە بەردەوامەکان"
         Lang.Eng -> "In Progress Quizzes"
      }
   }

   fun highScoreQuizzes(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "تاقیکردنەوەی نمرە بەرزەکان"
         Lang.Eng -> "High Score Quizzes"
      }
   }

   fun newestFirst(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "نوێترین سەرەتا"
         Lang.Eng -> "Newest First"
      }
   }

   fun oldestFirst(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "کۆنترین سەرەتا"
         Lang.Eng -> "Oldest First"
      }
   }

   fun highestScoreFirst(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "بەرزترین نمرە سەرەتا"
         Lang.Eng -> "Highest Score First"
      }
   }

   fun lowestScoreFirst(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "نزمترین نمرە سەرەتا"
         Lang.Eng -> "Lowest Score First"
      }
   }

   fun quizNumber(lang: Lang, number: Int): String {
      return when (lang) {
         Lang.Krd -> "تاقیکردنەوە #$number"
         Lang.Eng -> "Quiz #$number"
      }
   }

   fun answered(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "وەڵامدراو"
         Lang.Eng -> "Answered"
      }
   }

   fun correct(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "ڕاست"
         Lang.Eng -> "Correct"
      }
   }

   fun incorrect(lang: Lang): String {
      return when (lang) {
         Lang.Krd -> "هەڵە"
         Lang.Eng -> "Incorrect"
      }
   }

   fun filter(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Filter & Sort"
         Lang.Krd -> "فلتەر و ڕێکخستن"
      }
   }

   fun filterBy(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Filter by"
         Lang.Krd -> "فلتەرکردن بە"
      }
   }

   fun filterByState(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Filter by state"
         Lang.Krd -> "فلتەرکردن بە دۆخ"
      }
   }

   fun sortBy(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Sort by"
         Lang.Krd -> "ڕێکخستن بە"
      }
   }

   fun sortByState(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Sort by State"
         Lang.Krd -> "ڕێکخستن بە پێی دۆخ"
      }
   }

   fun resetAll(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Reset All"
         Lang.Krd -> "ڕێکخستنەوەی هەموویان"
      }
   }

   fun reset(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Reset"
         Lang.Krd -> "ڕێکخستنەوە"
      }
   }

   fun date(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Date"
         Lang.Krd -> "بەروار"
      }
   }

   fun score(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "Score"
         Lang.Krd -> "نمرە"
      }
   }

   fun all(it: Lang) : String{
      return when (it) {
         Lang.Krd -> "هەموو"
         Lang.Eng -> "All"
      }
   }

   fun questions(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "پرسیارەکان"
         Lang.Eng -> "Questions"
      }
   }

   fun thisEmailHasBeenUsedOnAnotherAccount(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "This email has been used on another account"
         Lang.Krd -> "ئەم ئیمەیڵە پێشتر بۆ هەژمارێکی تر بەکارهێنراوە"
      }
   }

   fun quiz(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "تاقیکردنەوە"
         Lang.Eng -> "Quiz"
      }
   }

   fun totalQuestions(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "کۆی پرسیارەکان"
         Lang.Eng -> "Total Questions"
      }
   }

   fun logout(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "چوونە دەرەوە"
         Lang.Eng -> "Logout"
      }
   }

   fun edit(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "چاککردن"
         Lang.Eng -> "Edit"
      }
   }

   fun daysLeft(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ڕۆژ ماوە"
         Lang.Eng -> "Days Left"
      }
   }

   fun setTheLanguageForTheSubjectsAndTheApp(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "زمانەکان بۆ بابەتەکان و ئەپەکە دیاری بکە"
         Lang.Eng -> "Set the language for the subjects and the app"
      }
   }

   fun subscriptionInformation(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "زانیاری ئەندامبوون"
         Lang.Eng -> "Subscription Information"
      }
   }

   fun male(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "نێر"
         Lang.Eng -> "Male"
      }
   }

   fun female(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "مێ"
         Lang.Eng -> "Female"
      }
   }

   fun notSet(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Not Set"
         Lang.Krd -> "دیاری نەکراوە"
      }
   }

   fun developedBy(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Developed by"
         Lang.Krd -> "پەرەی پێدراوە لەلایەن"
      }
   }

   fun help(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Help"
         Lang.Krd -> "یارمەتی"
      }
   }

   fun support(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Support"
         Lang.Krd -> "پشتگیری"
      }
   }

   fun faq(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Frequently Asked Questions (FAQ)"
         Lang.Krd -> "پرسیارە باوەکان"
      }
   }

   fun privacyPolicy(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Privacy Policy"
         Lang.Krd -> "سیاسەتی تایبەتمەندێتی"
      }
   }

   fun dangerZone(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Danger Zone"
         Lang.Krd -> "ناوچەی خەتەر"
      }
   }

   fun success(lang: Lang): String {
      return when (lang) {
         Lang.Eng -> "✅  Success"
         Lang.Krd -> "✅  سەرکەوتووبوو"
      }
   }

   fun deleteAccountDescription(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Are you sure you want to delete your account? This action cannot be undone."
         Lang.Krd -> "دڵنیایت دەتەوێت هەژمارەکەت بسڕی؟ ئەم کردارە ناتوانرێت بگەڕێتەوە."
      }
   }

   fun title(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Title"
         Lang.Krd -> "سەردێڕ"
      }
   }

   fun internship(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Internship"
         Lang.Krd -> "تاقیکردنەوەی کارامەیی"
      }
   }

   fun internships(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Internships"
         Lang.Krd -> "تاقیکردنەوەی کارامەیی"
      }
   }

   fun pathfinity(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Pathfinity"
         Lang.Krd -> "Pathfinity"
      }
   }

   fun mentorshipGuidance(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Mentorship & Guidance"
         Lang.Krd -> "ڕێنمایی و ڕێبەری"
      }
   }

   fun mentorshipGuidanceDescription(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Get personalized guidance from experienced mentors to help you navigate your educational and career path effectively."
         Lang.Krd -> "ڕێنمایی تایبەت لە ڕێبەرانی بە ئەزموون وەربگرە بۆ یارمەتیدانت لە ڕێگای پەروەردەیی و پیشەییت بە شێوەیەکی کاریگەر."
      }
   }

   fun internshipPrograms(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Internship Programs"
         Lang.Krd -> "بەرنامەکانی ڕاهێنان"
      }
   }

   fun internshipProgramsDescription(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Access valuable internship opportunities with leading organizations to gain practical experience and enhance your professional skills."
         Lang.Krd -> "دەستگەیشتن بە دەرفەتەکانی ڕاهێنان لەگەڵ ڕێکخراوە پێشەنگەکان بۆ بەدەستهێنانی ئەزموونی کرداری و باشترکردنی تواناکانی پیشەییت."
      }
   }

   fun resendOtp(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Resend Code"
         Lang.Krd -> "دووبارە ناردنی کۆد"
      }
   }

   fun aboutUsDescription(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Pathfinity was born from the vision of three passionate software engineering students committed to transforming education in Iraq. Our platform bridges the gap between traditional education and practical career skills by offering comprehensive courses, personalized mentorship from industry experts, valuable internship opportunities with leading companies, and engaging live content. We believe in empowering students to forge their own path to success through accessible, high-quality learning resources that prepare them for the challenges of tomorrow's workforce."
         Lang.Krd -> "پاسفینیتی لە ڕوانگەی سێ خوێندکاری پەرۆشی ئەندازیاری سۆفتوێرەوە دروست بوو کە پابەندن بە گۆڕینی پەروەردە لە عێراق. پلاتفۆرمەکەمان بۆشایی نێوان پەروەردەی نەریتی و کارامەییە پیشەییەکان پڕ دەکاتەوە لە ڕێگەی پێشکەشکردنی کۆرسی تەواو، ڕێنیشاندانی تایبەت لەلایەن پسپۆڕانی بوارەکەوە، دەرفەتی ڕاهێنانی بەنرخ لەگەڵ کۆمپانیا پێشەنگەکان، و ناوەڕۆکی ڕاستەوخۆی ڕاکێش. ئێمە باوەڕمان بە تواناکردنی خوێندکاران هەیە بۆ دروستکردنی ڕێگای تایبەت بە خۆیان بەرەو سەرکەوتن لە ڕێگەی سەرچاوەی فێربوونی بەردەست و کوالێتی بەرز کە ئامادەیان دەکات بۆ تەحەدایەکانی هێزی کاری داهاتوو."
      }
   }

   fun unknown(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Eng -> "Unknown"
         Lang.Krd -> "نەزانراو"
      }
   }

   fun birthday(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ڕۆژی لەدایکبوون"
         Lang.Eng -> "Birthday"
      }
   }

   fun selectYourBirthday(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "تکایە ڕۆژی لەدایکبوونت دیاری بکە"
         Lang.Eng -> "Please select your date of birth"
      }
   }

   fun skills(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "شارەزاییەکان"
         Lang.Eng -> "Skills"
      }
   }

   fun addSkills(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "زیادکردنی شارەزایی"
         Lang.Eng -> "Add Skills"
      }
   }

   fun yourSkills(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "شارەزاییەکانی تۆ"
         Lang.Eng -> "Your Skills"
      }
   }

   fun noSkillsAdded(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "هیچ شارەزاییەک زیاد نەکراوە"
         Lang.Eng -> "No skills added yet"
      }
   }

   fun editSkills(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "دەستکاریکردنی شارەزاییەکان"
         Lang.Eng -> "Edit Skills"
      }
   }

   fun removeSkill(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "سڕینەوەی شارەزایی"
         Lang.Eng -> "Remove Skill"
      }
   }

   fun enterSkill(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "شارەزاییەک بنووسە"
         Lang.Eng -> "Enter a skill"
      }
   }

   fun skillsDescription(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "شارەزاییەکانت زیاد بکە بۆ ئەوەی پڕۆفایلەکەت باشتر دەربکەوێت"
         Lang.Eng -> "Add your skills to enhance your profile"
      }
   }

   fun availableInternships(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "ڕاهێنانە بەردەستەکان"
         Lang.Eng -> "Available Internships"
      }
   }

   fun courses(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "خولەکان"
         Lang.Eng -> "Courses"
      }
   }

   fun course(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "خول"
         Lang.Eng -> "Course"
      }
   }

   fun creator(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "دروستکەر"
         Lang.Eng -> "Creator"
      }
   }

   fun by(currentAppLang: Lang): String {
      return when (currentAppLang) {
         Lang.Krd -> "لە لایەن"
         Lang.Eng -> "By"
      }
   }

   fun searchCourses(appLang: Lang): String {
        return when (appLang) {
             Lang.Krd -> "بەدواداچوون لە خولەکان"
             Lang.Eng -> "Search Courses"
        }
   }

}