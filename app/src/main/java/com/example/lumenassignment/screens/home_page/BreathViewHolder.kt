package com.example.lumenassignment.screens.home_page

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.example.lumenassignment.assignment.lumen.me.model.IBreathItem
import com.example.lumenassignment.lumen_app.base.ViewHolderBase
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.breath_item.view.*

class BreathViewHolder(
    parent: ViewGroup,
    @LayoutRes layout: Int,
    private val onClick: PublishSubject<String>
) :
    ViewHolderBase<IBreathItem>(parent, layout) {
    override fun bindData(model: IBreathItem) {
        itemView.text_view_breath_state.text = getText(model.getValidText())
        itemView.text_view_date.text = model.getDateText()
        itemView.setOnClickListener {
            onClick.onNext(model.getDateText())
        }
    }

    private fun getText(@StringRes resString: Int): String {
        return itemView.resources.getString(resString)
    }

}