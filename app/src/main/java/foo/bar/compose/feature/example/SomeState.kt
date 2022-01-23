package foo.bar.compose.feature.example

const val MAX_NUMBER = 5

data class SomeState(
    val showControls: Boolean = true,
    val magicNumber: Int = 0
) {

    init {
        require(magicNumber <= MAX_NUMBER) {"magicNumber must be smaller than $MAX_NUMBER"}
        require(magicNumber >= 0) {"magicNumber must be bigger than 0"}
    }

    fun canIncrease(): Boolean = magicNumber != MAX_NUMBER
    fun canDecrease(): Boolean = magicNumber > 0
}
