package ui.screens.mentorship

import AlumniChatsRoute
import AlumniDetailRoute
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import database.MyDao
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.icon_message_fill
import supabase.UserAlumni
import ui.components.bottomNavBarHeight
import ui.components.buttons.MyIconButton
import ui.components.environment.currentAppLang
import ui.components.layout.RowC
import ui.components.modifier.myBackground
import ui.components.modifier.noIndicationClickable
import EditProfileRoute
import PaymentGraph
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.ui.draw.blur
import androidx.compose.ui.text.style.TextAlign
import ui.components.environment.MyLocalUserStudent

@Composable
fun AlumniListScreen(
   navController: NavController,
   dao: MyDao
) {
   val focusManager = LocalFocusManager.current
   val alumni by dao.getAllUserAlumni().collectAsStateWithLifecycle(null)
   val appLang = currentAppLang()
   val currentUser = MyLocalUserStudent.current
   val isPremium = currentUser.premium

   // Search query state
   var searchQuery by remember { mutableStateOf("") }

   // Filtered alumni based on search
   val filteredAlumni = remember(alumni, searchQuery) {
      alumni?.filter { alumnus ->
         searchQuery.isEmpty() ||
                 alumnus.firstName.contains(searchQuery, ignoreCase = true) ||
                 alumnus.lastName.contains(searchQuery, ignoreCase = true) ||
                 alumnus.university?.contains(searchQuery, ignoreCase = true) == true
      } ?: emptyList()
   }

   Scaffold(
      modifier = Modifier
         .noIndicationClickable { focusManager.clearFocus() }
         .fillMaxSize(),
   ) { paddingValues ->
      Box(modifier = Modifier.fillMaxSize()) {
         Column(
            modifier = Modifier
               .fillMaxSize()
               .padding(paddingValues)
               .padding(bottom = bottomNavBarHeight)
               // Apply blur for non-premium users
               .then(if (!isPremium) Modifier.blur(radius = 8.dp) else Modifier)
         ) {
            // Search bar
            RowC(Modifier.padding(16.dp)) {
               OutlinedTextField(
                  value = searchQuery,
                  onValueChange = { if (isPremium) searchQuery = it },
                  modifier = Modifier.weight(1f),
                  placeholder = { Text("Search alumni...") },
                  leadingIcon = { Icon(Icons.Default.Search, "Search") },
                  trailingIcon = {
                     if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { if (isPremium) searchQuery = "" }) {
                           Icon(Icons.Default.Clear, "Clear")
                        }
                     }
                  },
                  singleLine = true,
                  shape = RoundedCornerShape(12.dp)
               )

               MyIconButton(
                  res = Res.drawable.icon_message_fill,
                  onClick = {
                     if (isPremium) navController.navigate(AlumniChatsRoute)
                  }
               )
            }

            if (alumni == null) {
               // Loading state
               Box(
                  modifier = Modifier.fillMaxSize(),
                  contentAlignment = Alignment.Center
               ) {
                  CircularProgressIndicator()
               }
            } else if (filteredAlumni.isEmpty()) {
               // Empty state
               Box(
                  modifier = Modifier.fillMaxSize(),
                  contentAlignment = Alignment.Center
               ) {
                  Column(
                     horizontalAlignment = Alignment.CenterHorizontally,
                     verticalArrangement = Arrangement.spacedBy(8.dp)
                  ) {
                     Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                     )
                     Text(
                        text = "No alumni found",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                     )
                  }
               }
            } else {
               // Alumni grid
               LazyVerticalGrid(
                  columns = GridCells.Adaptive(minSize = 200.dp),
                  modifier = Modifier.fillMaxSize(),
                  contentPadding = PaddingValues(16.dp),
                  verticalArrangement = Arrangement.spacedBy(16.dp),
                  horizontalArrangement = Arrangement.spacedBy(16.dp)
               ) {
                  items(filteredAlumni) { alumnus ->
                     AlumniCard(
                        alumnus = alumnus,
                        onClick = {
                           if (isPremium) navController.navigate(AlumniDetailRoute(alumnus.id))
                        }
                     )
                  }
               }
            }
         }

         // Premium upgrade prompt overlay for non-premium users
         if (!isPremium) {
            PremiumUpgradePrompt(
               modifier = Modifier
                  .fillMaxSize()
                  .padding(paddingValues)
                  .padding(32.dp),
               onUpgradeClick = {
                  // Navigate to premium upgrade screen
                  if (currentUser.email.isNullOrEmpty()) {
                     navController.navigate(EditProfileRoute)
                  } else {
                     navController.navigate(PaymentGraph)
                  }
               }
            )
         }
      }
   }
}

