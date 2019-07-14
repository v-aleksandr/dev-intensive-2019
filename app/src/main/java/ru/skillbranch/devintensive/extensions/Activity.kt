package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.android.synthetic.main.activity_main.*


fun Activity.hideKeyboard() {
    val focusedView = this.currentFocus
    if (focusedView != null) {
        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(focusedView.windowToken, 0)
    }
}

fun Activity.isKeyboardOpen(): Boolean {
    return !this.isKeyboardClosed()
}
fun Activity.isKeyboardClosed(): Boolean {
    this.window.decorView.getWindowVisibleDisplayFrame(Rect())
    return this.root_view.rootView.height - this.root_view.height <100
}
fun Activity.showSoftKeyboard(view: View) {
    val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    view.requestFocus()
    inputMethodManager.showSoftInput(view, 0)
}
