package com.mo.sh.studyassistant.data.network

import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.GenerativeModel
import com.google.firebase.ai.type.Content
import com.google.firebase.ai.type.generationConfig
import com.google.firebase.ai.type.HarmBlockThreshold
import com.google.firebase.ai.type.HarmCategory
import com.google.firebase.ai.type.SafetySetting
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.GenerativeBackend

class GeminiApi {
    suspend fun generateText(
        systemInstruction: String,
        prompt: String,
        key: String
    ): String? {
        val model = GeminiModelHelper.createGenerativeModel(key, systemInstruction)
        return model.generateContent(prompt).text
    }

    suspend fun generateMessage(
        systemInstruction: String,
        history: List<Content>,
        message: String,
        key: String
    ): String? {
        val model = GeminiModelHelper.createGenerativeModel(key, systemInstruction)
        val chat = model.startChat(history)
        return chat.sendMessage(message).text
    }
}

object GeminiModelHelper {

    private val safetySettings = listOf(
        SafetySetting(HarmCategory.HARASSMENT, HarmBlockThreshold.NONE),
        SafetySetting(HarmCategory.HATE_SPEECH, HarmBlockThreshold.NONE),
        SafetySetting(HarmCategory.DANGEROUS_CONTENT, HarmBlockThreshold.NONE),
        SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, HarmBlockThreshold.NONE),
    )

    private const val temp = 0.7f

    fun createGenerativeModel(
        apiKey: String,
        systemInstruction: String
    ): GenerativeModel {
        // Use gemini-3.1-flash-lite for higher quota/rate limits in 2026.
        val chosenModel = "gemini-3.1-flash-lite"

        // Note: Firebase AI SDK typically uses the API key from google-services.json.
        // If the user wants to use a manual key, we use GenerativeBackend.googleAI() 
        // but if the signature doesn't support a key, it must be configured in Firebase console.
        return Firebase.ai(backend = GenerativeBackend.googleAI()).generativeModel(
            modelName = chosenModel,
            generationConfig = generationConfig {
                temperature = temp
            },
            safetySettings = safetySettings,
            systemInstruction = content { text(systemInstruction) }
        )
    }
}
