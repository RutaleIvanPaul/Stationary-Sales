package io.ramani.ramaniStationary.app.reports.presentation.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.domain.createmerchant.model.NameValueModel
import java.text.NumberFormat
import java.util.*

class NameValueRVAdapter(
    data: MutableList<NameValueModel>
) :
    BaseQuickAdapter<NameValueModel, BaseViewHolder>(R.layout.item_name_value, data) {
    override fun convert(holder: BaseViewHolder, item: NameValueModel) {
        with(holder) {
            setText(R.id.item_name_value_name, item.name)
            setText(R.id.item_name_value_value, String.format("Tsh %s", NumberFormat.getNumberInstance(Locale.US).format(item.value)))
        }
    }
}
