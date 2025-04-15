package captemma.froggie.android.familytree.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import captemma.froggie.android.familytree.R
import captemma.froggie.android.familytree.model.Gender
import captemma.froggie.android.familytree.model.Person

@Composable
fun PersonCard(person: Person) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Image(
            modifier = Modifier.size(64.dp),
            painter = painterResource(id = if (person.gender==Gender.MALE) R.drawable.ic_person_male_foreground else if (person.gender==Gender.FEMALE) R.drawable.ic_person_female_foreground else R.drawable.ic_person_other_foreground),
            contentDescription = "profile_picture"
        )
        Spacer(Modifier.size(8.dp))
        Column(Modifier.weight(1f)){
            Text(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                text = person.getFullName(),
                maxLines = 1
            )
            Spacer(Modifier.size(2.dp))
            Text(
                text = if (person.isHeir) stringResource(R.string.heir) else "",
                maxLines = 1
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewPersonCard() {
    PersonCard(Person(2, "Default", "Human", Gender.FEMALE, mutableListOf(1), true))
}