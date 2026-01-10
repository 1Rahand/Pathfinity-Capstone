sealed class RootViewState(val graph: Graph) {
   data object Loading : RootViewState(LoadingGraph)
   data object OnBoarding : RootViewState(OnBoardingGraph)
   data object AppContent : RootViewState(HomeGraph)
   data object Login : RootViewState(AuthGraph)
}