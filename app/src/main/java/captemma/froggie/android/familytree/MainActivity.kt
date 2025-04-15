package captemma.froggie.android.familytree

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import captemma.froggie.android.familytree.model.Gender
import captemma.froggie.android.familytree.model.PersonRepository
import captemma.froggie.android.familytree.ui.screen.ListScreen
import captemma.froggie.android.familytree.ui.theme.FamilyTreeTheme

class MainActivity : ComponentActivity() {

    private val repository = PersonRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        val dawn = repository.addPerson("Dawn", "Rutherford", Gender.FEMALE, heir = true)
//        val frost = repository.addPerson("Frost", "Thomas")
//        val river = repository.addPerson(
//            "River",
//            "Rutherford",
//            Gender.MALE,
//            mutableListOf(dawn.id, frost.id),
//            true
//        )
//        val kyle = repository.addPerson("Kyle", "Kyleson", Gender.MALE)
//        val katie = repository.addPerson(
//            "Katie",
//            "Rutherford",
//            Gender.FEMALE,
//            mutableListOf(dawn.id)
//        )
//        val blake = repository.addPerson(
//            "Blake",
//            "Rutherford",
//            Gender.OTHER,
//            mutableListOf(dawn.id, kyle.id)
//        )
//        val kai = repository.addPerson("Kai", "Rutherford", Gender.MALE)
//        val coral = repository.addPerson("Coral", "Rutherford", Gender.FEMALE, heir = true)
//
//        repository.addParentToPerson(katie.id, dawn.id)
//        repository.addParentToPerson(katie.id, kyle.id)
//        repository.addParentToPerson(katie.id, frost.id)
//
//        repository.addChildrenToPerson(river.id, listOf(kai.id, coral.id))
//
//        Log.d("Debug", "Dawn's children: ${repository.getPeopleByIds(repository.findChildrenOf(dawn.id))}")
//        Log.d("Debug", "River's children: ${repository.getPeopleByIds(repository.findChildrenOf(river.id))}")
//
//        repository.removePerson(kyle.id)
//        Log.d("Debug", katie.toString())

        setContent {
            FamilyTreeTheme {
                ListScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FamilyTreeTheme {
        ListScreen()
    }
}