package ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.fill.Calendar
import data.RepoAuth
import domain.Gender
import domain.validators.EmailValidator
import domain.validators.OtpValidator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.painterResource
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.icon_gender
import pathfinity.composeapp.generated.resources.icon_star
import presentation.StringResources
import ui.components.Dot
import ui.components.MyTopAppBar
import ui.components.OnboardingDotsIndicator
import ui.components.buttons.MyButton
import ui.components.buttons.SaveButton
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.layout.LtrLayout
import ui.components.layout.RowC
import ui.components.modifier.myBackground
import ui.components.modifier.noIndicationClickable
import ui.components.textfield.MyFilledTextField
import ui.components.textfield.MyOutlineTextField
import ui.platform.toPlatformDp
import ui.utility.GenderIcon
import utilities.fromMillis
import utilities.toMillis

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditScreen(
   navController: NavController,
   repoAuth: RepoAuth
) {
   val profile = MyLocalUserStudent.current
   val snackbarState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   val focusManager = LocalFocusManager.current
   val lang = currentAppLang()

   // State variables for all profile fields
   var isLoading by remember { mutableStateOf(false) }

   // Email state variables
   var tempEmail by remember(profile.email) { mutableStateOf(profile.email ?: "") }
   var sentEmailTo by remember { mutableStateOf("") }
   var otpSent by remember { mutableStateOf(false) }
   var otp by remember { mutableStateOf("") }
   var resendCooldown by remember { mutableIntStateOf(0) }
   var emailSendLoading by remember { mutableStateOf(false) }
   var emailVerifyLoading by remember { mutableStateOf(false) }
   val otpFieldFocusRequester = remember { FocusRequester() }
   val isEmailValid = EmailValidator.isValidInstagramEmail(tempEmail)

   // Name state variables
   var tempFirstName by remember(profile.firstName) { mutableStateOf(profile.firstName ?: "") }
   var tempLastName by remember(profile.lastName) { mutableStateOf(profile.lastName ?: "") }

   // Gender state variables
   var tempGender by remember(profile.gender) { mutableStateOf(profile.gender) }

   // Skills state variables
   val currentSkills = profile.skills ?: emptyList()
   var skillsList by remember(profile.skills) { mutableStateOf(currentSkills) }
   var newSkill by remember { mutableStateOf("") }

   // Birthday state variables
   val datePickerState = rememberDatePickerState(initialSelectedDateMillis = profile.birthdate?.toMillis())
   val selectedDate = datePickerState.selectedDateMillis?.let { LocalDate.fromMillis(it) }

   // Pager state
   val pagerState = rememberPagerState(pageCount = { 5 })

   // Function to start resend cooldown
   fun startResendCooldown() {
      scope.launch {
         resendCooldown = 5
         while (resendCooldown > 0) {
            delay(1000)
            resendCooldown -= 1
         }
      }
   }

   // Function to send verification email
   fun sendVerificationEmail(isResend: Boolean = false) {
      scope.launch {
         emailSendLoading = true
         repoAuth.requestUpdateEmail(tempEmail)
            .onSuccess {
               otpSent = true
               sentEmailTo = tempEmail
               launch {
                  snackbarState.showSnackbar(message = "✅  ${StringResources.sentVerificationCodeToEmail(lang)}\n${sentEmailTo}")
               }
            }
            .onError {
               launch {
                  snackbarState.showSnackbar(it.getDescription(lang))
               }
            }

         if (isResend || otpSent) {
            startResendCooldown()
         }

         emailSendLoading = false
      }
   }

   // Function to verify OTP
   fun verifyOtp() {
      scope.launch {
         emailVerifyLoading = true
         repoAuth.verifyUpdateEmailOtp(tempEmail, otp)
            .onSuccess {
               snackbarState.showSnackbar(StringResources.success(lang))
               // Move to next page on success
               scope.launch {
                  delay(1000)
                  pagerState.animateScrollToPage(1)
               }
            }
            .onError {
               snackbarState.showSnackbar(it.getDescription(lang))
            }
         emailVerifyLoading = false
      }
   }

   // Function to save all profile changes
   fun saveAllChanges() {
      scope.launch {
         isLoading = true

         // Create updated profile with all changes
         val updatedProfile = profile.copy(
            firstName = tempFirstName,
            lastName = tempLastName,
            genderString = tempGender.name.lowercase(),
            skills = skillsList,
            birthdate = selectedDate
         )

         // If email was verified (indicated by successful OTP verification), update it
         if (otpSent && OtpValidator.isOtpValid(otp)) {
            // Email is updated through the verification process
         }

         repoAuth.saveUserStudent(updatedProfile)
            .onSuccess {
               launch {
                  launch {
                     snackbarState.showSnackbar(StringResources.success(lang))
                  }
                  delay(1000)
                  navController.navigateUp()
               }
            }
            .onError {
               snackbarState.showSnackbar(it.getDescription(lang))
            }

         isLoading = false
      }
   }

   // Check if any changes were made
   val isProfileChanged = tempFirstName != profile.firstName ||
           tempLastName != profile.lastName ||
           tempGender != profile.gender ||
           skillsList != currentSkills ||
           datePickerState.selectedDateMillis != profile.birthdate?.toMillis() ||
           (otpSent && OtpValidator.isOtpValid(otp) && tempEmail != profile.email)

   // Create dots for pager indicator
   val dots = remember(pagerState.currentPage) {
      List(pagerState.pageCount) { index ->
         Dot(isCompleted = index <= pagerState.currentPage)
      }
   }

   Scaffold(
      modifier = Modifier
         .fillMaxSize()
         .noIndicationClickable { focusManager.clearFocus() },
      topBar = { MyTopAppBar(title = StringResources.editProfile(lang)) { navController.navigateUp() } },
      snackbarHost = { SnackbarHost(hostState = snackbarState) },
      bottomBar = {
         Column(
            modifier = Modifier.padding(16.dp)
         ) {
            OnboardingDotsIndicator(
               totalDots = dots,
               selectedIndex = pagerState.currentPage,
               modifier = Modifier
                  .align(Alignment.CenterHorizontally)
                  .padding(bottom = 16.dp),
               defaultColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )

            if (pagerState.currentPage == pagerState.pageCount - 1) {
               // Show Save button on last page
               SaveButton(
                  modifier = Modifier.fillMaxWidth(),
                  isActive = isProfileChanged,
                  onClick = { saveAllChanges() },
                  isLoading = isLoading
               )
            } else {
               // Show Next button on other pages
               MyButton(
                  onClick = {
                     scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                     }
                  },
                  text = StringResources.next(lang),
                  enabled = true,
                  modifier = Modifier.fillMaxWidth(),
                  containerColor = MaterialTheme.colorScheme.primary,
                  contentColor = MaterialTheme.colorScheme.onPrimary
               )
            }
         }
      }
   ) { paddingValues ->
      HorizontalPager(
         state = pagerState,
         modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 16.dp, vertical = 8.dp),
         verticalAlignment = Alignment.Top
      ) { page ->
         when (page) {
            0 -> EmailEditPage(
               tempEmail = tempEmail,
               onEmailChange = { tempEmail = it },
               otp = otp,
               onOtpChange = { otp = it },
               otpSent = otpSent,
               isEmailValid = isEmailValid,
               resendCooldown = resendCooldown,
               currentEmail = profile.email,
               onSendVerification = { sendVerificationEmail() },
               onVerifyOtp = { verifyOtp() },
               sendLoading = emailSendLoading,
               verifyLoading = emailVerifyLoading,
               otpFieldFocusRequester = otpFieldFocusRequester
            )
            1 -> NameEditPage(
               firstName = tempFirstName,
               lastName = tempLastName,
               onFirstNameChange = { tempFirstName = it },
               onLastNameChange = { tempLastName = it }
            )
            2 -> GenderEditPage(
               gender = tempGender,
               onGenderChange = { tempGender = it }
            )
            3 -> SkillsEditPage(
               skillsList = skillsList,
               onSkillsChange = { skillsList = it },
               newSkill = newSkill,
               onNewSkillChange = { newSkill = it }
            )
            4 -> BirthdayEditPage(
               datePickerState = datePickerState
            )
         }
      }
   }
}

