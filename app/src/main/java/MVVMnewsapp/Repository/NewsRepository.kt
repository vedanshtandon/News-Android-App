package MVVMnewsapp.Repository

import MVVMnewsapp.API.RetrofitInstance
import MVVMnewsapp.Database.NewsArticleDatabase
import MVVMnewsapp.Entity.NewsArticleEntity
import MVVMnewsapp.Models.Article
import java.util.Locale.IsoCountryCode

class NewsRepository(val db:NewsArticleDatabase) {

    // Room Database Function
    suspend fun insertArticle(article:NewsArticleEntity) = db.getNewsArticleDao().insertArticle(article)
    fun getAllArticles()=db.getNewsArticleDao().getAllArticles()
    suspend fun deleteArticle(article:NewsArticleEntity)=db.getNewsArticleDao().delete(article)

    // Retrofit Functions
    suspend fun getBreakingNews(countryCode:String,pageNumber:Int)=
        RetrofitInstance.api.getBreakingNews(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)
}