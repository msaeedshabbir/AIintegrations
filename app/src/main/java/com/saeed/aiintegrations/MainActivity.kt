package com.saeed.aiintegrations

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GoogleGenerativeAIException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val apiKey = "AIzaSyC2Y66hMsgsvreun4KxewvINf3YfzZ2X24"
    private val modelName = "gemini-1.5-flash"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val eTprompt = findViewById<EditText>(R.id.eTprompt)
        val btnsubmit = findViewById<Button>(R.id.btnsubmit)
        val eTresponse = findViewById<TextView>(R.id.eTresponse)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        btnsubmit.setOnClickListener {
            val prompt = eTprompt.text.toString()
            if (prompt.isNotBlank()) {
                progressBar.visibility = ProgressBar.VISIBLE
                generateContent(prompt, eTresponse, progressBar)
            } else {
                eTresponse.text = "Please enter a prompt."
            }
        }
    }

    private fun generateContent(prompt: String, eTresponse: TextView, progressBar: ProgressBar) {
        val generativeModel = GenerativeModel(
            modelName = modelName,
            apiKey = apiKey
        )

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    generativeModel.generateContent(prompt)
                }
                eTresponse.text = response.text
            } catch (e: GoogleGenerativeAIException) {
                Log.e("MainActivity", "Error generating content", e)
                eTresponse.text = "Error: ${e.message}"
            } catch (e: Exception) {
                Log.e("MainActivity", "Unexpected error", e)
                eTresponse.text = "Unexpected error occurred. Please try again."
            } finally {
                progressBar.visibility = ProgressBar.GONE
            }
        }
    }
}
