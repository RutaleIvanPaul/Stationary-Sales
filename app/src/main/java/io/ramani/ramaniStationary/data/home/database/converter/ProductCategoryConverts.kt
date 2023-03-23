package io.ramani.ramaniStationary.data.home.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.ramani.ramaniStationary.domain.home.model.ProductCategoryModel

class ProductCategoryConverts {
    @TypeConverter
    fun listToJson(value: List<ProductCategoryModel>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<ProductCategoryModel>::class.java).toList()
}