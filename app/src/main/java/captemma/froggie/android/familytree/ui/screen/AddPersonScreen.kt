package captemma.froggie.android.familytree.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import captemma.froggie.android.familytree.model.Gender
import captemma.froggie.android.familytree.model.PersonRepository

@Composable
fun AddPersonScreen(
    personRepository: PersonRepository, navController: NavController? = null
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.OTHER) }
    var heir by remember { mutableStateOf(false) }

    Scaffold() { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Spacer(modifier = Modifier.padding(12.dp))
            Row(modifier = Modifier.padding(innerPadding)) {
                OutlinedTextField(
                    shape = RoundedCornerShape(12.dp),
                    label = {Text("First Name")},
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    value = firstName,
                    onValueChange = {
                        firstName = it
                    }
                )
                OutlinedTextField(
                    shape = RoundedCornerShape(12.dp),
                    label = {Text("Last Name")},
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    value = lastName,
                    onValueChange = {
                        lastName = it
                    }
                )
            }

            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                var genderListExanded by remember { mutableStateOf(false) }

                OutlinedButton(
                    modifier = Modifier.padding(start = 8.dp, top = 12.dp, bottom = 8.dp),
                    onClick = { genderListExanded = true },
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Gender: ${gender.toString()}")
                }

                DropdownMenu(
                    modifier = Modifier.padding(innerPadding),
                    expanded = genderListExanded,
                    onDismissRequest = { genderListExanded = false }
                ) {
                    Gender.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.name.lowercase()) },
                            onClick = {
                                gender = option
                                genderListExanded = false
                            }
                        )
                    }
                }
            }

        }
    }
}

@Composable
@Preview
fun PreviewAddPersonScreen(){
    val repository = PersonRepository()
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
    AddPersonScreen(personRepository = repository)
}