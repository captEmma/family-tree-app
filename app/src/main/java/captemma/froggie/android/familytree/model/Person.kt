package captemma.froggie.android.familytree.model

data class Person(
    val id: Int,
    var firstName: String,
    var lastName: String,
    var parentIDs: MutableList<Int> = mutableListOf(),
    var isHeir: Boolean = false
)