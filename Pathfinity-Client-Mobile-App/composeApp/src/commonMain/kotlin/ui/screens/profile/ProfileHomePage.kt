package ui.screens.profile

import AboutUsRoute
import DeleteAccountRoute
import EditBirthdayRoute
import EditEmailRoute
import EditGenderRoute
import EditNameRoute
import EditSkillsRoute
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import appVersion
import data.RepoAuth
import data.RepoSettingImpl
import domain.Appearance
import domain.Gender
import domain.Lang
import domain.Platform
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import pathfinity.composeapp.generated.resources.*
import platform.platform
import presentation.StringResources
import supabase.UserStudent
import ui.components.MyBaseModalSheet
import ui.components.MyHorizontalDivider
import ui.components.RotativeChevron
import ui.components.bottomNavBarHeight
import ui.components.buttons.LangButton
import ui.components.buttons.MyIconButton
import ui.components.environment.*
import ui.components.layout.ColumnC
import ui.components.layout.LtrLayout
import ui.components.layout.RowC
import ui.components.layout.RtlLayout
import ui.components.modifier.myBackground
import ui.platform.toPlatformDp
import ui.theme.red
import ui.theme.redContainer
import ui.utility.getFlag
import ui.utility.getIcon
import utilities.toInstagramTime
import utilities.toMillis

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
fun ProfileScreen(navController: NavController, repoSettingImpl: RepoSettingImpl, repoAuth: RepoAuth) {
   val uriHandler = LocalUriHandler.current
   val student = MyLocalUserStudent.current
   val settings = MyLocalSettings.current
   var signOutLoading by remember { mutableStateOf(false) }
   val snackbarHostState = remember { SnackbarHostState() }
   val scope = rememberCoroutineScope()
   val lang = currentAppLang()

   val onSignOut: () -> Unit = {
      scope.launch {
         signOutLoading = true
         repoAuth.signOut()
            .onError {
               scope.launch {
                  snackbarHostState.showSnackbar(message = it.getDescription(lang))
               }
            }
            .onSuccess {

            }
         signOutLoading = false
      }
   }

   val onDeleteAccount: () -> Unit = {

   }

   var isLanguageSelectionVisible by remember { mutableStateOf(false) }
   var isAppearanceSelectionVisible by remember { mutableStateOf(false) }

   if (isLanguageSelectionVisible) {
      LanguageSelectionBottomSheet(
         onDismissRequest = { isLanguageSelectionVisible = false },
         onLanguageSelected = { newLang ->
            scope.launch {
               repoSettingImpl.setLang(newLang)
            }
         }
      )
   }

   if (isAppearanceSelectionVisible) {
      AppearanceBottomSheet(
         onDismissRequest = { isAppearanceSelectionVisible = false },
         onAppearanceSelected = { appearance ->
            scope.launch {
               repoSettingImpl.setAppearance(appearance)
            }
         }
      )
   }

   Scaffold(
      modifier = Modifier.fillMaxSize(),
      snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
   ) { paddingValues ->
      Column(
         modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(20.dp)
            .padding(bottom = bottomNavBarHeight),
         horizontalAlignment = Alignment.CenterHorizontally
      ) {
         Column(
            modifier = Modifier.widthIn(max = 600.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
         ) {
            ProfileHeader(student, onSignOut, signOutLoading)
            PersonalInfoCard(
               profile = student,
               onNameClick = { navController.navigate(EditNameRoute) },
               onGenderClick = { navController.navigate(EditGenderRoute) },
               onEmailClick = { navController.navigate(EditEmailRoute) },
               onBirthdayClick = { navController.navigate(EditBirthdayRoute) },
               onSkillsClick = { navController.navigate(EditSkillsRoute) }
            )

            SubscriptionInfoCard(student)

            AppSettingsCard(
               onLanguageClick = { isLanguageSelectionVisible = true },
               onAppearanceClick = { isAppearanceSelectionVisible = true }
            )


            HelpCard(
               onAboutUsClick = { navController.navigate(AboutUsRoute) },
               onContactUsClick = { uriHandler.openUri(settings.whatsappLink) },
               onPrivacyPolicyClick = { uriHandler.openUri("https://pathfinity.app/privacy-policy") },
               onFAQClick = { uriHandler.openUri(settings.desktopLink) },
               onRateUsClick = {
                  val link = if (platform == Platform.Android) settings.googlePlayLink else settings.appstoreLink
                  uriHandler.openUri(link)
               }
            )

            DeleteButton(
               onClick = { navController.navigate(DeleteAccountRoute) }
            )

            RowC(Modifier.fillMaxWidth()) {
               SocialMediaButton(
                  res = Res.drawable.logo_facebook_96,
                  onClick = {
                     uriHandler.openUri(settings.facebookLink)
                  }
               )
               SocialMediaButton(
                  res = Res.drawable.logo_instagram_96,
                  onClick = {
                     uriHandler.openUri(settings.instagramLink)
                  }
               )
               SocialMediaButton(
                  res = Res.drawable.logo_whatsapp_96,
                  onClick = {
                     uriHandler.openUri(settings.whatsappLink)
                  }
               )
            }

            Text(
               text = "v$appVersion",
               style = MaterialTheme.typography.bodySmall,
               color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5F),
               modifier = Modifier.align(Alignment.CenterHorizontally)
            )
         }
      }
   }
}

