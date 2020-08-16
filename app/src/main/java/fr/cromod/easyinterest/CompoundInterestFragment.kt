package fr.cromod.easyinterest

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.compound_interest.*
import kotlin.math.pow

class CompoundInterestFragment() : Fragment() {

    private lateinit var parentActivity: Activity
    var inputs: MutableMap<EditText, String> = mutableMapOf()
    var result: Float = 0F

    companion object
    {
        fun newInstance(): CompoundInterestFragment
        {
            return CompoundInterestFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        val view = inflater.inflate(R.layout.compound_interest, container, false)

        inputs[editStartCapital] = editStartCapital.text.toString()
        inputs[editAnnualGrowth] = editAnnualGrowth.text.toString()
        inputs[editNbOfYears] = editNbOfYears.text.toString()
        inputs[editSavings] = editSavings.text.toString()

        listenTextChanged(editStartCapital)
        listenTextChanged(editAnnualGrowth)
        listenTextChanged(editNbOfYears)
        listenTextChanged(editSavings)

        return view
    }

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if (context is Activity) {
            this.parentActivity = context
        }
    }

    private fun listenTextChanged(editText: EditText)
    {
        editText.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {
                inputs[editText] = s.toString()
                calculateFinalCapital(
                    startCapital = if(inputs[editStartCapital]!!.isEmpty()) 0F else inputs[editStartCapital]!!.toFloat(),
                    growth = if(inputs[editAnnualGrowth]!!.isEmpty()) 0F else inputs[editAnnualGrowth]!!.toFloat(),
                    nbOfYears = if(inputs[editNbOfYears]!!.isEmpty()) 0 else inputs[editNbOfYears]!!.toInt(),
                    savings = if(inputs[editSavings]!!.isEmpty()) 0F else inputs[editSavings]!!.toFloat(),
                    monthly = !switchFrequency.isChecked
                )
                resultfinalCapital.setText(result.toString())
            }
            override fun afterTextChanged(arg0: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        })
    }

    fun onChangeFrequency(view: View)
    {
        // Update result
        calculateFinalCapital(
            startCapital = if(editStartCapital.text.isEmpty()) 0F else editStartCapital.text.toString().toFloat(),
            growth = if(editAnnualGrowth.text.isEmpty()) 0F else editAnnualGrowth.text.toString().toFloat(),
            nbOfYears = if(editNbOfYears.text.isEmpty()) 0 else editNbOfYears.text.toString().toInt(),
            savings = if(editSavings.text.isEmpty()) 0F else editSavings.text.toString().toFloat(),
            monthly = !switchFrequency.isChecked
        )
        resultfinalCapital.setText(result.toString())

        // Update label
        labelFrequency.setText(if(switchFrequency.isChecked) R.string.yearly else R.string.monthly)
    }

    fun calculateFinalCapital( startCapital: Float, growth: Float, nbOfYears: Int,
                               savings: Float, monthly: Boolean)
    {
        val factor: Float = 1 + growth / 100
        val period: Float = if(monthly) 12F else 1F
        result = startCapital*(factor.pow(nbOfYears))
        for (i in 1..nbOfYears) {
            result += period*savings*factor.pow(i-1)
        }
    }

}
