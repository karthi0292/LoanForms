package com.scb.loanforms.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.scb.loanforms.models.FormData
import com.scb.loanforms.R
import com.scb.loanforms.Utils

class HomeActivity : AppCompatActivity() {
    companion object {
        val KEY_FORM_DATA = "formData"
        val PAGE_NO = "pageNo"
        val KEY_SELECTED_FORMDATA = "selectedFormData"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val formData = getObjectFromJsonFile()

        val navController = this.findNavController(R.id.myNavHostFragment)

        navController.setGraph(navController.graph, setBundleData(formData))
    }

    private fun setBundleData(formData: FormData?): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(KEY_FORM_DATA, formData)
        bundle.putInt(PAGE_NO, 0)
        bundle.putParcelable(KEY_SELECTED_FORMDATA, null)
        return bundle
    }

    private fun getObjectFromJsonFile(): FormData? {
        val formJsonData =
            Utils.readJsonFromAsset(this)
        return Gson().fromJson(formJsonData, FormData::class.java)
    }
}
