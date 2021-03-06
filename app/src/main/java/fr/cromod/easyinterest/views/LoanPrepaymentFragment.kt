package fr.cromod.easyinterest.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.cromod.easyinterest.utils.Calculator
import fr.cromod.easyinterest.R
import kotlinx.android.synthetic.main.loan_prepayment.*

class LoanPrepaymentFragment() : AbstractFragment() {

    // Intermediate inputs for calculation
    var initialRemainingDuration: Float = 0F
    var initialMonthlyPayment: Float = 0F
    var initialLoanCost: Float = 0F
    var prepayment: Float = 0F

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
                instance = LoanPrepaymentFragment()
            }
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.loan_prepayment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        listenTextChanged(edit_remaining_capital)
        listenTextChanged(edit_remaining_duration)
        listenTextChanged(edit_interest_rate)
        listenTextChanged(edit_prepayment_amount)

        listenResultTypeChanged()
    }

    private fun listenResultTypeChanged()
    {
        switch_result.setOnClickListener {
            updateResult()
            label_prepayment_result.setText(if(switch_result.isChecked) R.string.remaining_months_after_prepayment else R.string.new_monthly_payment)
        }
    }

    override fun updateResult()
    {
        initializeUpdating()

        if (switch_result.isChecked)
        {
            updateDuration(initialMonthlyPayment, initialRemainingDuration, initialLoanCost, prepayment)
        }
        else
        {
            updateMonthlyPayment(initialMonthlyPayment, initialRemainingDuration, initialLoanCost, prepayment)
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
                interestRate = if (edit_interest_rate.text.isEmpty()) 0F else edit_interest_rate.text.toString()
                    .toFloat(),
                nbOfMonths = initialRemainingDuration
            )

        initialLoanCost = Calculator.loanCost(
            loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                .toFloat(),
            monthlyPayment = initialMonthlyPayment,
            nbOfMonths = initialRemainingDuration
        )

        prepayment = if(edit_prepayment_amount.text.isEmpty()) 0F else edit_prepayment_amount.text.toString().toFloat()
    }

    private fun updateDuration(initialMonthlyPayment: Float, initialRemainingDuration: Float, initialLoanCost: Float, prepayment: Float)
    {
        result = Calculator.loanDuration(
            loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                .toFloat(),
            interestRate = if (edit_interest_rate.text.isEmpty()) 0F else edit_interest_rate.text.toString()
                .toFloat(),
            monthlyPayment = initialMonthlyPayment,
            prepayment = prepayment
        )
        gain = initialRemainingDuration - result

        loanCost = Calculator.loanCost(
            loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                .toFloat(),
            monthlyPayment = initialMonthlyPayment,
            nbOfMonths = result,
            prepayment = prepayment
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

    private fun updateMonthlyPayment(initialMonthlyPayment: Float, initialRemainingDuration: Float,  initialLoanCost: Float, prepayment: Float)
    {
        result = Calculator.monthlyPayment(
            loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                .toFloat(),
            interestRate = if (edit_interest_rate.text.isEmpty()) 0F else edit_interest_rate.text.toString()
                .toFloat(),
            nbOfMonths = initialRemainingDuration,
            prepayment = prepayment
        )
        gain = initialMonthlyPayment - result

        loanCost = Calculator.loanCost(
            loanAmount = if (edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString()
                .toFloat(),
            monthlyPayment = result,
            nbOfMonths = initialRemainingDuration,
            prepayment = prepayment
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
        prepayment_result.text = if (result.isFinite()) {
            beautifyNumber(result.toBigDecimal().toPlainString())
        } else ""

        prepayment_gain.text = if (gain.isFinite()) {
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
            edit_interest_rate.id to edit_interest_rate.text.toString(),
            edit_prepayment_amount.id to edit_prepayment_amount.text.toString(),
            switch_result.id to if(switch_result.isChecked) "on" else "off"
        )
    }

    override fun restoreInputs()
    {
        setEditTextFromInputs(edit_remaining_capital, inputs)
        setEditTextFromInputs(edit_remaining_duration, inputs)
        setEditTextFromInputs(edit_interest_rate, inputs)
        setEditTextFromInputs(edit_prepayment_amount, inputs)

        switch_result?.isChecked = inputs.containsKey(switch_result?.id)
                && inputs[switch_result?.id] == "on"
    }
}
