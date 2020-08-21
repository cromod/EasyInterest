package fr.cromod.easyinterest

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlin.text.Regex

abstract class AbstractFragment : Fragment() {

    private lateinit var parentActivity: Activity

    companion object
    {
        open fun beautifyNumber(numberStr: String): String
        {
            val split = numberStr.split(".")
            val firstDigits = split[0].substring(0,split[0].length%3)
            var nextDigits = split[0].substring(split[0].length%3)
            val afterDigits = if (split.size > 1) "." + split[1] else ""
            return firstDigits + nextDigits.replace("([0-9]{3})".toRegex(), " $1") + afterDigits
        }
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if (context is Activity) {
            this.parentActivity = context
        }
    }

    open fun listenTextChanged(editText: EditText)
    {
        editText.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
                updateResult()
            }
            override fun afterTextChanged(arg0: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        })
    }

    abstract fun updateResult()
}
