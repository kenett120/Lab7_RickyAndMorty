package plat.lab1.myapplication.ui.state

data class ScreenState<T>(
    val isLoading: Boolean = true,
    val data: T? = null,
    val hasError: Boolean = false
)