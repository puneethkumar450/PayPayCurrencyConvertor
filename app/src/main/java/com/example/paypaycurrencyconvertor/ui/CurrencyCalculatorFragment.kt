package com.example.paypaycurrencyconvertor.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.paypaycurrencyconvertor.R
import com.example.paypaycurrencyconvertor.data.source.local.database.entity.CurrencyRate
import com.example.paypaycurrencyconvertor.databinding.FragmentCurrencyCalculatorBinding
import com.example.paypaycurrencyconvertor.model.ConvertedCurrencyModel
import com.example.paypaycurrencyconvertor.ui.adapter.ConvertedCurrencyRecyclerViewAdapter
import com.example.paypaycurrencyconvertor.ui.bottomsheet.BottomSheetPicker
import com.example.paypaycurrencyconvertor.ui.viewmodel.CurrencyCalculatorViewModel
import com.example.paypaycurrencyconvertor.utils.Resource
import com.example.paypaycurrencyconvertor.utils.dialogUtils.CustomDialogCallback
import com.example.paypaycurrencyconvertor.utils.dialogUtils.CustomDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CurrencyCalculatorFragment : Fragment(), ICurrencyCalculatorListener
{
    private lateinit var mCurrencyCalBinding: FragmentCurrencyCalculatorBinding
    private val mCurrencyCalViewModel by viewModels<CurrencyCalculatorViewModel>()
    private lateinit var mCurrencyConverterAdapter: ConvertedCurrencyRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mCurrencyCalBinding = FragmentCurrencyCalculatorBinding.inflate(layoutInflater)
        mCurrencyCalBinding.lifecycleOwner = this
        mCurrencyCalBinding.currencyCalculatorViewModel = mCurrencyCalViewModel
        return mCurrencyCalBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCurrencyCalViewModel.setNavigator(this)
        mCurrencyCalViewModel.mIsLoading.value = true
        setupObservers()
        mCurrencyCalViewModel.startUpdates()
    }


    override fun setupObservers() {
        mCurrencyCalViewModel.mCurrencyResponse.observe(requireActivity()) { response ->
            mCurrencyCalViewModel.getCurrencyResponse(response)
            setupCurrencyList(response)
        }

        mCurrencyCalBinding.currencyAmountInputEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                Log.d("CCFragment"," afterTextChanged called" )
                if(mCurrencyCalViewModel.validateText(s.toString())){
                    mCurrencyCalViewModel.onClickCalculateCurrency()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        mCurrencyCalBinding.llSelectedCurrency.setOnClickListener {
            showBottomSheet();
        }
    }

    private fun showBottomSheet()
    {
        val lView: View = layoutInflater.inflate(R.layout.layout_bottom_sheet_country, null)
        val lBottomSheetListView = lView.findViewById<BottomSheetPicker>(R.id.lv_country)
        val lBottomSheetClose = lView.findViewById<ImageView>(R.id.close_bottom_sheet)
        val lCountryList = mCurrencyCalViewModel.prepareCurrencyNameList()
        val lListAdapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            lCountryList
        )
        lBottomSheetListView.adapter = lListAdapter
       val lBottomSheetDialog = BottomSheetDialog(requireActivity())
        lBottomSheetDialog.setContentView(lView)
        lBottomSheetDialog.show()
        lBottomSheetClose.setOnClickListener {
            lBottomSheetDialog.dismiss()
        }
        lBottomSheetListView.onItemClickListener = OnItemClickListener { _, view, position, id ->
            val lSelectedItem = lCountryList[position]
            mCurrencyCalViewModel.mCurrencyNameLiveData.value = lSelectedItem
            mCurrencyCalBinding.tvSelectedCurrency.text = lSelectedItem
            mCurrencyCalViewModel.onClickCalculateCurrency()
            lBottomSheetDialog.dismiss()
        }
    }


    override fun onShowDialog(aMessage: String) {
        showDialog(requireContext().resources.getString(R.string.msg_failed_title), aMessage, true)
    }

    private fun showDialog(aTitle: String, aMessage: String, aIsOnlyPositive: Boolean) {
        var dialog: DialogFragment? = null
        var bntText = requireContext().getString(R.string.yes_txt)
        if (aIsOnlyPositive) {
            bntText = requireContext().getString(R.string.btn_text_ok)
        }
        dialog = CustomDialogFragment(
            aTitle,
            aMessage,
            bntText,
            requireContext().getString(R.string.cancel_text),
            object : CustomDialogCallback {
                override fun onNextClick() {
                    dialog?.dismiss()
                }

                override fun onPositiveClick() {
                    dialog?.dismiss()
                }

                override fun onCloseClick() {
                    dialog?.dismiss()
                }
            },
            aIsOnlyPositive,
        )
        dialog.show(childFragmentManager, "DIALOG_TAG")
    }

    private fun setupCurrencyList(aResponse: Resource<List<CurrencyRate>>)
    {
        val lData = aResponse.aData?.toMutableList()
        if (!lData.isNullOrEmpty()) {
            val lText = DEFAULT_CURRENCY
            mCurrencyCalBinding.tvSelectedCurrency.text = lText
            mCurrencyCalViewModel.mCurrencyNameLiveData.value = lText
        }
    }

    override fun onShowList(aConvertedList: List<ConvertedCurrencyModel>) {
        mCurrencyCalViewModel.mIsDataFetching.set(true)
        val lState = mCurrencyCalBinding.convertedCurrencyRecyclerView.layoutManager?.onSaveInstanceState()
        mCurrencyConverterAdapter = ConvertedCurrencyRecyclerViewAdapter()
        mCurrencyCalBinding.convertedCurrencyRecyclerView.adapter = mCurrencyConverterAdapter
        mCurrencyConverterAdapter.clearItems()
        mCurrencyConverterAdapter.addItems(aConvertedList)
        if(lState != null){
            mCurrencyCalBinding.convertedCurrencyRecyclerView.layoutManager?.onRestoreInstanceState(lState)
        }
    }

    companion object{
        private const val DEFAULT_CURRENCY = "USD"
    }
}