@Composable
fun PremiumUpgradePrompt(
   modifier: Modifier = Modifier,
   onUpgradeClick: () -> Unit
) {
   Column(
      modifier = modifier,
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
   ) {
      Icon(
         imageVector = Icons.Outlined.LocationOn,
         contentDescription = null,
         modifier = Modifier.size(64.dp),
         tint = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
         text = "Premium Feature",
         style = MaterialTheme.typography.headlineMedium,
         fontWeight = FontWeight.Bold,
         textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
         text = "Access to alumni connections is available exclusively for premium members.",
         style = MaterialTheme.typography.bodyLarge,
         textAlign = TextAlign.Center,
         color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(24.dp))

      Button(
         onClick = onUpgradeClick,
         modifier = Modifier.fillMaxWidth(0.8f)
      ) {
         Text(
            text = "Upgrade to Premium",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 4.dp)
         )
      }
   }
}

@Composable
fun AlumniCard(
   alumnus: UserAlumni,
   onClick: () -> Unit,
   modifier: Modifier = Modifier
) {
   Column(
      modifier = modifier
         .fillMaxWidth()
         .heightIn(min = 240.dp)
         .clip(RoundedCornerShape(16.dp))
         .myBackground(onClick = onClick),
      verticalArrangement = Arrangement.spacedBy(12.dp)
   ) {
      // Profile image area with aspect ratio and improved overlay gradient
      Box(
         modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f/9f)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
      ) {
         // Profile image or placeholder
         if (alumnus.profilePictureUrl != null) {
            AsyncImage(
               model = alumnus.profilePictureUrl,
               contentDescription = "Profile picture",
               modifier = Modifier.fillMaxSize(),
               contentScale = ContentScale.Crop
            )
         } else {
            Box(
               modifier = Modifier
                  .fillMaxSize()
                  .background(
                     brush = Brush.linearGradient(
                        colors = listOf(
                           MaterialTheme.colorScheme.primaryContainer,
                           MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                        )
                     )
                  ),
               contentAlignment = Alignment.Center
            ) {
               Icon(
                  imageVector = Icons.Outlined.Person,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onPrimaryContainer,
                  modifier = Modifier.size(64.dp)
               )
            }
         }

         // Improved gradient overlay for better text readability
         Box(
            modifier = Modifier
               .fillMaxSize()
               .background(
                  brush = Brush.verticalGradient(
                     colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.7f)
                     ),
                     startY = 0f,
                     endY = Float.POSITIVE_INFINITY
                  )
               )
         )

         // Name with improved visibility
         Text(
            text = "${alumnus.firstName} ${alumnus.lastName}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
               .align(Alignment.BottomStart)
               .padding(16.dp)
         )


      }

      // Content section with improved info rows
      Column(
         modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
         verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
         // University info with enhanced icon presentation
         alumnus.university?.let {
            Row(
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               Surface(
                  shape = CircleShape,
                  color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                  modifier = Modifier.size(28.dp)
               ) {
                  Icon(
                     imageVector = Icons.Default.School,
                     contentDescription = null,
                     modifier = Modifier.padding(5.dp),
                     tint = MaterialTheme.colorScheme.primary
                  )
               }
               Text(
                  text = it,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
               )
            }
         }

         // Graduation year with consistent styling
         alumnus.graduationYear?.let {
            Row(
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
               Surface(
                  shape = CircleShape,
                  color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                  modifier = Modifier.size(28.dp)
               ) {
                  Icon(
                     imageVector = Icons.Default.CalendarToday,
                     contentDescription = null,
                     modifier = Modifier.padding(5.dp),
                     tint = MaterialTheme.colorScheme.primary
                  )
               }
               Text(
                  text = "Class of $it",
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
               )
            }
         }

         // Experience preview with better visual treatment
         alumnus.experience?.let {
            if (it.isNotEmpty()) {
               Row(
                  verticalAlignment = Alignment.Top,
                  horizontalArrangement = Arrangement.spacedBy(8.dp)
               ) {
                  Surface(
                     shape = CircleShape,
                     color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                     modifier = Modifier.size(28.dp)
                  ) {
                     Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        modifier = Modifier.padding(5.dp),
                        tint = MaterialTheme.colorScheme.primary
                     )
                  }
                  Text(
                     text = it.take(100) + if (it.length > 100) "..." else "",
                     style = MaterialTheme.typography.bodySmall,
                     color = MaterialTheme.colorScheme.onSurfaceVariant,
                     maxLines = 2,
                     overflow = TextOverflow.Ellipsis
                  )
               }
            }
         }
      }
   }
}