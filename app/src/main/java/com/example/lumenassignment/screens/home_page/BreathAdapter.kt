package com.example.lumenassignment.screens.home_page

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.lumenassignment.R
import com.example.lumenassignment.assignment.lumen.me.model.BreathItem
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class BreathAdapter : ListAdapter<BreathItem, BreathViewHolder>(BreathItemDiff) {

    private val onClick = PublishSubject.create<String>()

    object BreathItemDiff : DiffUtil.ItemCallback<BreathItem>() {
        override fun areContentsTheSame(oldItem: BreathItem, newItem: BreathItem): Boolean {
            return areItemsTheSame(oldItem, newItem)
        }

        override fun areItemsTheSame(oldItem: BreathItem, newItem: BreathItem): Boolean {
            return oldItem.date == newItem.date
        }
    }

    fun onClick(): Observable<String>? {
        return onClick.hide()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreathViewHolder {
        return BreathViewHolder(parent, R.layout.breath_item, onClick)
    }

    override fun onBindViewHolder(holder: BreathViewHolder, position: Int) {
        holder.bindData(getItem(position))
    }

}