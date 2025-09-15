package paufregi.connectfeed.core.models

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
) {
    override fun toString(): String = "v$major.$minor.$patch"
    companion object {
        fun parse(input: String): Version? {
            val regex = Regex("^v?(\\d+)\\.(\\d+)\\.(\\d+)$")
            val match = regex.matchEntire(input.trim()) ?: return null
            val (major, minor, patch) = match.destructured
            return runCatching {
                Version(major.toInt(), minor.toInt(), patch.toInt())
            }.getOrNull()

        }
    }
}
