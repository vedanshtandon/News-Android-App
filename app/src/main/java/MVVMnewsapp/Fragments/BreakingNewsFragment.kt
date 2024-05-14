package MVVMnewsapp.Fragments

import MVVMnewsapp.Activity.MainActivity
import MVVMnewsapp.Adapters.NewsAdapters
import MVVMnewsapp.Adapters.onNewsItemClickListener
import MVVMnewsapp.Models.Article
import MVVMnewsapp.Utils.Constants.Companion.QUERY_PAGE_SIZE
import MVVMnewsapp.Utils.Resource
import NewsViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsandroidmvvmapp.R


class BreakingNewsFragment:Fragment(R.layout.breaking_news_fragment),onNewsItemClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapters // Assume you have a custom adapter named NewsAdapter
    lateinit var viewModel: NewsViewModel
    lateinit var paginationProgressBar:ProgressBar
    var isError = false
    var isLoading = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.breaking_news_fragment,container,false)
        recyclerView=view.findViewById(R.id.recycler_view)
        adapter = NewsAdapters(emptyList(),this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(this@BreakingNewsFragment.scrollListener)
        paginationProgressBar=view.findViewById(R.id.paginationProgressBar)
        return view
    }


    override fun onViewCreated(view: View,savedInstanceState: Bundle?){

        viewModel=(activity as MainActivity).viewModel
        viewModel.getBreakingNews("in")
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()
                    response.data?.let {newsResponse->
                        Log.e("NewsResponse","Network Response : $newsResponse")
                        adapter.updateArticles(newsResponse.articles)
                    }
                }

                is Resource.Error->{
                    hideProgressBar()
                    response.message?.let {message->
                        Log.e("NewsResponse","Network Response : $message")
                    }
                }

                is Resource.Loading->{
                    showProgressBar()
                    Log.e("NewsResponse","Network Response : Loading")
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onNewsItemClicked(article: Article) {
        val bundle=Bundle().apply {
            putString("author",article.author)
            putString("content",article.content)
            putString("title",article.title)
            putString("description",article.description)
            putString("source", article.source?.name)
            putString("published",article.publishedAt)
            putString("url",article.url)
            putString("afterimage",article.urlToImage)
        }
        Navigation.findNavController(requireActivity(),R.id.newNavHostFragment).navigate(R.id.action_breakingNewsFragment_to_articleFragment,bundle)
    }


    // pagination
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoErrors = !isError
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate =
                isNoErrors && isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                        isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.getBreakingNews("in")
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }
}


