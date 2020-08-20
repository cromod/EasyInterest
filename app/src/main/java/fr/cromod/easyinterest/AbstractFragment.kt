package fr.cromod.easyinterest

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.fragment.app.Fragment

abstract class AbstractFragment : Fragment() {

    private lateinit var parentActivity: Activity

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
