package MVVMnewsapp.Database

import MVVMnewsapp.DAO.NewsArticleDAO
import MVVMnewsapp.Entity.NewsArticleEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [NewsArticleEntity::class],
    version=1
)
abstract class NewsArticleDatabase : RoomDatabase() {
    abstract fun getNewsArticleDao():NewsArticleDAO

    companion object{
        @Volatile
        private var instance:NewsArticleDatabase?=null
        private val LOCK=Any()

        operator fun invoke(context: Context)= instance?: synchronized(LOCK){
            instance ?: createDatabase(context).also{
                instance=it
            }
        }

        private fun createDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext,
            NewsArticleDatabase::class.java,
            "saved_news_article.db"
        ).fallbackToDestructiveMigration().build()
    }
}