package com.twainarc.tourmate.utils.view

import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView

object Extensions {

    fun AppCompatTextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {

        kotlin.runCatching {

            val spannableString = SpannableString(this.text)
            var startIndexOfLink = -1

            for (link in links) {

                val clickableSpan = object : ClickableSpan() {

                    override fun updateDrawState(textPaint: TextPaint) {

                        textPaint.color = textPaint.linkColor   // text color
                        textPaint.isUnderlineText = false       // link underline
                    }

                    override fun onClick(view: View) {

                        Selection.setSelection((view as TextView).text as Spannable, 0)

                        view.invalidate()
                        link.second.onClick(view)
                    }
                }

                startIndexOfLink = this.text.toString().indexOf(link.first, startIndexOfLink + 1)
                // if(startIndexOfLink == -1) continue // todo verify if texts contains links
                spannableString.setSpan(
                    clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            this.movementMethod = LinkMovementMethod.getInstance() // for click
            this.setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }
}