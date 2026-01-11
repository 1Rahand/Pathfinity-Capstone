import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import data.MessageSyncer
import di.commonModule
import domain.Appearance
import domain.UserType
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import supabase.UserStudent
import ui.components.BottomNavBar
import ui.components.direction
import ui.components.environment.MyLocalAppLang
import ui.components.environment.MyLocalAppearance
import ui.components.environment.MyLocalDarkTheme
import ui.components.environment.MyLocalSettings
import ui.components.environment.MyLocalUserStudent
import ui.components.layout.LtrLayout
import ui.theme.AppTheme
import ui.theme.animationDuration

const val appVersion = "1.0.0"
const val agoraId = "fbfce5a1035144b6b86cdf5758fe164b"

@Composable
@Preview
fun App(platformModule : Module = module {  }) {
   KoinApplication(application = { modules(commonModule() + platformModule) }) {
      val vm = koinViewModel<RootViewModel>()
      val state by vm.rootViewState.collectAsStateWithLifecycle()
      val student by vm.student.collectAsStateWithLifecycle()
      val setting by vm.settingFlow.collectAsStateWithLifecycle()
      val isDark = if (setting.appearance == Appearance.SystemDefault) isSystemInDarkTheme() else setting.appearance == Appearance.Dark
      val messageSyncer : MessageSyncer = koinViewModel()
      AppTheme(darkTheme = isDark) {
         Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            val navController = rememberNavController()
            val entry by navController.currentBackStackEntryAsState()
            val currentDestination = entry?.destination
            val shouldShowBar =
               currentDestination?.hierarchy?.any { hierarchy -> shouldDisplayNavBar.any { hierarchy.hasRoute(it) } } == true
            val bottomBarHeightPadding = if (shouldShowBar) 56.dp else 0.dp

            CompositionLocalProvider(
               MyLocalAppLang provides setting.lang,
               LocalLayoutDirection provides setting.lang.direction(),
               MyLocalAppearance provides setting.appearance,
               MyLocalUserStudent provides (student ?: UserStudent.empty),
               MyLocalSettings provides setting,
               MyLocalDarkTheme provides isDark,
            ) {
               Scaffold(
                  bottomBar = {
                     AnimatedVisibility (
                        shouldShowBar,
                        enter = fadeIn(tween(MaterialTheme.animationDuration)) + slideInVertically(tween(MaterialTheme.animationDuration)) { it },
                        exit = fadeOut(tween(MaterialTheme.animationDuration)) + slideOutVertically(tween(MaterialTheme.animationDuration)) { it }
                     ) {
                        LtrLayout {
                           BottomNavBar(
                              modifier = Modifier ,
                              navController = navController,
                              currentDestination = currentDestination,
                              destinations = buildList {
                                 add(HomeGraph)
                                 add(AlumniGraph)
                                 add(CourseGraph)
                                 add(InternshipsGraph)
                                 add(ProfileGraph)
                              }
                           )
                        }
                     }
                  }
               ) {
                  NavHost(
                     navController = navController,
                     startDestination = state.graph,
                     modifier = Modifier.fillMaxSize(),
                  ) {
                     loginGraph(navController)
                     onBoardingGraph()
                     loadingGraph()
                     homeGraph(navController)
                     internshipsGraph(navController)
                     profileGraph(navController)
                     courseGraph(navController)
                     paymentGraph(navController)
                     alumniGraph(navController)
                  }
               }
            }
         }
      }
   }
}