@Composable
fun EmailEditPage(
   tempEmail: String,
   onEmailChange: (String) -> Unit,
   otp: String,
   onOtpChange: (String) -> Unit,
   otpSent: Boolean,
   isEmailValid: Boolean,
   resendCooldown: Int,
   currentEmail: String?,
   onSendVerification: () -> Unit,
   onVerifyOtp: () -> Unit,
   sendLoading: Boolean,
   verifyLoading: Boolean,
   otpFieldFocusRequester: FocusRequester
) {
   val lang = currentAppLang()

   Column(
      modifier = Modifier
         .fillMaxSize()
         .verticalScroll(rememberScrollState())
         .padding(vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top
   ) {
      Icon(
         imageVector = Icons.Outlined.Email,
         contentDescription = null,
         tint = MaterialTheme.colorScheme.primary,
         modifier = Modifier
            .myBackground(shape = CircleShape, elevation = 1.dp)
            .padding(16.dp)
            .size(32.dp),
      )

      Text(
         text = StringResources.setEmail(lang),
         style = MaterialTheme.typography.titleLarge,
         fontWeight = FontWeight.Bold,
         modifier = Modifier.padding(top = 8.dp)
      )

      LtrLayout() {
         MyFilledTextField(
            value = tempEmail,
            icon = Icons.Filled.Email,
            placeholder = "example@gmail.com",
            onValueChange = { onEmailChange(it.replace(" ", "")) },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            singleLine = true
         )
      }

      MyButton(
         modifier = Modifier
            .fillMaxWidth(0.70F)
            .padding(top = 16.dp),
         onClick = onSendVerification,
         text = if (resendCooldown > 0) "${StringResources.resendIn(lang)} ${resendCooldown}s" else StringResources.sendVerificationCode(lang),
         enabled = isEmailValid && resendCooldown == 0 && tempEmail != currentEmail,
         containerColor = MaterialTheme.colorScheme.primary,
         contentColor = MaterialTheme.colorScheme.onPrimary,
         isLoading = sendLoading
      )

      AnimatedVisibility(otpSent) {
         ColumnC(Modifier.padding(top = 32.dp)) {
            HorizontalDivider()
            Text(
               text = StringResources.enterTheVerificationCode(lang),
               style = MaterialTheme.typography.titleLarge,
               fontWeight = FontWeight.Bold,
               modifier = Modifier
                  .align(Alignment.Start)
                  .padding(top = 16.dp)
            )

            RowC(modifier = Modifier.padding(top = 8.dp)) {
               LtrLayout {
                  MyOutlineTextField(
                     value = otp,
                     placeholder = "123456",
                     onValueChange = {
                        if (it.all { char -> char.isDigit() } && it.length <= 6) {
                           onOtpChange(it)
                        }
                     },
                     keyboardType = KeyboardType.Number,
                     modifier = Modifier
                        .width(120.dp)
                        .focusRequester(otpFieldFocusRequester)
                  )
               }

               MyButton(
                  text = StringResources.verify(lang),
                  onClick = onVerifyOtp,
                  modifier = Modifier.weight(1f),
                  enabled = OtpValidator.isOtpValid(otp),
                  isLoading = verifyLoading,
                  containerColor = MaterialTheme.colorScheme.primary,
                  contentColor = MaterialTheme.colorScheme.onPrimary
               )
            }
         }
      }
   }
}

@Composable
fun NameEditPage(
   firstName: String,
   lastName: String,
   onFirstNameChange: (String) -> Unit,
   onLastNameChange: (String) -> Unit
) {
   val lang = currentAppLang()

   Column(
      modifier = Modifier
         .fillMaxSize()
         .verticalScroll(rememberScrollState())
         .padding(vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top
   ) {
      Text(
         text = StringResources.name(lang),
         style = MaterialTheme.typography.titleLarge,
         fontWeight = FontWeight.Bold,
         modifier = Modifier.padding(bottom = 16.dp)
      )

      RowC {
         ColumnC(Modifier.weight(1f)) {
            Text(
               text = StringResources.firstName(lang),
               style = MaterialTheme.typography.titleLarge,
               color = MaterialTheme.colorScheme.onSurface,
               fontWeight = FontWeight.Bold,
               modifier = Modifier.fillMaxWidth()
            )
            MyOutlineTextField(
               value = firstName,
               onValueChange = onFirstNameChange,
               modifier = Modifier.fillMaxWidth(),
               singleLine = true
            )
         }

         ColumnC(Modifier.weight(1f)) {
            Text(
               text = StringResources.lastName(lang),
               style = MaterialTheme.typography.titleLarge,
               color = MaterialTheme.colorScheme.onSurface,
               fontWeight = FontWeight.Bold,
               modifier = Modifier.fillMaxWidth()
            )
            MyOutlineTextField(
               value = lastName,
               onValueChange = onLastNameChange,
               modifier = Modifier.fillMaxWidth(),
               singleLine = true
            )
         }
      }
   }
}

@Composable
fun GenderEditPage(
   gender: Gender,
   onGenderChange: (Gender) -> Unit
) {
   val lang = currentAppLang()

   Column(
      modifier = Modifier
         .fillMaxSize()
         .verticalScroll(rememberScrollState())
         .padding(vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top
   ) {
      Icon(
         painter = painterResource(Res.drawable.icon_gender),
         contentDescription = "Gender",
         modifier = Modifier
            .myBackground(shape = CircleShape)
            .padding(16.dp)
            .size(32.dp),
      )

      Text(
         text = StringResources.gender(lang),
         style = MaterialTheme.typography.titleLarge,
         fontWeight = FontWeight.Bold,
         modifier = Modifier.padding(top = 8.dp)
      )

      Text(
         text = StringResources.chooseYourGender(lang),
         style = MaterialTheme.typography.bodyMedium,
         color = Color.Gray,
         textAlign = TextAlign.Center,
         modifier = Modifier.padding(top = 4.dp)
      )

      RowC(Modifier.padding(top = 24.dp)) {
         val genders = listOf(Gender.Male, Gender.Female)
         genders.forEach {
            GenderIcon(
               modifier = Modifier.weight(1f),
               gender = it,
               isActive = it == gender,
               onClick = { onGenderChange(it) }
            )
         }
      }
   }
}

@Composable
fun SkillsEditPage(
   skillsList: List<String>,
   onSkillsChange: (List<String>) -> Unit,
   newSkill: String,
   onNewSkillChange: (String) -> Unit
) {
   val lang = currentAppLang()

   Column(
      modifier = Modifier
         .fillMaxSize()
         .verticalScroll(rememberScrollState())
         .padding(vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top
   ) {
      Icon(
         painter = painterResource(Res.drawable.icon_star),
         contentDescription = "Skills",
         modifier = Modifier
            .myBackground(shape = CircleShape)
            .padding(16.dp)
            .size(32.dp),
      )

      Text(
         text = StringResources.editSkills(lang),
         style = MaterialTheme.typography.titleLarge,
         fontWeight = FontWeight.Bold,
         modifier = Modifier.padding(top = 8.dp)
      )

      Text(
         text = StringResources.skillsDescription(lang),
         style = MaterialTheme.typography.bodyMedium,
         color = Color.Gray,
         textAlign = TextAlign.Center,
         modifier = Modifier.padding(top = 4.dp)
      )

      Row(
         modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
         horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
         OutlinedTextField(
            value = newSkill,
            onValueChange = onNewSkillChange,
            modifier = Modifier.weight(1f),
            label = { Text(StringResources.enterSkill(lang)) },
            singleLine = true
         )

         IconButton(
            onClick = {
               if (newSkill.isNotBlank() && !skillsList.contains(newSkill)) {
                  onSkillsChange(skillsList + newSkill)
                  onNewSkillChange("")
               }
            },
            modifier = Modifier.align(Alignment.CenterVertically)
         ) {
            Icon(Icons.Default.Add, contentDescription = "Add Skill")
         }
      }

      Text(
         text = StringResources.yourSkills(lang),
         style = MaterialTheme.typography.titleMedium,
         fontWeight = FontWeight.Bold,
         modifier = Modifier
            .align(Alignment.Start)
            .padding(top = 24.dp)
      )

      if (skillsList.isEmpty()) {
         Text(
            text = StringResources.noSkillsAdded(lang),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
         )
      } else {
         FlowRow(
            modifier = Modifier
               .fillMaxWidth()
               .padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
         ) {
            skillsList.forEach { skill ->
               InputChip(
                  selected = true,
                  onClick = { },
                  label = { Text(skill) },
                  trailingIcon = {
                     IconButton(
                        onClick = { onSkillsChange(skillsList.filter { it != skill }) },
                        modifier = Modifier.size(24.dp)
                     ) {
                        Icon(
                           Icons.Default.Add,
                           contentDescription = StringResources.removeSkill(lang),
                           modifier = Modifier.rotate(45f)
                        )
                     }
                  }
               )
            }
         }
      }
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BirthdayEditPage(
   datePickerState: DatePickerState
) {
   val lang = currentAppLang()

   Column(
      modifier = Modifier
         .fillMaxSize()
         .verticalScroll(rememberScrollState())
         .padding(vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Top
   ) {
      Icon(
         imageVector = EvaIcons.Fill.Calendar,
         contentDescription = "Birthday",
         modifier = Modifier
            .myBackground(shape = CircleShape)
            .padding(16.dp)
            .size(32.dp),
      )

      Text(
         text = StringResources.birthday(lang),
         style = MaterialTheme.typography.titleLarge,
         fontWeight = FontWeight.Bold,
         modifier = Modifier.padding(top = 8.dp)
      )

      Text(
         text = StringResources.selectYourBirthday(lang),
         style = MaterialTheme.typography.bodyMedium,
         color = Color.Gray,
         textAlign = TextAlign.Center,
         modifier = Modifier.padding(top = 4.dp)
      )

      DatePicker(
         state = datePickerState,
         showModeToggle = false,
         modifier = Modifier.padding(top = 16.dp)
      )
   }
}