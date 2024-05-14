package MVVMnewsapp.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "SavedNewsArticles",
)
data class NewsArticleEntity(
    @PrimaryKey(autoGenerate = true) val uid:Int=0,
    @ColumnInfo(name="Author") val author:String?,
    @ColumnInfo(name="Content") val content:String?,
    @ColumnInfo(name="Description") val description:String?,
    @ColumnInfo(name="Published_At") val published:String?,
    @ColumnInfo(name="Source") val source:String?,
    @ColumnInfo(name="Title") val title:String?,
    @ColumnInfo(name="Url") val url:String?,
    @ColumnInfo(name="Url_to_image") val url_to_image:String?
)

