package com.scb.loanforms.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Forms(val innerViews: ArrayList<InnerViews>, val title: String) : Parcelable {
}