@Composable
fun DeleteButton(
   modifier: Modifier = Modifier,
   onClick: () -> Unit
) {
   ListItem(
      modifier = modifier
         .myBackground(MaterialTheme.colorScheme.redContainer, onClick = onClick),
      headlineContent = {
         Text(
            text = StringResources.deleteAccount(currentAppLang()),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.red
         )
      },
      leadingContent = {
         Icon(
            painter = painterResource(Res.drawable.icon_delete),
            contentDescription = null,
            modifier = Modifier
               .size(20.dp),
            tint = MaterialTheme.colorScheme.red
         )
      },
      trailingContent = {
         RotativeChevron(isExpanded = false, color = MaterialTheme.colorScheme.red)
      },
      colors = ListItemDefaults.colors(containerColor = Color.Transparent)
   )
}


@Composable
private fun ProfileHeader(
   profile: UserStudent,
   onLogout: () -> Unit,
   logoutLoading: Boolean = false
) {

   val profileImage = profile.gender.getIcon()

   RowC {
      Image(
         painter = painterResource(profileImage),
         contentDescription = null,
         modifier = Modifier
            .myBackground(shape = CircleShape)
            .size(56.dp)
      )

      Text(
         text = profile.firstName ?: "Guest",
         style = MaterialTheme.typography.titleMedium,
         fontWeight = FontWeight.Bold,
      )

      Spacer(Modifier.weight(1f))



      MyIconButton(
         res = Res.drawable.icon_logout,
         contentColor = MaterialTheme.colorScheme.red,
         onClick = onLogout,
         isLoading = logoutLoading
      )
   }
}


