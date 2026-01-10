import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import data.RepoSettingImpl
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource
import org.koin.compose.koinInject
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.icon_book_fill
import pathfinity.composeapp.generated.resources.icon_book_outline
import pathfinity.composeapp.generated.resources.icon_home_fill
import pathfinity.composeapp.generated.resources.icon_home_outline
import pathfinity.composeapp.generated.resources.icon_internship
import pathfinity.composeapp.generated.resources.icon_internship_outline
import pathfinity.composeapp.generated.resources.icon_message_fill
import pathfinity.composeapp.generated.resources.icon_message_outline
import pathfinity.composeapp.generated.resources.icon_person_fill
import pathfinity.composeapp.generated.resources.icon_person_outline
import ui.components.layout.LtrLayout
import ui.screens.HomeScreen
import ui.screens.OnBoardingScreen
import ui.screens.mentorship.AlumniDetailScreen
import ui.screens.mentorship.AlumniListScreen
import ui.screens.authentication.OtpScreen
import ui.screens.authentication.SigningScreen
import ui.screens.authentication.SignupScreen
import ui.screens.course.CourseDetailScreen
import ui.screens.course.CourseHomeScreen
import ui.screens.course.CourseVideoScreen
import ui.screens.course.CreatorDetailScreen
import ui.screens.course.LiveScreen
import ui.screens.internship.CompanyDetailScreen
import ui.screens.internship.InternshipDetailScreen
import ui.screens.internship.InternshipsScreen
import ui.screens.mentorship.AlumniChatScreen
import ui.screens.mentorship.AlumniChatsScreen
import ui.screens.payment.AcquireCodeScreen
import ui.screens.payment.CodeActivationScreen
import ui.screens.payment.PaymentScreen
import ui.screens.profile.AboutUsScreen
import ui.screens.profile.DeleteAccountScreen
import ui.screens.profile.EditBirthdayScreen
import ui.screens.profile.EditEmailScreen
import ui.screens.profile.EditGenderScreen
import ui.screens.profile.EditNameScreen
import ui.screens.profile.EditSkillsScreen
import ui.screens.profile.ProfileEditScreen
import ui.screens.profile.ProfileScreen

sealed interface Graph
sealed interface Route
sealed interface BottomBarGraph : Graph

data class GraphInfo(
   val nameEng: String,
   val nameKrd: String,
   val selectedIcon: DrawableResource,
   val unselectedIcon: DrawableResource = selectedIcon
)

fun BottomBarGraph.getGraphInfo(): GraphInfo {
   return when (this) {
      is HomeGraph -> GraphInfo("Home", "سەرەکی", Res.drawable.icon_home_fill, Res.drawable.icon_home_outline)
      is ProfileGraph -> GraphInfo("Profile", "هەژمار", Res.drawable.icon_person_fill, Res.drawable.icon_person_outline)
      is InternshipsGraph -> GraphInfo("Internships", "کاری مەشقکردن", Res.drawable.icon_internship, Res.drawable.icon_internship_outline)
      is CourseGraph ->  GraphInfo("Courses", "خولەکان", Res.drawable.icon_book_fill, Res.drawable.icon_book_outline)
      is AlumniGraph -> GraphInfo("Mentors" , "ڕاهێنەران" , Res.drawable.icon_message_fill , Res.drawable.icon_message_outline)
   }
}

val shouldDisplayNavBar = listOf(
   HomeRoute::class,
   ProfileRoute::class,
   InternshipsRoute::class,
   CourseHomeRoute::class,
   AlumniRoute::class
)

@Serializable data object HomeGraph : BottomBarGraph
@Serializable data object HomeRoute : Route

fun NavGraphBuilder.homeGraph(navController: NavController) {
   navigation<HomeGraph>(startDestination = HomeRoute) {
      composable<HomeRoute> {
         HomeScreen(navController = navController, koinInject())
      }
   }
}


@Serializable data object ProfileGraph : BottomBarGraph
@Serializable data object ProfileRoute
@Serializable data object AboutUsRoute
@Serializable data object EditNameRoute
@Serializable data object DeleteAccountRoute
@Serializable data object EditGenderRoute
@Serializable data object EditEmailRoute
@Serializable data object EditBirthdayRoute
@Serializable data object EditSkillsRoute
@Serializable data object EditProfileRoute

fun NavGraphBuilder.profileGraph(navController: NavController) {
   navigation<ProfileGraph>(startDestination = ProfileRoute) {
      composable<ProfileRoute> {
         ProfileScreen(navController, koinInject(), koinInject())
      }

      composable<EditNameRoute> {
         EditNameScreen(navController, koinInject())
      }

      composable<EditGenderRoute> {
         EditGenderScreen(navController, koinInject())
      }

      composable<DeleteAccountRoute> {
         DeleteAccountScreen(navController, koinInject())
      }

      composable<AboutUsRoute> {
         AboutUsScreen(navController)
      }

      composable<EditEmailRoute>{
         EditEmailScreen(navController, koinInject())
      }

      composable<EditBirthdayRoute>{
         EditBirthdayScreen(navController, koinInject())
      }

      composable<EditSkillsRoute>{
         EditSkillsScreen(navController, koinInject())
      }
      composable<EditProfileRoute> {
         ProfileEditScreen(navController , koinInject())
      }
   }
}

