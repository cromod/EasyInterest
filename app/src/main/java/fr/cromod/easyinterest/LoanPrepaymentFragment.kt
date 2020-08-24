package fr.cromod.easyinterest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.loan_prepayment.*

class LoanPrepaymentFragment() : AbstractFragment() {

    companion object
    {
        private var instance: LoanPrepaymentFragment? = null

        fun newInstance(): LoanPrepaymentFragment?
        {
            if (instance == null) instance = LoanPrepaymentFragment()
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
            label_prepayment_result.setText(if(switch_result.isChecked) R.string.remaining_months_after else R.string.monthly_payment)
        }
    }

    override fun updateResult()
    {
        // Initial values
        val initialRemainingDuration: Int = if(edit_remaining_duration.text.isEmpty()) 0 else edit_remaining_duration.text.toString().toInt()

        val initialMonthlyPayment = Calculator.monthlyPayment(
            loanAmount = if(edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString().toFloat(),
            interestRate = if(edit_interest_rate.text.isEmpty()) 0F else edit_interest_rate.text.toString().toFloat(),
            nbOfMonths = initialRemainingDuration.toFloat()
        )

        val prepayment: Float = if(edit_prepayment_amount.text.isEmpty()) 0F else edit_prepayment_amount.text.toString().toFloat()

        // Compute results according to the selected type
        var result: Float = 0F
        var gain: Float = 0F
        var loanCost: Float = 0F
        if (switch_result.isChecked)
        {
            result = Calculator.loanDuration(
                loanAmount = if(edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString().toFloat(),
                interestRate = if(edit_interest_rate.text.isEmpty()) 0F else edit_interest_rate.text.toString().toFloat(),
                monthlyPayment = initialMonthlyPayment,
                prepayment = prepayment
            )

            gain = initialRemainingDuration.toFloat() - result

            loanCost = Calculator.loanCost(
                loanAmount = if(edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString().toFloat(),
                monthlyPayment = initialMonthlyPayment,
                nbOfMonths = result,
                prepayment = prepayment
            )

            result = Calculator.roundFloat(result, 0)
            gain = Calculator.roundFloat(gain, 0)
        }
        else
        {
            result = Calculator.monthlyPayment(
                loanAmount = if(edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString().toFloat(),
                interestRate = if(edit_interest_rate.text.isEmpty()) 0F else edit_interest_rate.text.toString().toFloat(),
                nbOfMonths = initialRemainingDuration.toFloat(),
                prepayment = prepayment
            )

            gain = initialMonthlyPayment - result

            loanCost = Calculator.loanCost(
                loanAmount = if(edit_remaining_capital.text.isEmpty()) 0F else edit_remaining_capital.text.toString().toFloat(),
                monthlyPayment = result,
                nbOfMonths = initialRemainingDuration.toFloat(),
                prepayment = prepayment
            )

            result = Calculator.roundFloat(result)
            gain = Calculator.roundFloat(gain)
        }
        loanCost = Calculator.roundFloat(loanCost)

        // Display results
        prepayment_result.text = if (result.isFinite()) {
            beautifyNumber(result.toBigDecimal().toPlainString())
        } else ""

        prepayment_gain.text = if (gain.isFinite()) {
            beautifyNumber(gain.toBigDecimal().toPlainString())
        } else ""

        result_loan_cost.text = if (loanCost.isFinite()) {
            beautifyNumber(loanCost.toBigDecimal().toPlainString())
        } else ""
    }

}
