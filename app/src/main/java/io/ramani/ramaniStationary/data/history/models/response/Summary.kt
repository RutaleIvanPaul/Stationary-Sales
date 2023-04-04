package io.ramani.ramaniStationary.data.history.models.response

import com.google.gson.annotations.Expose

data class Summary (
     var avgMin: Float = 0f,
 val date: String? = null,
 val locationsVisited: Long = 0,
 val numOrders: Long = 0,
 val totalSpend: Double = 0.0,
 val totalDiscountValue: Double = 0.0
)
