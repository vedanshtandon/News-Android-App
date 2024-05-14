package MVVMnewsapp.Activity


import MVVMnewsapp.Database.NewsArticleDatabase
import MVVMnewsapp.Repository.NewsRepository
import MVVMnewsapp.ViewModels.NewsViewModelProviderFactory
import NewsViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsandroidmvvmapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(){

    lateinit var viewModel:NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        val newsNavHostFragment = supportFragmentManager.findFragmentById(R.id.newNavHostFragment) as NavHostFragment
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

        val newsRepository = NewsRepository(NewsArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)
    }
}