package com.example.data.model

data class LanguageInfo(
    val code: String,
    val name: String,
    val nativeName: String,
    val flag: String,
    val greeting: String,
    val description: String,
    val totalLessons: Int = 12
)

data class VocabularyItem(
    val word: String,
    val transliteration: String = "",
    val translation: String,
    val exampleSentence: String = "",
    val exampleTranslation: String = "",
    val toneGuide: String = ""
)

data class ListeningExercise(
    val speechText: String,
    val questionPrompt: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class SpeakingPrompt(
    val targetPhrase: String,
    val transliteration: String = "",
    val translation: String,
    val phoneticTip: String
)

data class RoleplayTurn(
    val speaker: String,
    val message: String,
    val translation: String,
    val expectedReplyHint: String
)

data class ConversationRoleplay(
    val scenarioTitle: String,
    val situation: String,
    val partnerRole: String,
    val initialMessage: String,
    val initialTranslation: String,
    val sampleTurns: List<RoleplayTurn>
)

data class LanguageQuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class LanguageLesson(
    val id: String,
    val languageCode: String,
    val level: String, // Beginner, Intermediate, Advanced
    val title: String,
    val subtitle: String,
    val vocabulary: List<VocabularyItem>,
    val grammarRule: String,
    val grammarExamples: List<String>,
    val listening: ListeningExercise,
    val speaking: SpeakingPrompt,
    val roleplay: ConversationRoleplay,
    val quiz: List<LanguageQuizQuestion>
)
