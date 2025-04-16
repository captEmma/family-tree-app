package captemma.froggie.android.familytree.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import captemma.froggie.android.familytree.R
import captemma.froggie.android.familytree.model.Gender
import captemma.froggie.android.familytree.model.Person
import captemma.froggie.android.familytree.model.PersonRepository
import captemma.froggie.android.familytree.sqlite.PersistentDataHelper


@Composable
fun AddPersonScreen(
    navController: NavController, personRepository: PersonRepository
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.OTHER) }
    val parentIds = remember { mutableStateListOf<Int>() }
    var heir by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.safeContentPadding(),
        topBar = {
            Row() {
                IconButton(onClick = { navController.popBackStack() }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go Back"
                    )
                }
                Text(
                    modifier = Modifier.padding(12.dp),
                    text = stringResource(R.string.add_new_person),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row() {
                    OutlinedTextField(
                        shape = RoundedCornerShape(12.dp),
                        label = { Text(stringResource(R.string.first_name)) },
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
                        label = { Text(stringResource(R.string.last_name)) },
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
                Row(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.wrapContentHeight()
                    ) {
                        var genderListExanded by remember { mutableStateOf(false) }

                        OutlinedButton(
                            onClick = { genderListExanded = true },
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(
                                stringResource(
                                    R.string.gender_info,
                                    gender.toString().lowercase()
                                )
                            )
                        }

                        DropdownMenu(
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

                    Row(
                        modifier = Modifier.wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text("Heir")

                        Checkbox(
                            checked = heir,
                            onCheckedChange = { heir = it }
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row() {
                        var parentListExpanded by remember { mutableStateOf(false) }
                        val canAddParent by remember {
                            derivedStateOf {
                                parentIds.size < 2 && personRepository.getIds()
                                    .any { it !in parentIds }
                            }
                        }
                        OutlinedButton(
                            modifier = Modifier.wrapContentSize(),
                            shape = CircleShape,
                            onClick = { parentListExpanded = true },
                            enabled = canAddParent
                        ) {
                            Text("Add a parent")
                        }
                        DropdownMenu(
                            expanded = parentListExpanded,
                            onDismissRequest = { parentListExpanded = false }
                        ) {
                            personRepository.getPeople().forEach { person ->
                                if (person.id !in parentIds) {
                                    DropdownMenuItem(
                                        text = { Text(person.getFullName()) },
                                        onClick = {
                                            parentIds.add(person.id)
                                            parentListExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    if(parentIds.isNotEmpty())
                    {
                        Row(modifier = Modifier.padding(8.dp)) {
                            Text("Parents:")
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ){
                        parentIds.forEach{ parentId ->
                            val parent = personRepository.getPersonById(parentId)
                            if(parent != null){
                                ParentViewCard(parent) {
                                    parentIds.remove(parentId)
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    border = BorderStroke(1.dp, Color.Black),
                    enabled = firstName.isNotEmpty() && lastName.isNotEmpty(),
                    onClick = {
                        personRepository.addPerson(firstName, lastName, gender, mutableListOf(), heir)
                        navController.popBackStack()
                    },
                    content = { Text("Add") }
                )
            }
        }
    )
}

@Composable
fun ParentViewCard(parent: Person, onRemove: () -> Unit){
        TextButton(
            border = BorderStroke(1.dp, Color.Black),
            shape = RoundedCornerShape(12.dp),
            onClick = onRemove,
            content = { Text(parent.getFullName()) }
        )
}

@Composable
@Preview
fun PreviewAddPersonScreen() {
    val context = LocalContext.current
    val fakeController = rememberNavController()
    val fakePersistentDataHelper = remember { PersistentDataHelper(context) }
    val fakeRepository = remember { PersonRepository(fakePersistentDataHelper) }

    AddPersonScreen(fakeController, fakeRepository)
}