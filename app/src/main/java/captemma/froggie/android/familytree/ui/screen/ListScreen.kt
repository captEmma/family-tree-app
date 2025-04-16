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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import captemma.froggie.android.familytree.model.Gender
import captemma.froggie.android.familytree.model.PersonRepository
import captemma.froggie.android.familytree.navigation.Screen
import captemma.froggie.android.familytree.ui.view.PersonCard

@Composable
fun ListScreen(navController: NavController, personRepository: PersonRepository) {
    val context = LocalContext.current

    val people = personRepository.getPeople()

    Scaffold(
        modifier = Modifier.safeDrawingPadding(),

        floatingActionButton = {
            LargeFloatingActionButton(
                shape = CircleShape,
                onClick = {
                    navController.navigate(Screen.AddPerson.route)
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
    val fakeRepository = PersonRepository()
    val fakeController = rememberNavController()

    if(fakeRepository.getPeople().isEmpty()){
        val dawn = fakeRepository.addPerson("Dawn", "Rutherford", Gender.FEMALE, heir = true)
        val frost = fakeRepository.addPerson("Frost", "Thomas", Gender.MALE)
        val river = fakeRepository.addPerson(
            "River",
            "Rutherford",
            Gender.MALE,
            mutableListOf(dawn.id, frost.id),
            true
        )
        val kyle = fakeRepository.addPerson("Kyle", "Kyleson", Gender.MALE)
        val katie = fakeRepository.addPerson(
            "Katie",
            "Rutherford",
            Gender.FEMALE,
            mutableListOf(dawn.id)
        )
        val blake = fakeRepository.addPerson(
            "Blake",
            "Rutherford",
            Gender.FEMALE,
            mutableListOf(dawn.id, kyle.id)
        )
        val kai = fakeRepository.addPerson("Kai", "Rutherford", Gender.MALE)
        val coral = fakeRepository.addPerson("Coral", "Rutherford", Gender.FEMALE, heir = true)

        fakeRepository.addParentToPerson(katie.id, dawn.id)
        fakeRepository.addParentToPerson(katie.id, kyle.id)
        fakeRepository.addParentToPerson(katie.id, frost.id)

        fakeRepository.addChildrenToPerson(river.id, listOf(kai.id, coral.id))
    }

    ListScreen(fakeController, fakeRepository)
}