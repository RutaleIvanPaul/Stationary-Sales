package io.ramani.ramaniStationary.data.auth.models

data class TaxInformationResponse(
   val _id: String = "",
   val UIN: String,
   val RECEIPTCODE: String,
   val RCNUM: Long,// RCNUM is GC
   val TIN: String,
   val VRN: String,
   val NAME: String
){
   override fun toString(): String {
      return super.toString()
   }
}
