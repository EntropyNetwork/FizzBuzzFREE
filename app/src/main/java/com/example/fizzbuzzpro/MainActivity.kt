package com.example.fizzbuzzpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.selects.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fizzBuzzButton: Button = findViewById(R.id.fizzBuzzButton)
        val fizzBuzzField: TextView = findViewById(R.id.fizzBuzzField)
        val autoFizzBuzzButton: Button = findViewById(R.id.autoFizzBuzzButton)
        var i = 1

        fizzBuzzButton.setOnClickListener {
            fizzBuzzFunction(fizzBuzzField, i)
            i = i + 1
        }

        autoFizzBuzzButton.setOnClickListener {
               GlobalScope.launch(Dispatchers.Main) {
                   try{
                       val text = "AUTO implemented with Kotlin Coroutines"
                       val length = Toast.LENGTH_LONG
                       Toast.makeText(applicationContext, text, length).show()
                       val side = Channel<Int>()
                       produceNumbers(side).consumeEach {
                           delay(100)
                           fizzBuzzField.text = fizzBuzz(it)
                       }
                   }catch (t: Throwable){

                   }
               }
        }
    }

    fun CoroutineScope.produceNumbers(side: SendChannel<Int>) = produce<Int> {
        for (num in 1..150) { // produce 10 numbers from 1 to 10
            select<Unit> {
                onSend(num) {} // Send to the primary channel
            }
        }
    }

    private fun fizzBuzzFunction(fizzBuzzField: TextView, i: Int) {
        runBlocking {
            val fizzBuzzStr: String = fizzBuzz(i)
            fizzBuzzField.text = fizzBuzzStr.subSequence(0, fizzBuzzStr.length)
        }
    }

    private fun fizzBuzz(i: Int):String{
        //short-circuit evaluate modular arithmetic logic
        if ((i % 3 == 0) && (i % 5 == 0)){
            return "FizzBuzz!"
        }else if ((i % 3 == 0)){
            return "Fizz"
        }else if ((i % 5 == 0)){
            return "Buzz"
        }else return i.toString()
    }
}