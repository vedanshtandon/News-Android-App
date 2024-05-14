package MVVMnewsapp.DAO

import MVVMnewsapp.Entity.NewsArticleEntity
import MVVMnewsapp.Models.Article
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsArticleDAO {

    @Query("SELECT * FROM SavedNewsArticles")
    fun getAllArticles() : LiveData<List<NewsArticleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article :NewsArticleEntity) : Long

    @Delete
    suspend fun delete(article:NewsArticleEntity)
}