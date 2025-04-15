package captemma.froggie.android.familytree.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import captemma.froggie.android.familytree.model.PersonRepository
import captemma.froggie.android.familytree.ui.screen.AddPersonScreen
import captemma.froggie.android.familytree.ui.screen.ListScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(), personRepository: PersonRepository
) {
    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ){
        composable(Screen.List.route){
            ListScreen(navController)
        }
        composable(Screen.AddPerson.route) {
            AddPersonScreen(personRepository, navController)
        }
    }
}