package ui.screens

import AlumniDetailRoute
import CompanyDetailRoute
import CourseDetailRoute
import CreatorDetailRoute
import EditProfileRoute
import InternshipDetailRoute
import PaymentGraph
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import data.Syncer
import database.MyDao
import domain.Platform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import pathfinity.composeapp.generated.resources.Res
import pathfinity.composeapp.generated.resources.icon_update
import platform.platform
import presentation.StringResources
import supabase.Course
import supabase.CourseCategory
import supabase.CourseReview
import supabase.CourseVideo
import supabase.CourseVideoProgress
import supabase.Internship
import supabase.LiveSession
import supabase.UserAlumni
import supabase.UserCompany
import supabase.UserContentCreator
import ui.components.bottomNavBarHeight
import ui.components.buttons.MyUpdateIconButton
import ui.components.environment.MyLocalUserStudent
import ui.components.environment.currentAppLang
import ui.components.modifier.myBackground
import ui.screens.course.CourseCard
import ui.screens.internship.InternshipCard
import ui.screens.mentorship.AlumniCard
import ui.screens.mentorship.PremiumUpgradePrompt
import ui.utility.getIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
   navController: NavController,
   myDao: MyDao
) {
   val user = MyLocalUserStudent.current
   val internships by myDao.getAllInternships().collectAsStateWithLifecycle(null)
   val courses by myDao.getAllCourses().collectAsStateWithLifecycle(emptyList())
   val creators by myDao.getAllUserContentCreators().map { it.associateBy { it.id } }.collectAsStateWithLifecycle(mapOf())
   val categories by myDao.getAllCourseCategories().map { it.associateBy { it.id } }.collectAsStateWithLifecycle(mapOf())
   val videos by myDao.getAllCourseVideos().map { list -> list.groupBy { it.courseId } }.collectAsStateWithLifecycle(mapOf())
   val lives by myDao.getAllLiveSessions().map { list -> list.groupBy { live -> live.courseId } }.collectAsStateWithLifecycle(mapOf())
   val videoProgresses by myDao.getAllCourseVideoProgress().map { it.associateBy { it.videoId } }.collectAsStateWithLifecycle(mapOf())
   val alumni by myDao.getAllUserAlumni().collectAsStateWithLifecycle(null)
   val reviews by myDao.getAllCourseReviews().map { it.groupBy { it.courseId } }.collectAsStateWithLifecycle(mapOf())

   val companies by myDao.getAllUserCompanies().collectAsStateWithLifecycle(null)
   val syncer: Syncer = koinViewModel()
   val isSyncing by syncer.isSyncing.collectAsStateWithLifecycle()
   val scope = rememberCoroutineScope()
   val currentUser = MyLocalUserStudent.current

   Scaffold(
      modifier = Modifier.fillMaxSize(),
   ) { paddingValues ->
      Column(
         modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(paddingValues)
            .padding(bottom = 32.dp)
            .padding(bottom = bottomNavBarHeight),
         verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
         Header(
            modifier = Modifier.padding(16.dp),
            onVerifyAccount = {
               navController.navigate(EditProfileRoute)
            },
            isLoading = isSyncing,
            onLoadClick = {
               scope.launch {
                  syncer.sync()
               }
            },
         )

         SectionHeader(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = StringResources.courses(currentAppLang())
         )
         CoursesList(
            courses = courses,
            categories = categories,
            creators = creators,
            onCourseClick = { navController.navigate(CourseDetailRoute(it.id)) },
            onCreatorChipClick = { navController.navigate(CreatorDetailRoute(it.id)) },
            videos = videos,
            videoProgresses = videoProgresses,
            reviews = reviews,
            lives = lives
         )

         SectionHeader(
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
            title = StringResources.availableInternships(currentAppLang())
         )
         InternshipsList(internships, companies, navController, isPremium = currentUser.premium)

         // New alumni section
         SectionHeader(
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 16.dp),
            title = "Connect with Alumni"
         )
         AlumniList(alumni, navController, user.premium)
      }
   }
}

