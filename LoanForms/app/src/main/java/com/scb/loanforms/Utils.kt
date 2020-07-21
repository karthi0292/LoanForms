package com.scb.loanforms

import android.content.Context
import android.content.res.AssetManager
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.recyclerview.widget.RecyclerView
import java.util.regex.Pattern

class Utils {
    companion object {
        val edtTextType: String = "EDT_TEXT_T11"
        val spinnerType: String = "DROP_DOWN_T12"
        val checkBoxType: String = "CHECK_BOX_T13"

        fun readJsonFromAsset(context: Context): String? {
            var json: String
            try {
                val inputStream = context.assets.open("form_data.txt", AssetManager.ACCESS_BUFFER)
                json = inputStream.bufferedReader().use { it.readText() }
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            return json
        }

        fun isRegexValid(regex: String, value: String): Boolean {
            val pattern: Pattern = Pattern.compile(regex)
            return pattern.matcher(value).matches()
        }

        fun getItemEdit(
            labelName: String,
            lloutRoot: LinearLayout,
            viewId: Int,
            type: String,
            cxt: Context
        ): EditText {

            val edtText: EditText =
                LayoutInflater.from(cxt)
                    .inflate(R.layout.item_edittext, lloutRoot, false) as EditText
            edtText.id = viewId
            edtText.hint = labelName
            when (type) {
                "text" -> edtText.inputType = InputType.TYPE_CLASS_TEXT
                "number" -> edtText.inputType = InputType.TYPE_CLASS_NUMBER
                "email" -> edtText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            return edtText
        }

        fun getItemSpinner(
            list: ArrayList<String>, lloutRoot: LinearLayout,
            viewId: Int,
            cxt: Context
        ): AppCompatSpinner {

            val spinner: AppCompatSpinner = LayoutInflater.from(cxt)
                .inflate(R.layout.item_spinner, lloutRoot, false) as AppCompatSpinner
            spinner.id = viewId

            val arrayAdapter = ArrayAdapter(cxt, android.R.layout.simple_spinner_item, list)
            arrayAdapter.setDropDownViewResource(R.layout.item_dropdown)
            spinner.adapter = arrayAdapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    //Toast.makeText(cxt, "Selected", Toast.LENGTH_LONG).show()
                }
            }
            return spinner
        }

        fun getItemCheckBox(
            labelName: String,
            lloutRoot: LinearLayout,
            viewId: Int,
            dataList: ArrayList<String>,
            cxt: Context
        ): View {
            val view: View =
                LayoutInflater.from(cxt).inflate(R.layout.item_recyclerview, lloutRoot, false)

            view.findViewById<TextView>(R.id.tvTitle).text = labelName
            val rvCheckBox = view.findViewById<RecyclerView>(R.id.rvCheckBox)

            val checkBoxRvAdapter = CheckBoxRvAdapter(cxt, dataList)
            rvCheckBox.adapter = checkBoxRvAdapter
            view.id = viewId
            return view
        }
    }

}