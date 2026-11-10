package plat.lab1.myapplication.data.remote

data class ApiResponse<T>(
    val info: ApiInfo,
    val results: List<T>
)

data class ApiInfo(
    val next: String?
)