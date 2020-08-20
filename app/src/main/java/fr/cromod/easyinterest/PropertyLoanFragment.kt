package fr.cromod.easyinterest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.property_loan.*
import kotlin.math.pow
import kotlin.math.round

class PropertyLoanFragment() : AbstractFragment() {

    companion object
    {
        private var instance: PropertyLoanFragment? = null

        fun newInstance(): PropertyLoanFragment?
        {
            if (instance == null) instance = PropertyLoanFragment()
            return instance
        }

        fun calculateMonthlyPayment( loanAmount: Float, interestRate: Float, nbOfYears: Int): Float
        {
            val factor: Float = interestRate / (100 * 12)
            val denominator: Float = 1 - 1 / (1+factor).pow(nbOfYears*12)
            return loanAmount * factor / denominator
        }

        fun calculateLoanCost(loanAmount: Float, monthlyPayment: Float, nbOfYears: Int): Float
        {
            return monthlyPayment * 12 * nbOfYears - loanAmount
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
        var monthlyPayment = calculateMonthlyPayment(
            loanAmount = if(edit_loan_amount.text.isEmpty()) 0F else edit_loan_amount.text.toString().toFloat(),
            interestRate = if(edit_interest_rate.text.isEmpty()) 0F else edit_interest_rate.text.toString().toFloat(),
            nbOfYears = if(edit_loan_duration.text.isEmpty()) 0 else edit_loan_duration.text.toString().toInt()
        )

        var loanCost = calculateLoanCost(
            loanAmount = if(edit_loan_amount.text.isEmpty()) 0F else edit_loan_amount.text.toString().toFloat(),
            monthlyPayment = monthlyPayment,
            nbOfYears = if(edit_loan_duration.text.isEmpty()) 0 else edit_loan_duration.text.toString().toInt()
        )

        if (monthlyPayment.isFinite())
        {
            monthlyPayment = round(monthlyPayment * 100) / 100
            result_monthly_payment.setText(monthlyPayment.toBigDecimal().toPlainString())
        }
        else
        {
            result_monthly_payment.setText("")
        }

        if (loanCost.isFinite())
        {
            loanCost = round(loanCost * 100) / 100
            result_loan_cost.setText(loanCost.toBigDecimal().toPlainString())
        }
        else
        {
            result_loan_cost.setText("")
        }
    }

}
