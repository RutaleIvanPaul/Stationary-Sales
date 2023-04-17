package io.ramani.ramaniStationary.data.createorder.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.ramani.ramaniStationary.data.createorder.models.request.SaleOrderModel

class SaleOrderModelConverts {
    @TypeConverter
    fun listToJson(value: List<SaleOrderModel>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<SaleOrderModel>::class.java).toList()
}