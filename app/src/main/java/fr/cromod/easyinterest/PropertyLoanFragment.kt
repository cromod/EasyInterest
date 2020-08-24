package fr.cromod.easyinterest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.property_loan.*

class PropertyLoanFragment() : AbstractFragment() {

    companion object
    {
        private var instance: PropertyLoanFragment? = null

        fun newInstance(): PropertyLoanFragment?
        {
            if (instance == null) instance = PropertyLoanFragment()
            return instance
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.property_loan, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        listenTextChanged(edit_loan_amount)
        listenTextChanged(edit_loan_duration)
        listenTextChanged(edit_interest_rate)
    }

    override fun updateResult()
    {
        var monthlyPayment = Calculator.monthlyPayment(
            loanAmount = if(edit_loan_amount.text.isEmpty()) 0F else edit_loan_amount.text.toString().toFloat(),
            interestRate = if(edit_interest_rate.text.isEmpty()) 0F else edit_interest_rate.text.toString().toFloat(),
            nbOfMonths = if(edit_loan_duration.text.isEmpty()) 0F else edit_loan_duration.text.toString().toFloat() * 12
        )

        var loanCost = Calculator.loanCost(
            loanAmount = if(edit_loan_amount.text.isEmpty()) 0F else edit_loan_amount.text.toString().toFloat(),
            monthlyPayment = monthlyPayment,
            nbOfMonths = if(edit_loan_duration.text.isEmpty()) 0F else edit_loan_duration.text.toString().toFloat() * 12
        )

        monthlyPayment = Calculator.roundFloat(monthlyPayment)
        loanCost = Calculator.roundFloat(loanCost)

        result_monthly_payment.text = if (monthlyPayment.isFinite()) {
            beautifyNumber(monthlyPayment.toBigDecimal().toPlainString())
        } else ""

        result_loan_cost.text = if (loanCost.isFinite()) {
            beautifyNumber(loanCost.toBigDecimal().toPlainString())
        } else ""
    }

}
