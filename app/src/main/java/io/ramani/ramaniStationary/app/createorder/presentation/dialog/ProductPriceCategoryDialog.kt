package io.ramani.ramaniStationary.app.createorder.presentation.dialog

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.ramani.ramaniStationary.R
import io.ramani.ramaniStationary.app.createorder.presentation.adapter.CheckoutPriceCategoriesRVAdapter
import io.ramani.ramaniStationary.domain.home.model.ProductCategoryModel
import io.ramani.ramaniStationary.domain.home.model.ProductModel

/**
 * @description     Price category selection dialog
 *
 * @author          Adrian
 */
class ProductPriceCategoryDialog(
    context: Context,
    private val product: ProductModel,
    private val itemPosition: Int,
    private val currency: String,
    val onItemChanged: (Int, ProductModel) -> Unit
) : Dialog(context) {

    var category = product.selectedPriceCategory
    var categoryAdapter: CheckoutPriceCategoriesRVAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_checkout_price_category)

        setCancelable(false)

        window?.setBackgroundDrawableResource(android.R.color.transparent);
        window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

        initView()
    }

    private fun initView() {
        updateRV()

        findViewById<TextView>(R.id.dialog_checkout_price_category_okay).setOnClickListener {
            product.selectedPriceCategory = category
            onItemChanged(itemPosition, product)
            dismiss()
        }
    }

    private fun updateRV() {
        categoryAdapter = CheckoutPriceCategoriesRVAdapter(product.productCategories as MutableList<ProductCategoryModel>, category, currency) { cate ->
            category = cate
            updateRV()
        }

        findViewById<RecyclerView>(R.id.dialog_checkout_price_category_categories).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
    }

    override fun onBackPressed() {
        dismiss()
    }
}