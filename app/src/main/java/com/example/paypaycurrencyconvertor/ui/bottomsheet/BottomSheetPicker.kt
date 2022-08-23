package com.example.paypaycurrencyconvertor.ui.bottomsheet

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.AbsListView
import android.widget.ListView

class BottomSheetPicker(pContext: Context?, pAttrs: AttributeSet?) : ListView(pContext, pAttrs)
{
    override fun onInterceptTouchEvent(pEvent: MotionEvent): Boolean {
        return true
    }

    override fun onTouchEvent(pEvent: MotionEvent): Boolean {
        if (canScrollVertically(this)) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.onTouchEvent(pEvent)
    }

    fun canScrollVertically(view: AbsListView?): Boolean {
        var canScroll = false
        if (view != null && view.childCount > 0) {
            val isOnTop = view.firstVisiblePosition != 0 || view.getChildAt(0).top != 0
            val isAllItemsVisible = isOnTop && lastVisiblePosition == view.childCount
            if (isOnTop || isAllItemsVisible) canScroll = true
        }
        return canScroll
    }
}