package MVVMnewsapp.Adapters

import MVVMnewsapp.Models.Article
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsandroidmvvmapp.R


class NewsAdapters(private var list:List<Article>, private val itemClickListener: onNewsItemClickListener):
    RecyclerView.Adapter<NewsAdapters.ViewHolder>(){


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var newsImage:ImageView
        var newsTitle:TextView
        var newsDescription:TextView
        var newsSource:TextView
        var newsPublishedAt:TextView

        init {
            newsImage=view.findViewById(R.id.tvArticleImage)
            newsTitle=view.findViewById(R.id.tvTitle)
            newsSource=view.findViewById(R.id.tvSource)
            newsPublishedAt=view.findViewById(R.id.tvPublishedAt)
            newsDescription=view.findViewById(R.id.tvDescription)

            // set the click listener for the Recycler View Item
            itemView.setOnClickListener{
                val position=adapterPosition
                if(position!=RecyclerView.NO_POSITION){
                    itemClickListener.onNewsItemClicked(list[position])
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(parent.context)
            .inflate(R.layout.news_item_layout,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.newsTitle.text=list[position].title
        holder.newsDescription.text=list[position].description
        holder.newsSource.text= list[position].source?.name
        holder.newsPublishedAt.text=list[position].publishedAt
        Glide.with(holder.itemView.context)
            .load(list[position].urlToImage) // Replace "imageUrl" with the actual URL of the image
            .into(holder.newsImage)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    // Function to update the list of articles in the adapter
    fun updateArticles(newList: List<Article>) {
        list = newList
        notifyDataSetChanged()
    }
}












