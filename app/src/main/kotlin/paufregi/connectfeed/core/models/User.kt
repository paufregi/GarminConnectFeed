package paufregi.connectfeed.core.models

data class User(
    val id: Long,
    val name: String,
    val profileImageUrl: String,
) {
    companion object {
        val ANONYM = User(0, "Anonym", "")
    }
}
