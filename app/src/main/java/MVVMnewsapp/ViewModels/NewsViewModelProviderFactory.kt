package MVVMnewsapp.ViewModels

import MVVMnewsapp.Repository.NewsRepository
import NewsViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel

class NewsViewModelProviderFactory(val newsRepository: NewsRepository)
    :ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(newsRepository) as T
    }
}