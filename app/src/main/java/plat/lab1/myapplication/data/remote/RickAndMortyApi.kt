package plat.lab1.myapplication.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface RickAndMortyApi {

    @GET("api/character")
    suspend fun getCharacters(): ApiResponse<CharacterDto>

    @GET("api/location")
    suspend fun getLocations(): ApiResponse<LocationDto>

    @GET
    suspend fun getCharactersPage(
        @Url url: String
    ): ApiResponse<CharacterDto>

    @GET
    suspend fun getLocationsPage(
        @Url url: String
    ): ApiResponse<LocationDto>
}