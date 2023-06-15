package com.capestone.facential.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.capestone.facential.R
import com.capestone.facential.data.remote.model.History
import com.capestone.facential.ui.result.DetailActivity

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val historyList = mutableListOf<History>()

    fun setHistory(history: List<History>) {
        historyList.clear()
        historyList.addAll(history)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_row_history, parent, false)
        return HistoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HistoryAdapter.HistoryViewHolder, position: Int) {
        val history = historyList[historyList.size - position - 1]
        holder.bind(history)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("historyId", history.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val historySkinTextView: TextView = itemView.findViewById(R.id.tv_card_history_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.tv_card_history_date)
        private val historyImageView: ImageView = itemView.findViewById(R.id.iv_card_history)

        fun bind(history: History) {
            historySkinTextView.text = history.skinType
            dateTextView.text = history.date.toString()

            Glide.with(itemView)
                .load(history.imageUrl)
                .centerCrop()
                .apply(RequestOptions().placeholder(R.mipmap.ic_launcher))
                .into(historyImageView)
        }
    }

}