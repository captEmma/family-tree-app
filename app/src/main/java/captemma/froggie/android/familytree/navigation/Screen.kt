package captemma.froggie.android.familytree.navigation

sealed class Screen(val route: String) {
    object List: Screen("list")
    object AddPerson: Screen("add_person")
}