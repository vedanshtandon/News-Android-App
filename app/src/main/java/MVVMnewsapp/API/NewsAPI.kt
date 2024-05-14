package MVVMnewsapp.API

import MVVMnewsapp.Models.NewsResponse
import MVVMnewsapp.Utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {
    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country")
        countryCode:String="in",
        @Query("page")
        pageNumber:Int=1,
        @Query("apikey")
        apiKey:String=API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery:String,
        @Query("page")
        pageNumber:Int=1,
        @Query("apikey")
        apiKey:String=API_KEY
    ): Response<NewsResponse>
}


/*
    NEWS APT returns a JSON response
    Convert JSON Response to kotlin class
*/