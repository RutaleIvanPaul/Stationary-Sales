package io.ramani.ramaniStationary.data.history.models.response

data class Activity(
    var activityId: String? = null,
    val checkInTime: String? = null,
    val checkOutTime: String? = null,
    val locationId: String? = null,
    val locationName: String? = null,
    val minsSpent: Long? = null,
    val orderId: String? = null,
    val orderStatus: String? = null,
    val deliveryStatus: String? = null,
    val printStatus: String? = null
)
