package io.ramani.ramaniStationary.data.home.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import io.ramani.ramaniStationary.domain.home.model.MerchantMemberModel
import io.ramani.ramaniStationary.domain.home.model.ProductCategoryModel

class MerchantMemberConverts {
    @TypeConverter
    fun listToJson(value: List<MerchantMemberModel>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<MerchantMemberModel>::class.java).toList()
}