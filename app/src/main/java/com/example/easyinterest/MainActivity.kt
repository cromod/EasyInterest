package com.example.easyinterest

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.math.*

class MainActivity(var inputs: MutableMap<EditText, String> = mutableMapOf(), var result: Float = 0F)
    : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputs[editStartCapital] = editStartCapital.text.toString()
        inputs[editAnnualGrowth] = editAnnualGrowth.text.toString()
        inputs[editNbOfYears] = editNbOfYears.text.toString()
        inputs[editSavings] = editSavings.text.toString()

        listenTextChanged(editStartCapital)
        listenTextChanged(editAnnualGrowth)
        listenTextChanged(editNbOfYears)
        listenTextChanged(editSavings)
    }

    private fun listenTextChanged(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                inputs[editText] = s.toString()
                calculateFinalCapital(
                    startCapital = if(inputs[editStartCapital]!!.isEmpty()) 0F else inputs[editStartCapital]!!.toFloat(),
                    growth = if(inputs[editAnnualGrowth]!!.isEmpty()) 0F else inputs[editAnnualGrowth]!!.toFloat(),
                    nbOfYears = if(inputs[editNbOfYears]!!.isEmpty()) 0 else inputs[editNbOfYears]!!.toInt(),
                    savings = if(inputs[editSavings]!!.isEmpty()) 0F else inputs[editSavings]!!.toFloat(),
                    monthly = !switchFrequency.isChecked
                )
                resultfinalCapital.setText(result.toString())
            }
            override fun afterTextChanged(arg0: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        })
    }

    fun onChangeFrequency(view: View) {
        // Update result
        calculateFinalCapital(
            startCapital = if(editStartCapital.text.isEmpty()) 0F else editStartCapital.text.toString().toFloat(),
            growth = if(editAnnualGrowth.text.isEmpty()) 0F else editAnnualGrowth.text.toString().toFloat(),
            nbOfYears = if(editNbOfYears.text.isEmpty()) 0 else editNbOfYears.text.toString().toInt(),
            savings = if(editSavings.text.isEmpty()) 0F else editSavings.text.toString().toFloat(),
            monthly = !switchFrequency.isChecked
        )
        resultfinalCapital.setText(result.toString())

        // Update label
        labelFrequency.setText(if(switchFrequency.isChecked) R.string.yearly else R.string.monthly)
    }

    fun calculateFinalCapital( startCapital: Float, growth: Float, nbOfYears: Int,
                               savings: Float, monthly: Boolean) {
        val factor: Float = 1 + growth / 100
        val period: Float = if(monthly) 12F else 1F
        result = startCapital*(factor.pow(nbOfYears))
        for (i in 1..nbOfYears) {
            result += period*savings*factor.pow(i-1)
        }
    }

}
