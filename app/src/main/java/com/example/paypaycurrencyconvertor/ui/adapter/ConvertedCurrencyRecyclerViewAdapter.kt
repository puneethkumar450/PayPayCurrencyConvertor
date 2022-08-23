package com.example.paypaycurrencyconvertor.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.paypaycurrencyconvertor.databinding.LayoutCurrencyItemBinding
import com.example.paypaycurrencyconvertor.model.ConvertedCurrencyModel
import com.example.paypaycurrencyconvertor.ui.base.BaseViewHolder
import com.example.paypaycurrencyconvertor.ui.viewmodel.CurrencyItemViewModel

class ConvertedCurrencyRecyclerViewAdapter :
    RecyclerView.Adapter<ConvertedCurrencyRecyclerViewAdapter.ConvertedCurrencyViewHolder>() {

    private var mCurrencyRateItemsList: ArrayList<ConvertedCurrencyModel> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConvertedCurrencyViewHolder {

        val binding: LayoutCurrencyItemBinding =
            LayoutCurrencyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return ConvertedCurrencyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConvertedCurrencyViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemCount(): Int {
        return mCurrencyRateItemsList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun addItems(list: List<ConvertedCurrencyModel>) {
        mCurrencyRateItemsList.addAll(list)
        notifyDataSetChanged()
    }


    fun clearItems() {
        mCurrencyRateItemsList.clear()
    }

    inner class ConvertedCurrencyViewHolder(
        itemViewBinding: LayoutCurrencyItemBinding
    ) : BaseViewHolder(itemViewBinding.root) {
        private val mBinding: LayoutCurrencyItemBinding = itemViewBinding
        private var mCurrencyItemViewModel: CurrencyItemViewModel? = null

        override fun onBind(position: Int) {
            val mCurrencyRateModel: ConvertedCurrencyModel = mCurrencyRateItemsList[position]
            mCurrencyItemViewModel =
                CurrencyItemViewModel(mCurrencyRateModel)
            mBinding.currencyItemViewModel = mCurrencyItemViewModel
            mBinding.executePendingBindings()
        }
    }


}