@Serializable data object InternshipsGraph : BottomBarGraph
@Serializable data object InternshipsRoute : Route
@Serializable data class InternshipDetailRoute(val internshipId: String) : Route
fun NavGraphBuilder.internshipsGraph(navController: NavController) {
   navigation<InternshipsGraph>(startDestination = InternshipsRoute) {
      composable<InternshipsRoute> {
         InternshipsScreen(navController, koinInject())
      }

      composable<InternshipDetailRoute> {
         val internshipId = it.toRoute<InternshipDetailRoute>().internshipId
         InternshipDetailScreen(navController, koinInject(), internshipId)
      }
   }
}

@Serializable data object CourseGraph : BottomBarGraph
@Serializable data object CourseHomeRoute : Route
@Serializable data class CourseDetailRoute(val courseId : String) : Route
@Serializable data class CreatorDetailRoute(val creatorId : String) : Route
@Serializable data class CompanyDetailRoute(val company : String) : Route
@Serializable data class CourseVideoScreenRoute(val videoId : String) : Route
@Serializable data class LiveScreenRoute(val channelName: String) : Route
fun NavGraphBuilder.courseGraph(navController: NavController) {
   navigation<CourseGraph>(startDestination = CourseHomeRoute) {
      composable<CourseHomeRoute> {
         CourseHomeScreen(navController, koinInject())
      }
      composable<CourseDetailRoute> {
         val courseId = it.toRoute<CourseDetailRoute>().courseId
         CourseDetailScreen(navController, koinInject(), courseId, koinInject())
      }
      composable<CreatorDetailRoute> {
         val creatorId = it.toRoute<CreatorDetailRoute>().creatorId
         CreatorDetailScreen(navController, koinInject(), creatorId)
      }
      composable<CompanyDetailRoute> {
         val companyId = it.toRoute<CompanyDetailRoute>().company
         CompanyDetailScreen(navController, koinInject(), companyId)
      }
      composable<CourseVideoScreenRoute> {
         val videoId = it.toRoute<CourseVideoScreenRoute>().videoId
         CourseVideoScreen(navController, koinInject(), videoId)
      }
      composable<LiveScreenRoute> {
         val channelName = it.toRoute<LiveScreenRoute>().channelName
         LtrLayout {
            LiveScreen(navController , koinInject() , channelName)
         }
      }
   }
}

@Serializable data object LoadingGraph : Graph
@Serializable data object LoadingRoute

fun NavGraphBuilder.loadingGraph() {
   navigation<LoadingGraph>(startDestination = LoadingRoute) {
      composable<LoadingRoute> {
         Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
         }
      }
   }
}

@Serializable data object OnBoardingGraph : Graph
@Serializable data object OnBoardingRoute

fun NavGraphBuilder.onBoardingGraph() {
   navigation<OnBoardingGraph>(startDestination = OnBoardingRoute) {
      composable<OnBoardingRoute> {
         val repoSettingImpl = koinInject<RepoSettingImpl>()
         val scope = rememberCoroutineScope()
         LtrLayout {
            OnBoardingScreen(
               onComplete = { scope.launch { repoSettingImpl.setIsFirstTime(false) } }
            )
         }
      }
   }
}


@Serializable data object AuthGraph : Graph
@Serializable data object SigningRoute : Route
@Serializable data object SignUpRoute : Route
@Serializable data class OtpSignUpRoute(val email: String)
fun NavGraphBuilder.loginGraph(navController: NavController) {
   navigation<AuthGraph>(startDestination = SignUpRoute) {

      composable<SignUpRoute> {
         SignupScreen(
            navController = navController,
            repoAuth = koinInject()
         )
      }

      composable<SigningRoute> {
         SigningScreen(
            navController = navController,
            repoAuth = koinInject(),
         )
      }

      composable<OtpSignUpRoute> {
         val (email) = it.toRoute<OtpSignUpRoute>()
         OtpScreen(
            navController = navController,
            email = email,
            repoAuth = koinInject()
         )
      }
   }
}

@Serializable data object PaymentGraph : Graph
@Serializable data object PaymentRoute : Route
@Serializable data object AcquireCodeRoute : Route
@Serializable data object CodeActivationRoute : Route

fun NavGraphBuilder.paymentGraph(navController: NavController) {
   navigation<PaymentGraph>(startDestination = PaymentRoute) {
      composable<PaymentRoute> {
         LtrLayout {
            PaymentScreen(navController)
         }
      }

      composable<AcquireCodeRoute> {
         LtrLayout {
            AcquireCodeScreen(navController)
         }
      }

      composable<CodeActivationRoute> {
         CodeActivationScreen(navController, koinInject())
      }
   }
}

@Serializable data object AlumniGraph : BottomBarGraph
@Serializable data object AlumniRoute : Route
@Serializable data class AlumniDetailRoute(val id : String) : Route
@Serializable data class AlumniChatRoute(val conversationId : String) : Route
@Serializable data object AlumniChatsRoute : Route

fun NavGraphBuilder.alumniGraph(navController: NavController) {
   navigation<AlumniGraph>(startDestination = AlumniRoute) {
      composable<AlumniRoute> {
         AlumniListScreen(navController, koinInject())
      }
      composable<AlumniDetailRoute> {
         val alumniId = it.toRoute<AlumniDetailRoute>().id
         AlumniDetailScreen(navController, koinInject(), alumniId, koinInject())
      }
      composable<AlumniChatRoute> {
         val conversationId = it.toRoute<AlumniChatRoute>().conversationId
         AlumniChatScreen(navController, koinInject() , conversationId)
      }
      composable<AlumniChatsRoute> {
         AlumniChatsScreen(navController , koinInject())
      }
   }
}