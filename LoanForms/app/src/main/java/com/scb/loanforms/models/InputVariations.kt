package com.scb.loanforms.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class InputVariations(
    val type: String,
    val minDigit: Int,
    val maxDigit: Int,
    val minAmt: Int,
    val maxAmt: Int
) : Parcelable {


}
