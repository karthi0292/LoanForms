package com.scb.loanforms.models

import android.os.Parcelable
import com.scb.loanforms.models.Forms
import kotlinx.android.parcel.Parcelize

@Parcelize
class FormData(val formList: ArrayList<Forms>) : Parcelable {
}