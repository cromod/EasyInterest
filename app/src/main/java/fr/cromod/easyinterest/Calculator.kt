package fr.cromod.easyinterest

import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.round

abstract class Calculator {

    companion object
    {
        fun roundFloat(result: Float, afterDecimal: Int = 2): Float
        {
            return round(result*(10F.pow(afterDecimal)))/10F.pow(afterDecimal)
        }

        fun finalCapital( startCapital: Float, growth: Float, nbOfYears: Int,
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
            return result
        }

        fun monthlyPayment( loanAmount: Float, interestRate: Float, nbOfMonths: Int, prepayment: Float = 0F ): Float
        {
            val factor: Float = interestRate / (100 * 12)
            val denominator: Float = 1 - 1 / (1+factor).pow(nbOfMonths)
            return (loanAmount-prepayment) * factor / denominator
        }

        fun loanDuration( loanAmount: Float, interestRate: Float, monthlyPayment: Float, prepayment: Float = 0F ): Float
        {
            val factor: Float = interestRate / (100 * 12)
            val numerator: Float = ln(1 - (loanAmount-prepayment) * factor / monthlyPayment)
            val denominator: Float = ln(1 + factor)
            return -numerator/denominator
        }

        fun loanCost(loanAmount: Float, monthlyPayment: Float, nbOfMonths: Float, prepayment: Float = 0F): Float
        {
            return monthlyPayment * nbOfMonths - loanAmount - prepayment
        }
    }
}