@Composable
fun Header(
   modifier: Modifier = Modifier,
   isLoading: Boolean = false,
   onLoadClick: () -> Unit = {},
   onVerifyAccount: () -> Unit = {}
) {
   val profile = MyLocalUserStudent.current
   Row(
      modifier = modifier,
      horizontalArrangement = Arrangement.spacedBy(16.dp),
      verticalAlignment = Alignment.CenterVertically
   ) {
      val drawable = profile.gender.getIcon()
      Image(
         painter = painterResource(drawable),
         contentDescription = null,
         modifier = Modifier
            .myBackground(shape = CircleShape)
            .size(56.dp)
      )
      Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
         Text(
            text = profile.fullName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
         )

         if (profile.premium) {
            GradientBackgroundText(
               text = "PRO",
            )
         } else {
            Text(
               text = "Free",
               style = MaterialTheme.typography.labelSmall,
               fontWeight = FontWeight.Light,
               modifier = Modifier.alpha(0.50F)
            )
         }
         if (profile.email.isNullOrEmpty()) {
            Text(
               text = StringResources.verify(currentAppLang()),
               modifier = Modifier
                  .myBackground(
                     containerColor = Color.Transparent,
                     elevation = 0.dp,
                     onClick = onVerifyAccount,
                  )
                  .padding(vertical = 4.dp),
               color = MaterialTheme.colorScheme.primary,
               style = MaterialTheme.typography.bodySmall
            )
         }
      }

      Spacer(Modifier.weight(1f))

      // Update button removed

      MyUpdateIconButton(
         res = Res.drawable.icon_update,
         isLoading = isLoading,
         onClick = onLoadClick
      )
   }
}

@Composable
fun GradientBackgroundText(
   text: String,
   modifier: Modifier = Modifier,
   textStyle: TextStyle = MaterialTheme.typography.labelSmall,
   shape: Shape = RoundedCornerShape(4.dp),
   brush: Brush = Brush.linearGradient(
      colors = listOf(
         Color(0xFFFF3F3F), // Start color
         Color(0xFF063CFF)  // End color
      )
   )
) {

   Text(
      text = text,
      style = textStyle,
      color = Color.White,
      modifier = modifier
         .clip(shape)
         .drawBehind {
            drawRect(brush = brush)
         }
         .padding(horizontal = 8.dp, vertical = if (platform == Platform.Android) 0.dp else 2.dp)
   )
}

@Composable
private fun SectionHeader(
   modifier: Modifier = Modifier,
   title: String
) {
   Text(
      modifier = modifier,
      text = title,
      style = MaterialTheme.typography.titleLarge,
      fontWeight = FontWeight.Bold,
   )
}

