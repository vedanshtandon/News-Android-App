import MVVMnewsapp.Entity.NewsArticleEntity
import MVVMnewsapp.Models.NewsResponse
import androidx.lifecycle.ViewModel
import MVVMnewsapp.Repository.NewsRepository
import MVVMnewsapp.Utils.Resource
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(val newsRepository: NewsRepository) : ViewModel() {



    val breakingNews:MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    private var breakingNewsPage=1
    var breakingNewsResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null

    // API functionality
    init {
        Log.e("NewsResponse","init block")
        getBreakingNews("in")
    }

    fun getBreakingNews(countryCode: String)=viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response=newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        breakingNews.postValue(handleBreakingNetworkResponse(response))
    }

    fun getSearchNews(searchQuery: String)=viewModelScope.launch {
        searchNewsResponse=null
        searchNews.postValue(Resource.Loading())
        val response=newsRepository.searchNews(searchQuery,searchNewsPage)
        breakingNews.postValue(handleBreakingNetworkResponse(response))
        searchNews.postValue(handleSearchNetworkResponse(response))
    }

    private fun handleBreakingNetworkResponse(response: Response<NewsResponse>): Resource<NewsResponse>? {
        if(response.isSuccessful){
            response.body()?.let{resultResponse->
                breakingNewsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse
                }
                else{
                    val oldArticles= breakingNewsResponse!!.articles
                    val newArticles=resultResponse.articles
                    oldArticles.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNetworkResponse(response:Response<NewsResponse>):Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let{resultResponse->
                searchNewsPage++
                if(searchNewsResponse==null){
                    searchNewsResponse=resultResponse
                }
                else{
                    val oldArticles= searchNewsResponse!!.articles
                    val newArticles=resultResponse.articles
                    oldArticles.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    // Room database functionality
    fun saveArticle(article: NewsArticleEntity)=viewModelScope.launch{
        newsRepository.insertArticle(article)
        Log.e("ButtonClicked","News Article has been added to database : ")
        Log.e("ButtonClicked","$article ")
    }
    fun getSavedNews()=newsRepository.getAllArticles()

    fun deleteArticle(article:NewsArticleEntity)=viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }



}