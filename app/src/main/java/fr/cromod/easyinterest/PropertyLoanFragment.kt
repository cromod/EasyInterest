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
import kotlinx.android.synthetic.main.property_loan.*
import kotlin.math.*

class PropertyLoanFragment() : Fragment() {

    private lateinit var parentActivity: Activity

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

    override fun onAttach(context: Context)
    {
        super.onAttach(context)
        if (context is Activity) {
            this.parentActivity = context
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

    private fun listenTextChanged(editText: EditText)
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

    private fun updateResult()
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
            result_monthly_payment.setText(monthlyPayment.toString())
        }
        else
        {
            result_monthly_payment.setText("")
        }

        if (loanCost.isFinite())
        {
            loanCost = round(loanCost * 100) / 100
            result_loan_cost.setText(loanCost.toString())
        }
        else
        {
            result_loan_cost.setText("")
        }
    }

}