@Composable
private fun CoursesList(
   courses: List<Course>?,
   creators: Map<String, UserContentCreator>,
   categories: Map<String, CourseCategory>,
   videos: Map<String, List<CourseVideo>>,
   videoProgresses: Map<String, CourseVideoProgress>,
   onCourseClick: (Course) -> Unit,
   onCreatorChipClick: (UserContentCreator) -> Unit = {},
   reviews: Map<String, List<CourseReview>>,
   lives: Map<String, List<LiveSession>>,
) {
   when {
      courses == null -> LoadingIndicator()
      courses.isEmpty() -> EmptyState("No courses available")
      else -> {
         BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val isDesktop = maxWidth > 450.dp

            if (isDesktop) {
               // Desktop layout with navigation buttons
               Box(modifier = Modifier.fillMaxWidth()) {
                  val lazyListState = rememberLazyListState()
                  val coroutineScope = rememberCoroutineScope()

                  // Show previous button if not at start
                  val showLeftButton = lazyListState.canScrollBackward
                  val showRightButton = lazyListState.canScrollForward

                  // Main content - LazyRow
                  LazyRow(
                     state = lazyListState,
                     horizontalArrangement = Arrangement.spacedBy(16.dp),
                     contentPadding = PaddingValues(horizontal = 16.dp),
                     modifier = Modifier.fillMaxWidth()
                  ) {
                     items(courses.size) { index ->
                        val course = courses[index]
                        val courseProgress = videos[course.id]?.let {
                           val totalVideos = it.size
                           val completedVideos = it.count { video ->
                              videoProgresses[video.id]?.completed == true
                           }
                           if (totalVideos > 0) {
                              (completedVideos.toDouble() / totalVideos.toDouble() * 100.0)
                           } else {
                              0.0
                           }
                        }
                        val reviewss = reviews[course.id]
                        val averagreRating = reviewss?.map { it.rating }?.average()

                        CourseCard(
                           course = course,
                           creator = creators[course.creatorId],
                           onClick = { onCourseClick(course) },
                           onCreatorChipClick = onCreatorChipClick,
                           courseCategory = categories[course.categoryId],
                           modifier = Modifier.width(400.dp),
                           courseProgress = courseProgress,
                           averageRating = averagreRating,
                           hasLive = lives[course.id]?.any { it.status == "active" } ?: false,
                        )
                     }
                  }

                  // Left navigation button
                  AnimatedVisibility(
                     visible = showLeftButton,
                     modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 8.dp)
                  ) {
                     NavigationButton(
                        icon = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Previous",
                        onClick = {
                           coroutineScope.launch {
                              // Scroll backwards by one item
                              lazyListState.animateScrollToItem(
                                 index = (lazyListState.firstVisibleItemIndex - 1).coerceAtLeast(0)
                              )
                           }
                        }
                     )
                  }

                  // Right navigation button
                  AnimatedVisibility(
                     visible = showRightButton,
                     modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                  ) {
                     NavigationButton(
                        icon = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next",
                        onClick = {
                           coroutineScope.launch {
                              lazyListState.animateScrollToItem(
                                 index = (lazyListState.firstVisibleItemIndex + 1).coerceAtMost(courses.lastIndex)
                              )
                           }
                        }
                     )
                  }
               }
            } else {
               // Mobile layout - use pager (unchanged)
               Column(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalAlignment = Alignment.CenterHorizontally
               ) {
                  val pagerState = rememberPagerState(pageCount = { courses.size })
                  var minHeight by remember { mutableStateOf(350.dp) }
                  val density = LocalDensity.current

                  HorizontalPager(
                     state = pagerState,
                     contentPadding = PaddingValues(horizontal = 16.dp),
                     pageSpacing = 16.dp,
                     modifier = Modifier.fillMaxWidth()
                  ) { page ->
                     val course = courses[page]
                     val courseProgress = videos[course.id]?.let {
                        val totalVideos = it.size
                        val completedVideos = it.count { video ->
                           videoProgresses[video.id]?.completed == true
                        }
                        if (totalVideos > 0) {
                           (completedVideos.toDouble() / totalVideos.toDouble() * 100.0)
                        } else {
                           0.0
                        }
                     }
                     CourseCard(
                        course = course,
                        creator = creators[course.creatorId],
                        onClick = { onCourseClick(course) },
                        onCreatorChipClick = onCreatorChipClick,
                        courseCategory = categories[course.categoryId],
                        modifier = Modifier
                           .onSizeChanged {
                              with(density) {
                                 val newHeight = it.height.toDp()
                                 if (newHeight > minHeight) {
                                    minHeight = newHeight
                                 }
                              }
                           }
                           .heightIn(min = minHeight),
                        courseProgress = courseProgress,
                        hasLive = lives[course.id]?.any { it.status == "active" } ?: false
                     )
                  }

                  Spacer(modifier = Modifier.height(16.dp))

                  // Page indicators (only shown on mobile)
                  Row(
                     horizontalArrangement = Arrangement.Center
                  ) {
                     repeat(courses.size.coerceAtMost(5)) { index ->
                        val isSelected = index == pagerState.currentPage % 5
                        val size = if (isSelected) 10.dp else 6.dp
                        Box(
                           modifier = Modifier
                              .padding(horizontal = 4.dp)
                              .size(size)
                              .clip(CircleShape)
                              .background(
                                 if (isSelected) MaterialTheme.colorScheme.primary
                                 else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                              )
                        )
                     }

                     if (courses.size > 5) {
                        Text(
                           text = "...",
                           modifier = Modifier.padding(horizontal = 4.dp),
                           style = MaterialTheme.typography.bodySmall,
                           color = MaterialTheme.colorScheme.primary
                        )
                     }
                  }
               }
            }
         }
      }
   }
}