@Composable
fun SocialMediaButton(
   res: DrawableResource,
   modifier: Modifier = Modifier,
   onClick: () -> Unit = {}
) {
   Image(
      painter = painterResource(res),
      contentDescription = null,
      modifier = modifier
         .myBackground(onClick = onClick)
         .padding(12.dp)
         .size(24.dp)

   )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun PersonalInfoCard(
   profile: UserStudent,
   onNameClick: () -> Unit,
   onGenderClick: () -> Unit,
   onEmailClick: () -> Unit,
   onBirthdayClick: () -> Unit,
   onSkillsClick: () -> Unit
) {
   Column(
      modifier = Modifier
         .fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
   ) {

      Text(
         text = StringResources.personalInformation(currentAppLang()),
         style = MaterialTheme.typography.titleMedium,
         fontWeight = FontWeight.Bold
      )


      Column(Modifier.myBackground()) {

         ProfileInfoItem(
            icon = Res.drawable.icon_person_outline,
            headlineContent = profile.fullName,
            trailingContent = { RotativeChevron(Modifier.alpha(0.50F)) },
            onClick = onNameClick
         )

         MyHorizontalDivider()

         ProfileInfoItem(
            icon = Res.drawable.icon_gender,
            headlineContent = profile.gender.getDescription(),
            trailingContent = { RotativeChevron(Modifier.alpha(0.50F)) },
            onClick = onGenderClick
         )

         MyHorizontalDivider()

         ProfileInfoItem(
            icon = Res.drawable.icon_email,
            headlineContent = profile.email ?: "",
            trailingContent = { RotativeChevron(Modifier.alpha(0.50F)) },
            onClick = onEmailClick
         )

         MyHorizontalDivider()

         val date: String =
            profile.birthdate?.let { Instant.fromEpochMilliseconds(it.toMillis()).toInstagramTime() } ?: ""
         ProfileInfoItem(
            icon = Res.drawable.icon_calendar,
            headlineContent = date,
            trailingContent = { RotativeChevron(Modifier.alpha(0.50F)) },
            onClick = onBirthdayClick
         )

         MyHorizontalDivider()

         ProfileInfoItem(
            icon = Res.drawable.icon_star,
            headlineContent = profile.skills?.joinToString(separator = ", ") ?: "",
            trailingContent = { RotativeChevron(Modifier.alpha(0.50F)) },
            onClick = onSkillsClick
         )


      }


   }
}

@Composable
fun Gender.getDescription(): String {
   return when (this) {
      Gender.Male -> StringResources.male(currentAppLang())
      Gender.Female -> StringResources.female(currentAppLang())
      Gender.Unknown -> StringResources.unknown(currentAppLang())
   }
}

@Composable
private fun ProfileInfoItem(
   modifier: Modifier = Modifier,
   icon: DrawableResource,
   headlineContent: String,
   trailingContent: @Composable (() -> Unit)? = null,
   onClick: (() -> Unit)? = null
) {
   ListItem(
      modifier = modifier.then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
      headlineContent = {
         Text(
            text = headlineContent,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(0.50F)
         )
      },
      leadingContent = {
         Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
               .alpha(0.65F)
               .size(20.dp)
         )
      },
      trailingContent = trailingContent,
      colors = ListItemDefaults.colors(containerColor = Color.Transparent)
   )
}

@Composable
private fun HelpItem(
   modifier: Modifier = Modifier,
   icon: DrawableResource,
   headlineContent: String,
   trailingContent: @Composable (() -> Unit)? = null,
   onClick: (() -> Unit)? = {},
   iconColor: Color = LocalContentColor.current
) {
   ListItem(
      modifier = modifier.then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
      headlineContent = {
         Text(
            text = headlineContent,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.alpha(0.50F)
         )
      },
      leadingContent = {
         Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
               .alpha(0.65F)
               .size(20.dp)
         )
      },
      trailingContent = trailingContent,
      colors = ListItemDefaults.colors(containerColor = Color.Transparent)
   )
}

@Composable
private fun SubscriptionInfoCard(profile: UserStudent) {
   Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
   ) {
      Text(
         text = StringResources.subscriptionInformation(currentAppLang()),
         style = MaterialTheme.typography.titleMedium,
         fontWeight = FontWeight.Bold
      )

      Column(
         modifier = Modifier
            .myBackground()
            .fillMaxWidth()
      ) {

         ProfileInfoItem(
            icon = Res.drawable.icon_coupon,
            headlineContent = if (profile.premium) StringResources.pro(currentAppLang()) else StringResources.free(
               currentAppLang()
            ),
         )

         profile.premiumExpiresAt?.let {
            MyHorizontalDivider()
            ProfileInfoItem(
               icon = Res.drawable.icon_timer,
               headlineContent = "${formatExpirationDate(it)} ${StringResources.daysLeft(currentAppLang())}"
            )
         }
      }
   }
}

