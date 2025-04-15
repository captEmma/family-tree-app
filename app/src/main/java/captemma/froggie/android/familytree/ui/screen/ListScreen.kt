package captemma.froggie.android.familytree.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import captemma.froggie.android.familytree.model.Gender
import captemma.froggie.android.familytree.model.PersonRepository
import captemma.froggie.android.familytree.navigation.Screen
import captemma.froggie.android.familytree.ui.view.PersonCard

@Composable
fun ListScreen(navController: NavController? = null) {
    val context = LocalContext.current
    val repository = remember { PersonRepository() }

    LaunchedEffect(Unit) {
        if(repository.getPeople().isEmpty()){
            val dawn = repository.addPerson("Dawn", "Rutherford", Gender.FEMALE, heir = true)
            val frost = repository.addPerson("Frost", "Thomas", Gender.MALE)
            val river = repository.addPerson(
                "River",
                "Rutherford",
                Gender.MALE,
                mutableListOf(dawn.id, frost.id),
                true
            )
            val kyle = repository.addPerson("Kyle", "Kyleson", Gender.MALE)
            val katie = repository.addPerson(
                "Katie",
                "Rutherford",
                Gender.FEMALE,
                mutableListOf(dawn.id)
            )
            val blake = repository.addPerson(
                "Blake",
                "Rutherford",
                Gender.FEMALE,
                mutableListOf(dawn.id, kyle.id)
            )
            val kai = repository.addPerson("Kai", "Rutherford", Gender.MALE)
            val coral = repository.addPerson("Coral", "Rutherford", Gender.FEMALE, heir = true)

            repository.addParentToPerson(katie.id, dawn.id)
            repository.addParentToPerson(katie.id, kyle.id)
            repository.addParentToPerson(katie.id, frost.id)

            repository.addChildrenToPerson(river.id, listOf(kai.id, coral.id))
        }
    }

    val people = repository.getPeople()

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),

        floatingActionButton = {
            LargeFloatingActionButton(
                shape = CircleShape,
                onClick = {
                    navController?.navigate(Screen.AddPerson.route)
                }
            ) { }
        },

        content = { innerPadding ->
            Box(Modifier.fillMaxSize().padding(innerPadding)){
                if(people.isNotEmpty()){
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(people.size){
                            PersonCard(people[it])
                        }
                    }
                }
            }

        }
    )
}

@Composable
@Preview
fun PreviewListScreen(){
    ListScreen()
}