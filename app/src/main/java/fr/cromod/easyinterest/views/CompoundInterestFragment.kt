package fr.cromod.easyinterest.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.cromod.easyinterest.utils.Calculator
import fr.cromod.easyinterest.R
import kotlinx.android.synthetic.main.compound_interest.*

class CompoundInterestFragment() : AbstractFragment() {

    companion object
    {
        private var instance: AbstractFragment? = null

        fun newInstance(): AbstractFragment?
        {
            if (instance == null) instance =
                CompoundInterestFragment()
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
            startCapital = if (edit_start_capital.text.isEmpty()) 0F else edit_start_capital.text.toString()
                .toFloat(),
            growth = if (edit_annual_growth.text.isEmpty()) 0F else edit_annual_growth.text.toString()
                .toFloat(),
            nbOfYears = if (edit_nb_of_years.text.isEmpty()) 0 else edit_nb_of_years.text.toString()
                .toInt(),
            savings = if (edit_savings.text.isEmpty()) 0F else edit_savings.text.toString()
                .toFloat(),
            monthly = !switch_frequency.isChecked
        )
        result = Calculator.roundFloat(result)

        result_final_capital.text = if (result.isFinite()) {
            beautifyNumber(result.toBigDecimal().toPlainString())
        } else ""
    }

    override fun saveInputs()
    {
        inputs = mapOf(edit_start_capital.id to edit_start_capital.text.toString(),
            edit_annual_growth.id to edit_annual_growth.text.toString(),
            edit_nb_of_years.id to edit_nb_of_years.text.toString(),
            edit_savings.id to edit_savings.text.toString(),
            switch_frequency.id to if(switch_frequency.isChecked) "on" else "off"
        )
    }

    override fun restoreInputs()
    {
        setEditTextFromInputs(edit_start_capital, inputs)
        setEditTextFromInputs(edit_annual_growth, inputs)
        setEditTextFromInputs(edit_nb_of_years, inputs)
        setEditTextFromInputs(edit_savings, inputs)

        switch_frequency?.isChecked = inputs.containsKey(switch_frequency?.id)
                                        && inputs[switch_frequency?.id] == "on"
    }
}