@Composable
private fun HelpCard(
   onAboutUsClick: () -> Unit = {},
   onContactUsClick: () -> Unit = {},
   onPrivacyPolicyClick: () -> Unit = {},
   onFAQClick: () -> Unit = {},
   onRateUsClick: () -> Unit = {}
) {
   Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
   ) {
      Text(
         text = StringResources.support(currentAppLang()),
         style = MaterialTheme.typography.titleMedium,
         fontWeight = FontWeight.Bold
      )

      Column(
         modifier = Modifier
            .myBackground()
            .fillMaxWidth()
      ) {

         HelpItem(
            icon = Res.drawable.logo,
            headlineContent = StringResources.aboutUs(currentAppLang()),
            trailingContent = { RotativeChevron(Modifier.alpha(0.50F)) },
            onClick = onAboutUsClick
         )

         HelpItem(
            icon = Res.drawable.icon_support,
            headlineContent = StringResources.contactUs(currentAppLang()),
            trailingContent = { RotativeChevron(Modifier.alpha(0.50F)) },
            onClick = onContactUsClick
         )

         HelpItem(
            icon = Res.drawable.icon_privacy_policy,
            headlineContent = StringResources.privacyPolicy(currentAppLang()),
            trailingContent = { RotativeChevron(Modifier.alpha(0.50F)) },
            onClick = onPrivacyPolicyClick
         )

         /*         HelpItem(
                     icon = Res.drawable.icon_faq,
                     headlineContent = StringResources.faq(currentAppLang()),
                     trailingContent = { RotativeChevron(Modifier.alpha(0.50F)) },
                     onClick = onFAQClick
                  )*/

         HelpItem(
            icon = Res.drawable.icon_star,
            headlineContent = StringResources.rateUs(currentAppLang()),
            trailingContent = { RotativeChevron(Modifier.alpha(0.50F)) },
            onClick = onRateUsClick
         )

      }
   }
}

@Composable
private fun AppSettingsCard(
   onLanguageClick: () -> Unit,
   onAppearanceClick: () -> Unit
) {
   Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(8.dp)
   ) {
      Text(
         text = StringResources.settings(currentAppLang()),
         style = MaterialTheme.typography.titleMedium,
         fontWeight = FontWeight.Bold,
         modifier = Modifier
      )

      Column(
         modifier = Modifier
            .myBackground()
            .fillMaxWidth()
      ) {

         // Language setting
         val lang = if (currentAppLang() == Lang.Eng)
            StringResources.english(currentAppLang())
         else
            StringResources.kurdish(currentAppLang())

         AppSettingItem(
            icon = currentAppLang().getFlag(),
            title = StringResources.language(currentAppLang()),
            value = lang,
            onClick = onLanguageClick
         )

         MyHorizontalDivider()

         // Appearance setting
         AppSettingItem(
            icon = currentAppearance().getIcon(),
            title = StringResources.appearance(currentAppLang()),
            value = currentAppearance().getAppearanceString(currentAppLang()),
            onClick = onAppearanceClick
         )
      }
   }
}

