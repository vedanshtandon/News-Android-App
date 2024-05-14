package MVVMnewsapp.Fragments

import MVVMnewsapp.Activity.MainActivity
import MVVMnewsapp.Adapters.NewsAdapters
import MVVMnewsapp.Adapters.onNewsItemClickListener
import MVVMnewsapp.Models.Article
import MVVMnewsapp.Models.Source
import NewsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsandroidmvvmapp.R

class SavedNewsFragment:Fragment(R.layout.saved_news_fragment),onNewsItemClickListener {


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapters
    lateinit var viewModel: NewsViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.saved_news_fragment,container,false)
        recyclerView=view.findViewById(R.id.recycler_view)
        adapter = NewsAdapters(emptyList(),this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?){


        viewModel = (activity as MainActivity).viewModel
        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer { articles->
            val dataset= mutableListOf<Article>()
            for(news in articles){
                dataset.add(
                    Article(
                    news.author,
                    news.content,
                    news.description,
                    news.published, news.source?.let { Source(it,news.source) },
                    news.title,
                    news.url,
                    news.url_to_image
                )
                )
            }
            adapter.updateArticles(dataset)
        })
    }


    override fun onNewsItemClicked(article: Article) {
        val bundle= Bundle().apply {
            putString("author",article.author)
            putString("content",article.content)
            putString("title",article.title)
            putString("description",article.description)
            putString("source", article.source?.name)
            putString("published",article.publishedAt)
            putString("url",article.url)
            putString("afterimage",article.urlToImage)
        }
        Navigation.findNavController(requireActivity(),R.id.newNavHostFragment).navigate(R.id.action_savedNewsFragment_to_articleFragment,bundle)
    }
}