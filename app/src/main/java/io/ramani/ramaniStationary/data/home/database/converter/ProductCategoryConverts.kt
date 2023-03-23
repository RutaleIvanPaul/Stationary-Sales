package io.ramani.ramaniStationary.data.home.database

import androidx.room.TypeConverter

class ProductCategoryConverts {
    @TypeConverter
    fun listToJson(value: List<JobWorkHistory>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<JobWorkHistory>::class.java).toList()
}