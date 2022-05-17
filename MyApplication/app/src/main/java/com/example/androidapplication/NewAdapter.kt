package com.example.androidapplication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class NewAdapter(private val subjectsList: ArrayList<Subjects>) :
    RecyclerView.Adapter<NewAdapter.NewViewHolder>() {


    class NewViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val titleImage : ShapeableImageView = itemView.findViewById(R.id.imageForTitle)
        val titleText : TextView = itemView.findViewById(R.id.titleText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,
        parent, false)
        return NewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val currentItem = subjectsList[position]
        holder.titleImage.setImageResource(currentItem.image_for_title)
        holder.titleText.text = currentItem.text_title
    }

    override fun getItemCount(): Int {
        return subjectsList.size
    }

}