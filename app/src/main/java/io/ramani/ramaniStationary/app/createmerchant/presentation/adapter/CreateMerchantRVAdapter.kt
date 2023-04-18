package io.ramani.ramaniStationary.app.createmerchant.presentation.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.domain.createmerchant.model.NameValueModel
import io.ramani.ramaniStationary.domain.home.model.MerchantModel

class CreateMerchantRVAdapter(
    data: MutableList<MerchantModel>,
    private val topMerchants: MutableList<NameValueModel>,
    val onItemSelected: (Int, MerchantModel) -> Unit
) :
    BaseQuickAdapter<MerchantModel, BaseViewHolder>(R.layout.item_create_merchant_item, data) {
    override fun convert(holder: BaseViewHolder, item: MerchantModel) {
        with(holder) {
            setText(R.id.item_merchant_name, item.name)
            setText(R.id.item_merchant_phone_number, if (item.members.isNotEmpty()) "+" + item.members.first().phoneNumber else "")

            val matchedMerchants = topMerchants.filter { merchant -> merchant.name.lowercase() == item.name.lowercase() }

            val totalOrders = if (matchedMerchants.isNotEmpty()) matchedMerchants.first().value else "TSH 0"
            setText(R.id.item_merchant_orders, totalOrders)

            itemView.setOnClickListener {
                onItemSelected(getItemPosition(item), item)
            }
        }
    }
}

enum class ItemSelectionType {
    QTY, PRICE_CATEGORY, DISCOUNT, REWARDS, DELETE
}
