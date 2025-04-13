package captemma.froggie.android.familytree.model

class PersonRepository {
    private val people = mutableListOf<Person>()
    private var nextID = 1

    fun addPerson(firstName: String, lastName: String,
                  parents: MutableList<Int> = mutableListOf(),
                  heir: Boolean=false): Person{
        val person = Person(
            id=nextID,
            firstName=firstName,
            lastName=lastName,
            parentIDs=parents,
            isHeir=heir
        )
        people.add(person)
        nextID++
        return person
    }

    fun getPersonById(id: Int): Person?{
        return people.find { person -> id == person.id }
    }


}