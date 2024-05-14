package MVVMnewsapp.Adapters

import MVVMnewsapp.Models.Article

interface onNewsItemClickListener {
    fun onNewsItemClicked(article : Article)
}