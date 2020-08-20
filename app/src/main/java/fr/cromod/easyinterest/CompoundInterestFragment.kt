package fr.cromod.easyinterest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.compound_interest.*
import kotlin.math.pow
import kotlin.math.round

class CompoundInterestFragment() : AbstractFragment() {

    companion object
    {
        private var instance: CompoundInterestFragment? = null

        fun newInstance(): CompoundInterestFragment?
        {
            if (instance == null) instance = CompoundInterestFragment()
            return instance
        }

        fun calculateFinalCapital( startCapital: Float, growth: Float, nbOfYears: Int,
                                   savings: Float, monthly: Boolean): Float
        {
            // some constants to initialize
            val factor: Float = 1 + growth / 100
            val period: Float = if(monthly) 12F else 1F
            // compound interests on start capital
            var result: Float = startCapital*(factor.pow(nbOfYears))
            // compound interests on savings
            for (i in 1..nbOfYears) {
                result += period*savings*factor.pow(i-1)
            }
            // round the result to 2 decimal places
            result = round(result*100)/100
            return result
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.compound_interest, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        listenTextChanged(edit_start_capital)
        listenTextChanged(edit_annual_growth)
        listenTextChanged(edit_nb_of_years)
        listenTextChanged(edit_savings)

        listenFrequencyChanged()
    }

    private fun listenFrequencyChanged()
    {
        switch_frequency.setOnClickListener {
            updateResult()
            label_frequency.setText(if(switch_frequency.isChecked) R.string.yearly else R.string.monthly)
        }
    }

    override fun updateResult()
    {
        var result = calculateFinalCapital(
            startCapital = if(edit_start_capital.text.isEmpty()) 0F else edit_start_capital.text.toString().toFloat(),
            growth = if(edit_annual_growth.text.isEmpty()) 0F else edit_annual_growth.text.toString().toFloat(),
            nbOfYears = if(edit_nb_of_years.text.isEmpty()) 0 else edit_nb_of_years.text.toString().toInt(),
            savings = if(edit_savings.text.isEmpty()) 0F else edit_savings.text.toString().toFloat(),
            monthly = !switch_frequency.isChecked
        )

        if (result.isFinite())
        {
            result = round(result * 100) / 100
            result_final_capital.setText(result.toBigDecimal().toPlainString())
        }
        else
        {
            result_final_capital.setText("")
        }
    }

}
