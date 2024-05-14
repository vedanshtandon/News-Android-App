package MVVMnewsapp.Models

import MVVMnewsapp.Models.Article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)