package com.scb.loanforms.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class InnerViews(
    val position: String,
    val labelName: String,
    val viewType: String,
    val regex:String,
    val dataList: ArrayList<String>,
    val input: InputVariations
) : Parcelable {


}
