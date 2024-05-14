package MVVMnewsapp.Fragments


import MVVMnewsapp.Activity.MainActivity
import MVVMnewsapp.Entity.NewsArticleEntity
import MVVMnewsapp.Models.Article
import MVVMnewsapp.Models.Source
import NewsViewModel
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.newsandroidmvvmapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArticleFragment : Fragment(R.layout.article_fragment){

    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        val webView=view.findViewById<WebView>(R.id.webView)
        val saveNewsButton=view.findViewById<FloatingActionButton>(R.id.SaveNewsButton)

        val bundle = arguments
        val article : Article
        if (bundle != null) {
            // Retrieve data from the bundle
            val author = bundle.getString("author")
            val content = bundle.getString("content")
            val title = bundle.getString("title")
            val description = bundle.getString("description")
            val source=bundle.getString("source")
            val publishedAt = bundle.getString("published")
            val url = bundle.getString("url")
            val urlToImage = bundle.getString("afterimage")
            article=Article(author,content,description,publishedAt,
                source?.let { Source(it,source) },title,url,urlToImage)

            webView.apply {
                webViewClient = WebViewClient()
                if (url != null) {
                    loadUrl(url)
                }
            }

            saveNewsButton.setOnClickListener {
                Toast.makeText(requireContext(), "Save News Button Clicked", Toast.LENGTH_SHORT).show()
                Log.e("ButtonClicked","News article is $article")
                    viewModel.saveArticle(NewsArticleEntity(
                        author = article.author,
                        content=article.content,
                        title = article.title,
                        description = article.description,
                        source = article.source?.name,
                        published = article.publishedAt,
                        url=article.url,
                        url_to_image = article.urlToImage
                    ))
            }

            // Update UI elements with the retrieved data
//            view.findViewById<TextView>(R.id.NewsArticleAuthor).text = author
//            view.findViewById<TextView>(R.id.NewsArticleContent).text = content
//            view.findViewById<TextView>(R.id.NewsArticleTitle).text = title
//            view.findViewById<TextView>(R.id.NewsArticleDescription).text = description
//            view.findViewById<TextView>(R.id.NewsArticleSource).text = source
//            view.findViewById<TextView>(R.id.NewsArticlePublishedAt).text = publishedAt
//            val newsArticleImage=view.findViewById<ImageView>(R.id.NewsArticleImage)
//            Glide.with(this) // Use 'this' if you're in a fragment
//                .load(urlToImage) // Replace "urlToImage" with the actual URL of the image
//                .into(newsArticleImage)
//            article=Article(author,content,description,publishedAt,
//                source?.let { Source(it,source) },title,url,urlToImage)
        }

    }
}