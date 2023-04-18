package io.ramani.ramaniStationary.data.createorder.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.ramani.ramaniStationary.data.createorder.models.request.SaleOrderItemModel

class SaleOrderItemModelConverts {
    @TypeConverter
    fun listToJson(value: List<SaleOrderItemModel>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<SaleOrderItemModel>::class.java).toList()
}