@Composable
private fun NavigationButton(
   icon: ImageVector,
   contentDescription: String,
   onClick: () -> Unit
) {
   FilledIconButton(
      onClick = onClick,
      modifier = Modifier
         .size(48.dp)
         .shadow(
            elevation = 4.dp,
            shape = CircleShape,
            spotColor = MaterialTheme.colorScheme.primary
         ),
      colors = IconButtonDefaults.filledIconButtonColors(
         containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
         contentColor = MaterialTheme.colorScheme.primary
      )
   ) {
      Icon(
         imageVector = icon,
         contentDescription = contentDescription,
         modifier = Modifier.size(32.dp)
      )
   }
}

@Composable
private fun rememberPagerState(
   initialPage: Int = 0,
   pageCount: () -> Int
) = androidx.compose.foundation.pager.rememberPagerState(
   initialPage = initialPage,
   pageCount = pageCount
)

@Composable
private fun InternshipsList(
   internships: List<Internship>?,
   companies: List<UserCompany>?,
   navController: NavController,
   isPremium: Boolean = false
) {
   Box(modifier = Modifier.fillMaxWidth().height(280.dp)) {
      when {
         internships == null || companies == null -> LoadingIndicator()
         internships.isEmpty() -> EmptyState("No internships available")
         else -> {
            BoxWithConstraints(
               modifier = Modifier.fillMaxWidth()
                  // Apply blur for non-premium users
                  .then(if (!isPremium) Modifier.blur(radius = 8.dp) else Modifier)
            ) {
               val isDesktop = maxWidth > 450.dp
               val displayedInternships = internships.take(5)

               Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier.fillMaxWidth()
               ) {
                  if (isDesktop) {
                     // Desktop layout with navigation buttons
                     Box(modifier = Modifier.fillMaxWidth()) {
                        val listState = rememberLazyListState()
                        val scope = rememberCoroutineScope()

                        LazyRow(
                           state = listState,
                           contentPadding = PaddingValues(horizontal = 16.dp),
                           horizontalArrangement = Arrangement.spacedBy(16.dp),
                           modifier = Modifier
                              .fillMaxWidth()
                              .height(220.dp)
                        ) {
                           items(displayedInternships.size) { index ->
                              val internship = displayedInternships[index]
                              val company = companies.find { it.id == internship.companyId }

                              var minHeight by remember { mutableStateOf(0.dp) }
                              val density = LocalDensity.current

                              InternshipCard(
                                 modifier = Modifier
                                    .width(330.dp)
                                    .onSizeChanged {
                                       with(density) {
                                          minHeight = max(minHeight, it.height.toDp())
                                       }
                                    }
                                    .heightIn(min = minHeight),
                                 internship = internship,
                                 company = company,
                                 onClick = {
                                    if (isPremium) {
                                       navController.navigate(InternshipDetailRoute(internship.id))
                                    }
                                 },
                                 onCompanyClick = {
                                    if (isPremium) {
                                       navController.navigate(CompanyDetailRoute(it.id))
                                    }
                                 }
                              )
                           }
                        }

                        // Navigation buttons
                        androidx.compose.animation.AnimatedVisibility(
                           visible = listState.canScrollBackward,
                           modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                           NavigationButton(
                              icon = Icons.Default.KeyboardArrowLeft,
                              contentDescription = "Previous",
                              onClick = {
                                 scope.launch {
                                    listState.animateScrollToItem(
                                       index = (listState.firstVisibleItemIndex - 1).coerceAtLeast(0)
                                    )
                                 }
                              }
                           )
                        }

                        androidx.compose.animation.AnimatedVisibility(
                           visible = listState.canScrollForward,
                           modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                           NavigationButton(
                              icon = Icons.Default.KeyboardArrowRight,
                              contentDescription = "Next",
                              onClick = {
                                 scope.launch {
                                    listState.animateScrollToItem(
                                       index = (listState.firstVisibleItemIndex + 1).coerceAtMost(internships.lastIndex)
                                    )
                                 }
                              }
                           )
                        }
                     }
                  } else {
                     // Mobile layout - use pager
                     val pagerState = rememberPagerState { displayedInternships.size }

                     HorizontalPager(
                        modifier = Modifier.fillMaxWidth(),
                        state = pagerState,
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        pageSpacing = 16.dp,
                     ) { page ->
                        val internship = displayedInternships[page]
                        val company = companies.find { it.id == internship.companyId }

                        InternshipCard(
                           internship = internship,
                           company = company,
                           onClick = {
                              if (isPremium) {
                                 navController.navigate(InternshipDetailRoute(internship.id))
                              }
                           },
                           onCompanyClick = {
                              if (isPremium) {
                                 navController.navigate(CompanyDetailRoute(it.id))
                              }
                           }
                        )
                     }

                     Spacer(modifier = Modifier.height(16.dp))

                     // Pager indicator if needed
                     if (displayedInternships.size > 1) {
                        Row(
                           modifier = Modifier,
                           horizontalArrangement = Arrangement.Center
                        ) {
                           repeat(displayedInternships.size.coerceAtMost(5)) { index ->
                              val isSelected = index == pagerState.currentPage % 5
                              val size = if (isSelected) 10.dp else 6.dp
                              Box(
                                 modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(size)
                                    .clip(CircleShape)
                                    .background(
                                       if (isSelected) MaterialTheme.colorScheme.primary
                                       else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                                    )
                              )
                           }

                           if (displayedInternships.size > 5) {
                              Text(
                                 text = "...",
                                 modifier = Modifier.padding(horizontal = 4.dp),
                                 style = MaterialTheme.typography.bodySmall,
                                 color = MaterialTheme.colorScheme.primary
                              )
                           }
                        }
                     }
                  }

                  // "View All" button
                  if (internships.size > 5) {
                     TextButton(
                        onClick = {
                           if (isPremium) {
                              navController.navigate("internships")
                           }
                        },
                        modifier = Modifier
                           .fillMaxWidth()
                           .padding(horizontal = 16.dp)
                     ) {
                        Text("View All Internships")
                        Spacer(Modifier.width(4.dp))
                        Icon(
                           imageVector = Icons.Filled.ArrowForward,
                           contentDescription = null,
                           modifier = Modifier.size(16.dp),
                           tint = MaterialTheme.colorScheme.primary
                        )
                     }
                  }
               }
            }

            // Premium upgrade prompt overlay for non-premium users
            if (!isPremium) {
               Box(
                  modifier = Modifier
                     .fillMaxSize()
                     .padding(horizontal = 32.dp),
                  contentAlignment = Alignment.Center
               ) {
                  val currentUser = MyLocalUserStudent.current
                  PremiumUpgradePrompt(
                     modifier = Modifier.fillMaxWidth(),
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
   }
}

@Composable
private fun AlumniList(
   alumni: List<UserAlumni>?,
   navController: NavController,
   isPremium: Boolean
) {
   Box(modifier = Modifier.fillMaxWidth()) {
      when {
         alumni == null -> LoadingIndicator()
         alumni.isEmpty() -> EmptyState("No alumni available")
         else -> {
            BoxWithConstraints(
               modifier = Modifier.fillMaxWidth()
                  // Apply blur for non-premium users
                  .then(if (!isPremium) Modifier.blur(radius = 8.dp) else Modifier)
            ) {
               val isDesktop = maxWidth > 450.dp
               val displayedAlumni = alumni.take(5)

               Column(modifier = Modifier.fillMaxWidth()) {
                  if (isDesktop) {
                     // Desktop layout with navigation buttons
                     Box(modifier = Modifier.fillMaxWidth()) {
                        val lazyListState = rememberLazyListState()
                        val coroutineScope = rememberCoroutineScope()

                        // Show previous/next buttons based on scroll position
                        val showLeftButton = lazyListState.canScrollBackward
                        val showRightButton = lazyListState.canScrollForward

                        // Main content - LazyRow
                        LazyRow(
                           state = lazyListState,
                           contentPadding = PaddingValues(horizontal = 16.dp),
                           horizontalArrangement = Arrangement.spacedBy(16.dp),
                           modifier = Modifier.fillMaxWidth()
                        ) {
                           items(displayedAlumni.size) { index ->
                              val alumnus = displayedAlumni[index]
                              AlumniCard(
                                 alumnus = alumnus,
                                 onClick = {
                                    if (isPremium) navController.navigate(AlumniDetailRoute(alumnus.id))
                                 },
                                 modifier = Modifier.width(320.dp)
                              )
                           }
                        }

                        // Left navigation button
                        androidx.compose.animation.AnimatedVisibility(
                           visible = showLeftButton,
                           modifier = Modifier.align(Alignment.CenterStart)
                        ) {
                           NavigationButton(
                              icon = Icons.Default.KeyboardArrowLeft,
                              contentDescription = "Previous",
                              onClick = {
                                 coroutineScope.launch {
                                    lazyListState.animateScrollToItem(
                                       index = (lazyListState.firstVisibleItemIndex - 1).coerceAtLeast(0)
                                    )
                                 }
                              }
                           )
                        }

                        // Right navigation button
                        androidx.compose.animation.AnimatedVisibility(
                           visible = showRightButton,
                           modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                           NavigationButton(
                              icon = Icons.Default.KeyboardArrowRight,
                              contentDescription = "Next",
                              onClick = {
                                 coroutineScope.launch {
                                    lazyListState.animateScrollToItem(
                                       index = (lazyListState.firstVisibleItemIndex + 1).coerceAtMost(alumni.size - 1)
                                    )
                                 }
                              }
                           )
                        }
                     }
                  } else {
                     // Mobile layout - use pager
                     Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                     ) {
                        val pagerState = rememberPagerState(pageCount = { displayedAlumni.size })

                        HorizontalPager(
                           state = pagerState,
                           contentPadding = PaddingValues(horizontal = 16.dp),
                           pageSpacing = 16.dp,
                           modifier = Modifier.fillMaxWidth()
                        ) { page ->
                           val alumnus = displayedAlumni[page]
                           AlumniCard(
                              alumnus = alumnus,
                              onClick = {
                                 if (isPremium) navController.navigate(AlumniDetailRoute(alumnus.id))
                              },
                              modifier = Modifier.fillMaxWidth()
                           )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Page indicators
                        Row(
                           modifier = Modifier.padding(top = 8.dp),
                           horizontalArrangement = Arrangement.Center
                        ) {
                           repeat(displayedAlumni.size) { index ->
                              val isSelected = index == pagerState.currentPage
                              val size = if (isSelected) 10.dp else 6.dp
                              Box(
                                 modifier = Modifier
                                    .padding(2.dp)
                                    .size(size)
                                    .clip(CircleShape)
                                    .background(
                                       if (isSelected) MaterialTheme.colorScheme.primary
                                       else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                                    )
                              )
                           }
                        }
                     }
                  }

                  // "View All" button
                  if (alumni.size > 5) {
                     TextButton(
                        onClick = { navController.navigate("alumni") },
                        modifier = Modifier
                           .align(Alignment.End)
                           .padding(top = 8.dp, end = 16.dp)
                     ) {
                        Text("View All")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                     }
                  }
               }
            }

            // Premium upgrade prompt overlay for non-premium users
            if (!isPremium) {
               Box(
                  modifier = Modifier
                     .fillMaxSize()
                     .padding(horizontal = 32.dp),
                  contentAlignment = Alignment.Center
               ) {
                  val currentUser = MyLocalUserStudent.current
                  PremiumUpgradePrompt(
                     modifier = Modifier.fillMaxWidth(),
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
   }
}

@Composable
private fun LoadingIndicator() {
   Box(
      modifier = Modifier
         .fillMaxWidth()
         .height(100.dp),
      contentAlignment = Alignment.Center
   ) {
      CircularProgressIndicator()
   }
}

@Composable
private fun EmptyState(message: String) {
   Box(
      modifier = Modifier
         .fillMaxWidth()
         .height(120.dp),
      contentAlignment = Alignment.Center
   ) {
      Text(
         text = message,
         style = MaterialTheme.typography.bodyLarge,
         color = MaterialTheme.colorScheme.onSurfaceVariant
      )
   }
}