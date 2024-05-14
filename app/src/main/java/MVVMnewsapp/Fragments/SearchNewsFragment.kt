package MVVMnewsapp.Fragments

import MVVMnewsapp.Activity.MainActivity
import MVVMnewsapp.Adapters.NewsAdapters
import MVVMnewsapp.Adapters.onNewsItemClickListener
import MVVMnewsapp.Models.Article
import MVVMnewsapp.Utils.Constants
import MVVMnewsapp.Utils.Resource
import NewsViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsandroidmvvmapp.R

class SearchNewsFragment:Fragment(R.layout.search_news_fragment),onNewsItemClickListener {

    lateinit var viewModel: NewsViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapters
    private lateinit var searchNews: SearchView
    lateinit var paginationProgressBar: ProgressBar
    var isLoading = false
    var isError = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.search_news_fragment,container,false)
        recyclerView=view.findViewById(R.id.recycler_view)
        recyclerView=view.findViewById(R.id.recycler_view)
        adapter = NewsAdapters(emptyList(),this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(this@SearchNewsFragment.scrollListener)
        searchNews=view.findViewById((R.id.searchView))
        paginationProgressBar=view.findViewById(R.id.paginationProgressBar)
        return view
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?){

        viewModel = (activity as MainActivity).viewModel
        viewModel.getSearchNews("politics")

        searchNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
               viewModel.getSearchNews(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        adapter.updateArticles(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("NewsResponse","Error Message : $message")
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
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
        Navigation.findNavController(requireActivity(),R.id.newNavHostFragment).navigate(R.id.action_searchNewsFragment_to_articleFragment,bundle)
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
            val isTotalMoreThanVisible = totalItemCount >= Constants.QUERY_PAGE_SIZE
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