package com.scb.loanforms.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.util.forEach
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.scb.loanforms.R
import com.scb.loanforms.Utils
import com.scb.loanforms.models.DynamicData
import com.scb.loanforms.models.FormData
import com.scb.loanforms.models.InnerViews
import com.scb.loanforms.models.SelectedFormData
import kotlinx.android.synthetic.main.fragment_form.view.*

/**
 * A simple [Fragment] subclass.
 */
class FormFragment : Fragment(), View.OnClickListener {
    private var pageNo: Int = 0
    private lateinit var formData: FormData
    private lateinit var cxt: Context
    private lateinit var lloutRoot: LinearLayout
    private lateinit var btnNext: Button
    private lateinit var btnSubmit: Button
    private lateinit var btnPrevious: Button
    private lateinit var tvTitle: TextView
    private lateinit var dynamicDataList: ArrayList<DynamicData>
    private var selectedFormData: SelectedFormData
    private var map: HashMap<String, String>
    private lateinit var fieldsBooleanArray: SparseBooleanArray
    private var totalNoOfPages: Int = 0

    init {
        map = HashMap()
        selectedFormData = SelectedFormData(map)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        getData()

        val view = inflater.inflate(R.layout.fragment_form, container, false)

        initViews(view)

        setBottomBarVisiblity()

        createViews(formData)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getData() {
        val args: FormFragmentArgs by navArgs()
        formData = args.formData
        pageNo = args.pageNo
        if (args.selectedFormData != null) {
            selectedFormData = args.selectedFormData!!
        }

    }

    private fun initViews(view: View) {
        lloutRoot = view.fragForm_lloutRoot
        btnNext = view.fragForm_btnNext
        btnSubmit = view.fragForm_btnSubmit
        btnPrevious = view.fragForm_btnPrevious
        tvTitle = view.fragForm_tvTitle

        btnNext.setOnClickListener(this)
        btnSubmit.setOnClickListener(this)
        btnPrevious.setOnClickListener(this)
    }

    private fun setBottomBarVisiblity() {
        totalNoOfPages = formData.formList.size
        if (pageNo == totalNoOfPages - 1)
            btnNext.text = getString(R.string.submit)
        else if (pageNo == 0) {
            btnPrevious.isEnabled = false
            btnNext.visibility = View.VISIBLE
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.cxt = context!!
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fragForm_btnPrevious -> {
                pageNo -= 1
                v.findNavController().navigate(
                    FormFragmentDirections.actionFormFragmentSelf(formData, selectedFormData)
                        .setPageNo(pageNo)
                )
            }
            R.id.fragForm_btnNext -> {
                if (pageNo != totalNoOfPages - 1) {
                    if (isValidFields()) {
                        pageNo += 1
                        v.findNavController().navigate(
                            FormFragmentDirections.actionFormFragmentSelf(
                                formData,
                                selectedFormData
                            )
                                .setPageNo(pageNo)
                        )
                    }
                } else {

                    if (isValidFields()) {
                        var strBuilder: StringBuilder = StringBuilder()
                        for (item in selectedFormData.formHashMap) {
                            strBuilder.append("${item.key} is ${item.value}\n")
                            Log.d("SelectedValue", "${item.key} is ${item.value}")
                        }
                        Toast.makeText(
                            cxt,
                            strBuilder.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            R.id.fragForm_btnSubmit -> {

            }
        }
    }

    /*-------------------------------Creating Dynamic views based on JsonFile---------------------*/
    private fun createViews(formData: FormData) {

        dynamicDataList = ArrayList()

        val formsList = formData.formList

        val title = formsList[pageNo].title
        tvTitle.text = title

        val innerViewsList = formsList[pageNo].innerViews

        generateViews(innerViewsList)

    }

    private fun generateViews(
        innerViewsList: ArrayList<InnerViews>
    ) {
        for ((count, innerViews) in innerViewsList.withIndex()) {
            var viewId: Int = count + 1

            addingDynamicViews(innerViews, viewId)

            addingGeneratedViewIds(viewId, count)

        }
    }

    private fun addingGeneratedViewIds(viewId: Int, position: Int) {
        var dynamicData = DynamicData(viewId, position)
        dynamicDataList.add(dynamicData)
    }

    private fun addingDynamicViews(innerViews: InnerViews, viewId: Int) {
        when (innerViews.viewType) {
            Utils.edtTextType -> {
                lloutRoot.addView(
                    Utils.getItemEdit(
                        innerViews.labelName,
                        lloutRoot,
                        viewId,
                        innerViews.input.type,
                        cxt,
                        selectedFormData.formHashMap?.get(innerViews.labelName)
                    )
                )
            }
            Utils.spinnerType -> {
                lloutRoot.addView(
                    Utils.getItemSpinner(
                        innerViews.dataList,
                        lloutRoot,
                        viewId,
                        cxt,
                        innerViews.dataList.indexOf(selectedFormData.formHashMap?.get(innerViews.labelName))
                    )
                )
            }
            Utils.checkBoxType -> {
                lloutRoot.addView(
                    Utils.getItemCheckBox(
                        innerViews.labelName,
                        lloutRoot,
                        viewId,
                        innerViews.dataList,
                        cxt,
                        innerViews.dataList.indexOf(selectedFormData.formHashMap?.get(innerViews.labelName)),
                        object : Utils.SetCheckBoxValueListener {
                            override fun setSelectedValue(selectedValue: String) {
                                selectedFormData.formHashMap?.put(
                                    innerViews.labelName,
                                    selectedValue
                                )
                            }
                        }
                    )
                )
            }
        }
    }

    /*----------------------------------------Validation-----------------------------------------*/
    private fun isValidFields(): Boolean {
        var isValid: Boolean
        fieldsBooleanArray = SparseBooleanArray()
        for (dynamicData in dynamicDataList) {
            val innerViews = formData.formList[pageNo].innerViews[dynamicData.position]

            isValid = isValidationBasedOnViewTypes(innerViews, dynamicData)
            fieldsBooleanArray.put(dynamicData.position, isValid)
        }
        map?.let { selectedFormData.formHashMap?.putAll(it) }
        return isErrors()
    }

    private fun isErrors(): Boolean {
        fieldsBooleanArray.forEach { i: Int, isValid: Boolean ->
            if (!isValid)
                return false
        }
        return true
    }

    private fun isValidationBasedOnViewTypes(
        innerViews: InnerViews,
        dynamicData: DynamicData
    ): Boolean {
        var isValid = false
        when (innerViews.viewType) {
            Utils.edtTextType -> {
                isValid = isValidationForEdtText(innerViews, dynamicData)
            }
            Utils.spinnerType -> {
                isValid = isValidationForSpinner(innerViews, dynamicData)
            }
            Utils.checkBoxType -> {
                isValid = isValidationForCheckBox(innerViews, dynamicData)
            }
        }
        return isValid
    }

    private fun isValidationForEdtText(
        innerViews: InnerViews,
        dynamicData: DynamicData
    ): Boolean {
        var isValid = true
        val edtText = lloutRoot.findViewById<EditText>(dynamicData.viewId)
        val enteredText = edtText.text.toString().trim()

        if (enteredText != null && enteredText.isNotEmpty()) {
            val enteredTextSize = enteredText.length
            val minLength = innerViews.input.minDigit
            val maxLength = innerViews.input.maxDigit
            val regex = innerViews.regex
            val minAmt = innerViews.input.minAmt
            val maxAmt = innerViews.input.maxAmt

            if (minLength == 0 && maxLength == 0 && minAmt == 0 && maxAmt == 0) {
                if (Utils.isRegexValid(
                        regex,
                        enteredText
                    )
                ) {
                    map?.put(innerViews.labelName, enteredText)
                } else {
                    isValid = false
                    edtText.error = "Entered value is invalid"
                }
            } else if (minLength != 0 && maxLength == 0 && enteredTextSize < minLength) {
                edtText.error = "Min it should be $minLength digit values"
                isValid = false
            } else if (minLength == 0 && maxLength != 0 && enteredTextSize != maxLength) {
                edtText.error = "It should be $maxLength digit values"
                isValid = false
            } else if (minLength != 0 && maxLength != 0) {
                if (enteredTextSize in minLength..maxLength) {
                    map?.put(innerViews.labelName, enteredText)
                } else {
                    isValid = false
                    edtText.error =
                        "No of digits must be between $minLength to $maxLength "
                }
            } else if (minAmt != 0 && maxAmt != 0) {
                if (Integer.parseInt(enteredText) in minAmt..maxAmt) {
                    map?.put(innerViews.labelName, enteredText)
                } else {
                    isValid = false
                    edtText.error =
                        "Range must be between $minAmt to $maxAmt "
                }
            } else {
                map?.put(innerViews.labelName, enteredText)
            }
        } else {
            edtText.error = "Required"
            isValid = false
        }
        return isValid
    }

    private fun isValidationForSpinner(
        innerViews: InnerViews,
        dynamicData: DynamicData
    ): Boolean {
        var isValid = true
        val spinner = lloutRoot.findViewById<AppCompatSpinner>(dynamicData.viewId)
        val selValue = spinner.selectedItemPosition
        if (selValue == 0) {
            isValid = false
            val selView = spinner.selectedView as TextView
            selView.error = "Required"
        } else {
            map?.put(innerViews.labelName, innerViews.dataList[selValue])
        }
        return isValid
    }

    private fun isValidationForCheckBox(innerViews: InnerViews, dynamicData: DynamicData): Boolean {
        var isValid = true
        val view = lloutRoot.findViewById<View>(dynamicData.viewId)
        val rv = view.findViewById<RecyclerView>(R.id.rvCheckBox)

        val chkBox = rv.findViewById<CheckBox>(R.id.checkbox)

        val checkBoxValue = selectedFormData.formHashMap?.get(innerViews.labelName)
        if (checkBoxValue.isNullOrEmpty()) {
            isValid = false
            chkBox.error = "Required"
        } else {
            chkBox.error = null
        }
        return isValid
    }
}