@Composable
private fun AppSettingItem(icon: DrawableResource, title: String, value: String, onClick: () -> Unit) {
   ListItem(
      headlineContent = {
         Text(title, style = MaterialTheme.typography.labelMedium)
      },
      supportingContent = {
         Text(value, style = MaterialTheme.typography.bodySmall, modifier = Modifier.alpha(0.50F))
      },
      leadingContent = {
         Image(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier
               .alpha(0.75F)
               .size(24.dp)
         )
      },
      trailingContent = {
         Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier
               .alpha(0.5F)
               .size(20.dp)
         )
      },
      modifier = Modifier.clickable(onClick = onClick),
      colors = ListItemDefaults.colors(containerColor = Color.Transparent)
   )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LanguageSelectionBottomSheet(
   modifier: Modifier = Modifier,
   onDismissRequest: () -> Unit,
   onLanguageSelected: (Lang) -> Unit
) {
   val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
   val scope = rememberCoroutineScope()
   MyBaseModalSheet(
      onDismissRequest = onDismissRequest,
      sheetState = state,
      content = {
         Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
               .padding(24.dp)
         ) {

            ColumnC(spacing = 0.dp.toPlatformDp(8.dp)) {
               Text(
                  text = StringResources.language(currentAppLang()),
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.Bold,
               )
               Text(
                  text = StringResources.setTheLanguageForTheSubjectsAndTheApp(currentAppLang()),
                  style = MaterialTheme.typography.labelMedium,
                  color = Color.Gray,
                  textAlign = TextAlign.Center
               )

            }

            LtrLayout {
               RowC(Modifier.fillMaxWidth()) {
                  LangButton(
                     modifier = Modifier.weight(1f),
                     isActive = currentAppLang() == Lang.Eng,
                     flag = Res.drawable.flag_uk_whitened,
                     text = "English",
                     onClick = { onLanguageSelected(Lang.Eng) }
                  )
                  RtlLayout {
                     LangButton(
                        modifier = Modifier.weight(1f),
                        isActive = currentAppLang() == Lang.Krd,
                        flag = Res.drawable.flag_kurdistan_whitened,
                        text = "کوردی",
                        onClick = { onLanguageSelected(Lang.Krd) }
                     )
                  }
               }
            }
         }
      }
   )
}

@Composable
private fun AppearanceButton(
   modifier: Modifier = Modifier,
   isActive: Boolean,
   icon: DrawableResource,
   text: String,
   onClick: () -> Unit,
) {
   val engButtonBackground by animateColorAsState(
      targetValue = if (isActive) MaterialTheme.colorScheme.secondary else Color.Transparent,
      animationSpec = tween(500)
   )

   val engButtonForeground by animateColorAsState(
      targetValue = if (isActive) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onBackground,
      animationSpec = tween(500)
   )

   OutlinedButton(
      modifier = modifier,
      onClick = onClick,
      content = {
         RowC {
            Image(
               painter = painterResource(resource = icon),
               contentDescription = null,
               modifier = Modifier
                  .size(24.dp),
               contentScale = ContentScale.Fit
            )
            Text(text = text)
         }
      },
      shape = RoundedCornerShape(8.dp),
      colors = ButtonDefaults.outlinedButtonColors(
         contentColor = engButtonForeground,
         containerColor = engButtonBackground
      ),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f))
   )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppearanceBottomSheet(
   modifier: Modifier = Modifier,
   onDismissRequest: () -> Unit,
   onAppearanceSelected: (Appearance) -> Unit
) {
   val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
   MyBaseModalSheet(
      modifier = modifier,
      sheetState = state,
      content = {
         Column(
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
               .padding(20.dp)
               .fillMaxWidth()
         ) {
            Text(
               text = StringResources.appearance(currentAppLang()),
               style = MaterialTheme.typography.headlineSmall,
               fontWeight = FontWeight.Bold,
            )

            Appearance.values().forEach { appearance: Appearance ->
               AppearanceButton(
                  modifier = Modifier.fillMaxWidth(),
                  isActive = appearance == currentAppearance(),
                  icon = appearance.getIcon(),
                  text = appearance.getAppearanceString(currentAppLang()),
                  onClick = {
                     onAppearanceSelected(appearance)
                  }
               )
            }
         }
      },
      onDismissRequest = onDismissRequest
   )
}

// Helper function to format the expiration date
private fun formatExpirationDate(instant: Instant): String {
   val currentTime = Clock.System.now()
   val remainingDays = (instant - currentTime).inWholeDays
   return remainingDays.toString()
}

private fun Appearance.getAppearanceString(lang: Lang): String {
   return when (this) {
      Appearance.Light -> StringResources.light(lang)
      Appearance.Dark -> StringResources.dark(lang)
      Appearance.SystemDefault -> StringResources.systemDefault(lang)
   }
}

private fun Appearance.getIcon(): DrawableResource {
   return when (this) {
      Appearance.Light -> Res.drawable.icon_sun
      Appearance.Dark -> Res.drawable.icon_moon
      Appearance.SystemDefault -> Res.drawable.icon_moon_sun
   }
}
