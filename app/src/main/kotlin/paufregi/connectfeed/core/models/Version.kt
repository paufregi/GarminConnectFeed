package paufregi.connectfeed.core.models

data class Version(
    val major: Int,
    val minor: Int,
    val patch: Int,
) {
    override fun toString(): String = "v$major.$minor.$patch"

    fun same(other: Version): Boolean {
        return this.major == other.major &&
            this.minor == other.minor &&
            this.patch == other.patch
    }

    fun isLowerThan(other: Version): Boolean {
        if (this.major < other.major) return true
        if (this.major > other.major) return false
        if (this.minor < other.minor) return true
        if (this.minor > other.minor) return false
        if (this.patch < other.patch) return true
        return false
    }

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
