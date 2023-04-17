package io.ramani.ramaniStationary.data.createorder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Sales")
data class SaleJsonModel (
    @PrimaryKey
    var identify: Long = 0,
    var isSent: Boolean = false,    // Flag to indicate whether this request is sent to server or not
    var request: String = ""
)