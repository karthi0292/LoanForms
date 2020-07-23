package com.scb.loanforms.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class SelectedFormData(var formHashMap: HashMap<String, String>) : Parcelable {
}