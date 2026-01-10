package ui.components

import BottomBarGraph
import HomeGraph
import InternshipsGraph
import ProfileGraph
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import domain.Lang
import getGraphInfo
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.components.environment.currentAppLang
import ui.components.layout.ColumnC
import ui.components.modifier.myBackground
import ui.platform.toPlatformDp

val bottomNavBarHeight = 64.dp
@Composable
fun BottomNavBar(
   modifier: Modifier = Modifier,
   navController: NavController,
   currentDestination: NavDestination?,
   destinations: List<BottomBarGraph> // e.g. listOf(HomeGraph, SubjectGraph, ProfileGraph)
) {

   Box(
      modifier = modifier
         .myBackground(shape = RectangleShape)
         .navigationBarsPadding()
         .height(bottomNavBarHeight)
         .fillMaxWidth()
      ,
   ){
      Row(
         modifier = Modifier.fillMaxWidth(),
         horizontalArrangement = Arrangement.SpaceAround,
         verticalAlignment = Alignment.CenterVertically
      ) {
         destinations.forEach { destination ->
            val isSelected = currentDestination?.hierarchy?.any { it.hasRoute(destination::class) } == true
            val info = destination.getGraphInfo()
            val icon = if (isSelected) info.selectedIcon else info.unselectedIcon
            val label = if (currentAppLang() == Lang.Krd) info.nameKrd else info.nameEng
            val textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            val alpha = if (isSelected) 1f else 0.5f

            ColumnC(
               spacing = 4.dp.toPlatformDp(4.dp),
               modifier = Modifier
                  .weight(1f)
                  .clickable {
                     navController.navigate(destination) {
                        popUpTo(navController.graph.findStartDestination().id) {
                           saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                     }
                  }
                  .padding(vertical = 8.dp)

            ) {
               Icon(
                  painter = painterResource(icon),
                  contentDescription = null,
                  tint = textColor,
                  modifier = Modifier.size(20.dp).alpha(alpha)
               )
               Text(
                  text = label,
                  style = MaterialTheme.typography.labelSmall,
                  color = textColor,
                  modifier = Modifier
                     .alpha(alpha)
               )
            }
         }
      }

   }
}


@Preview
@Composable
fun BottomNavBarPreview() {
   MyPreview(Lang.Eng) {
      val navController = rememberNavController()
      val entry by navController.currentBackStackEntryAsState()
      val currentDestination = entry?.destination
      Scaffold {
         Column(Modifier.padding(it)) {
            BottomNavBar(
               navController = navController,
               currentDestination = currentDestination,
               destinations = listOf(HomeGraph, InternshipsGraph, ProfileGraph)
            )
         }
      }
   }
}