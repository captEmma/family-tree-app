package captemma.froggie.android.familytree.ui.screen

import android.icu.text.CaseMap.Title
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGesturesPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import captemma.froggie.android.familytree.R
import captemma.froggie.android.familytree.model.Gender
import captemma.froggie.android.familytree.model.PersonRepository

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
        modifier = Modifier.safeGesturesPadding(),
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
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Row(modifier = Modifier.padding(innerPadding)) {
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
                        .padding(start = 8.dp, end = 8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box() {
                        var genderListExanded by remember { mutableStateOf(false) }

                        OutlinedButton(
                            modifier = Modifier.padding(innerPadding),
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

                    Row(
                        modifier = Modifier.wrapContentHeight(),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        Text(
                            modifier = Modifier.padding(innerPadding),
                            text = "Heir"
                        )

                        Checkbox(
                            checked = heir,
                            onCheckedChange = { heir = it }
                        )
                    }
                }

                Box(
                    modifier = Modifier.padding(innerPadding).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    var parentListExpanded by remember { mutableStateOf(false) }
                    val canAddParent: Boolean = parentIds.size<2 && personRepository.getIds().any {it !in parentIds}
                    OutlinedButton(
                        modifier = Modifier.padding(innerPadding).wrapContentSize(),
                        shape = CircleShape,
                        onClick = {parentListExpanded = true},
                        enabled = canAddParent
                    ) {
                        Text("Add a parent")
                    }
                    DropdownMenu(
                        modifier = Modifier.padding(innerPadding),
                        expanded = parentListExpanded,
                        onDismissRequest = {parentListExpanded = false}
                    ) {
                        personRepository.getPeople().forEach{ person ->
                            if(person.id !in parentIds) {
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
@Preview
fun PreviewAddPersonScreen() {
    val fakeController = rememberNavController()
    val repository = PersonRepository()
    AddPersonScreen(fakeController, repository)
}