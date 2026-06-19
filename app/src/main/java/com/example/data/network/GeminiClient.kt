package com.example.data.network

import com.example.BuildConfig
import com.squareup.moshi.JsonClass
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    val contents: List<Content>,
    val systemInstruction: Content? = null,
    val generationConfig: GenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    val text: String
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    val temperature: Float? = null,
    val maxOutputTokens: Int? = null
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    val candidates: List<Candidate>?
)

@JsonClass(generateAdapter = true)
data class Candidate(
    val content: Content?
)

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object RetrofitClient {
    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://generativelanguage.googleapis.com/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    val geminiService: GeminiApiService by lazy {
        retrofit.create(GeminiApiService::class.java)
    }
}

class GeminiClient {
    suspend fun generateAdvisorAdvice(prompt: String, chatHistory: List<Pair<String, String>>, userAcademicContext: String): String {
        val apiKey = BuildConfig.GEMINI_API_KEY

        // Check if API key is invalid/placeholder representation
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey.startsWith("GEMINI_API_KEY")) {
            return "Note: Gemini API Key is missing. To activate real-time advisor chat, configure your GEMINI_API_KEY inside the Secrets Panel of the Google AI Studio UI.\n\n" +
                   "Offline Advisor recommendation:\n\n" +
                   "To reach your objective, focus on registering at least 15 credit hours next semester and scoring A or A- in core courses. Prioritize mathematical and programming courses since they tend to have heavier weightings, and consistently review your concepts."
        }

        val systemContext = "You are HEC Advisor Pro, an elite academic planning advisor for university students in Pakistan. " +
                "You understand HEC grading guidelines (e.g. A corresponds to 4.0, B to 3.0, etc.). " +
                "Respond with helpful, encouraging, and accurate study roadmaps, grading tips, and planning advice."

        // Construct interactive contents array: System context info first, then chat history
        val contentsList = mutableListOf<Content>()
        
        // Add user profile context as first helper
        contentsList.add(Content(listOf(Part(text = "Student Profile Context: $userAcademicContext"))))
        
        // Add chat history
        chatHistory.forEach { (sender, text) ->
            val senderPrefix = if (sender == "user") "Student: " else "Advisor: "
            contentsList.add(Content(listOf(Part(text = senderPrefix + text))))
        }
        
        // Add current prompt
        contentsList.add(Content(listOf(Part(text = "Student Query: $prompt"))))

        val request = GeminiRequest(
            contents = contentsList,
            systemInstruction = Content(listOf(Part(text = systemContext))),
            generationConfig = GenerationConfig(temperature = 0.7f, maxOutputTokens = 1000)
        )

        return try {
            val response = RetrofitClient.geminiService.generateContent(apiKey, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "I couldn't generate a recommendation. Please try asking again."
        } catch (e: Exception) {
            "An error occurred while communicating with the AI Academic-Advisor: ${e.message}\n\nCheck your internet connectivity and ensure your AI Studio key is configured correctly."
        }
    }
}
