package ui.screens.course

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import database.MyDao
import kotlinx.coroutines.flow.map
import ui.components.MyTopAppBar
import ui.platform.LiveView

@Composable
fun LiveScreen(
   navController: NavController,
   dao : MyDao,
   channelName : String
){

   val snackbarHostState = remember { SnackbarHostState() }
   val live by dao.getAllLiveSessions().map { it.filter { it.channelName == channelName }.firstOrNull() }.collectAsStateWithLifecycle(null)
   Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
         MyTopAppBar(
            title = "Live Session",
            onBackClick = {
               navController.navigateUp()
            }
         )
      },
      snackbarHost = {
         SnackbarHost(snackbarHostState)
      }
   ) { paddingValues ->
      live?.let {
         LiveView(
            modifier = Modifier
               .padding(paddingValues)
               .fillMaxSize(),
            channelName = channelName,
            uid = it.creatorId
         )

      }
   }
}