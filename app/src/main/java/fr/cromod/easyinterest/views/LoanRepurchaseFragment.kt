package fr.cromod.easyinterest.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.cromod.easyinterest.utils.Calculator
import fr.cromod.easyinterest.R
import kotlinx.android.synthetic.main.loan_repurchase.*
import kotlinx.android.synthetic.main.loan_repurchase.edit_remaining_capital
import kotlinx.android.synthetic.main.loan_repurchase.edit_remaining_duration
import kotlinx.android.synthetic.main.loan_repurchase.result_gain_on_cost
import kotlinx.android.synthetic.main.loan_repurchase.result_loan_cost
import kotlinx.android.synthetic.main.loan_repurchase.switch_result

class LoanRepurchaseFragment() : AbstractFragment() {

    // Intermediate inputs for calculation
    var initialRemainingDuration: Float = 0F
    var initialMonthlyPayment: Float = 0F
    var initialLoanCost: Float = 0F

    // Results
    var result: Float = 0F
    var gain: Float = 0F
    var loanCost: Float = 0F
    var gainOnCost: Float = 0F

    companion object
    {
        private var instance: AbstractFragment? = null

        fun newInstance(): AbstractFragment?
        {
            if (instance == null) {
                instance = LoanRepurchaseFragment()
            }
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.loan_repurchase, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        listenTextChanged(edit_remaining_capital)
        listenTextChanged(edit_remaining_duration)
        listenTextChanged(edit_initial_interest_rate)
        listenTextChanged(edit_new_interest_rate)

        listenResultTypeChanged()
    }

    private fun listenResultTypeChanged()
    {
        switch_result.setOnClickListener {
            updateResult()
            label_repurchase_result.setText(if(switch_result.isChecked) R.string.remaining_months_after_repurchase else R.string.new_monthly_payment)
        }
    }

    override fun updateResult()
    {
        initializeUpdating()

        if (switch_result.isChecked)
        {
            updateDuration(initialMonthlyPayment, initialRemainingDuration, initialLoanCost)
        }
        else
        {
            updateMonthlyPayment(initialMonthlyPayment, initialRemainingDuration, initialLoanCost)
        }

        displayResult()
    }

    private fun initializeUpdating()
    {
        initialRemainingDuration = if(edit_remaining_duration.text.isEmpty()) 0F else edit_remaining_duration.text.toString().toFloat()

        initialMonthlyPayment =
            Calculator.monthlyPayment(
                loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                    .toFloat(),
                interestRate = if (edit_initial_interest_rate.text.isEmpty()) 0F else edit_initial_interest_rate.text.toString()
                    .toFloat(),
                nbOfMonths = initialRemainingDuration
            )

        initialLoanCost = Calculator.loanCost(
            loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                .toFloat(),
            monthlyPayment = initialMonthlyPayment,
            nbOfMonths = initialRemainingDuration
        )
    }

    private fun updateDuration(initialMonthlyPayment: Float, initialRemainingDuration: Float, initialLoanCost: Float)
    {
        result = Calculator.loanDuration(
            loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                .toFloat(),
            interestRate = if (edit_new_interest_rate.text.isEmpty()) 0F else edit_new_interest_rate.text.toString()
                .toFloat(),
            monthlyPayment = initialMonthlyPayment
        )
        gain = initialRemainingDuration - result

        loanCost = Calculator.loanCost(
            loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                .toFloat(),
            monthlyPayment = initialMonthlyPayment,
            nbOfMonths = result
        )
        gainOnCost = initialLoanCost - loanCost

        result =
            Calculator.roundFloat(result, 0)
        gain = Calculator.roundFloat(gain, 0)
        loanCost =
            Calculator.roundFloat(loanCost)
        gainOnCost =
            Calculator.roundFloat(gainOnCost)
    }

    private fun updateMonthlyPayment(initialMonthlyPayment: Float, initialRemainingDuration: Float,  initialLoanCost: Float)
    {
        result = Calculator.monthlyPayment(
            loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                .toFloat(),
            interestRate = if (edit_new_interest_rate.text.isEmpty()) 0F else edit_new_interest_rate.text.toString()
                .toFloat(),
            nbOfMonths = initialRemainingDuration
        )
        gain = initialMonthlyPayment - result

        loanCost = Calculator.loanCost(
            loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                .toFloat(),
            monthlyPayment = result,
            nbOfMonths = initialRemainingDuration
        )
        gainOnCost = initialLoanCost - loanCost

        result = Calculator.roundFloat(result)
        gain = Calculator.roundFloat(gain)
        loanCost =
            Calculator.roundFloat(loanCost)
        gainOnCost =
            Calculator.roundFloat(gainOnCost)
    }

    private fun displayResult()
    {
        repurchase_result.text = if (result.isFinite()) {
            beautifyNumber(result.toBigDecimal().toPlainString())
        } else ""

        repurchase_gain.text = if (gain.isFinite()) {
            beautifyNumber(gain.toBigDecimal().toPlainString())
        } else ""

        result_loan_cost.text = if (loanCost.isFinite()) {
            beautifyNumber(loanCost.toBigDecimal().toPlainString())
        } else ""

        result_gain_on_cost.text = if (gainOnCost.isFinite()) {
            beautifyNumber(gainOnCost.toBigDecimal().toPlainString())
        } else ""
    }

    override fun saveInputs()
    {
        if (!isVisible) return

        inputs = mapOf(edit_remaining_capital.id to edit_remaining_capital.text.toString(),
            edit_remaining_duration.id to edit_remaining_duration.text.toString(),
            edit_initial_interest_rate.id to edit_initial_interest_rate.text.toString(),
            edit_new_interest_rate.id to edit_new_interest_rate.text.toString(),
            switch_result.id to if(switch_result.isChecked) "on" else "off"
        )
    }

    override fun restoreInputs()
    {
        setEditTextFromInputs(edit_remaining_capital, inputs)
        setEditTextFromInputs(edit_remaining_duration, inputs)
        setEditTextFromInputs(edit_initial_interest_rate, inputs)
        setEditTextFromInputs(edit_new_interest_rate, inputs)

        switch_result?.isChecked = inputs.containsKey(switch_result?.id)
                && inputs[switch_result?.id] == "on"
    }
}
