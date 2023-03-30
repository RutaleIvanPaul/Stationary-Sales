package io.ramani.ramaniStationary.data.history.models.response

import com.google.gson.annotations.Expose

data class HistoryResponse(
     var activities: List<Activity>? = null,
     val summary: Summary? = null
)
