package fr.cromod.easyinterest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.compound_interest.*

class CompoundInterestFragment() : AbstractFragment() {

    companion object
    {
        private var instance: CompoundInterestFragment? = null

        fun newInstance(): CompoundInterestFragment?
        {
            if (instance == null) instance = CompoundInterestFragment()
            return instance
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
        var result = Calculator.finalCapital(
            startCapital = if(edit_start_capital.text.isEmpty()) 0F else edit_start_capital.text.toString().toFloat(),
            growth = if(edit_annual_growth.text.isEmpty()) 0F else edit_annual_growth.text.toString().toFloat(),
            nbOfYears = if(edit_nb_of_years.text.isEmpty()) 0 else edit_nb_of_years.text.toString().toInt(),
            savings = if(edit_savings.text.isEmpty()) 0F else edit_savings.text.toString().toFloat(),
            monthly = !switch_frequency.isChecked
        )
        result = Calculator.roundFloat(result)

        result_final_capital.text = if (result.isFinite()) {
            beautifyNumber(result.toBigDecimal().toPlainString())
        } else ""
    }

}
