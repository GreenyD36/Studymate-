package com.example

import com.example.data.model.LanguageInfo
import com.example.data.model.LanguageLesson
import com.example.data.model.LanguageQuizQuestion
import com.example.data.model.VocabularyItem
import com.example.data.repository.LanguageCurriculum
import com.example.ui.ActiveQuizState
import com.example.ui.AppDestination
import com.example.ui.ChatMessage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Fast JVM unit tests for StudyMate AI core models, curriculum, and state logic.
 */
class ExampleUnitTest {

    @Test
    fun testLanguageCurriculumStructure() {
        val languages = LanguageCurriculum.languages
        assertEquals(7, languages.size)

        val codes = languages.map { it.code }
        val expectedCodes = listOf("en", "fr", "rw", "es", "ko", "zh", "pt")
        for (code in expectedCodes) {
            assertTrue("Expected curriculum to contain language code: $code", codes.contains(code))
        }

        // Verify each language contains authentic lessons, vocabulary, grammar, and conversation scenarios
        for (lang in languages) {
            val lessons = LanguageCurriculum.getLessonsForLanguage(lang.code)
            assertTrue("Language ${lang.name} should have at least 1 lesson", lessons.isNotEmpty())
            val firstLesson = lessons.first()
            assertTrue("Lesson ${firstLesson.title} should have vocabulary", firstLesson.vocabulary.isNotEmpty())
            assertTrue("Lesson ${firstLesson.title} should have grammar rule", firstLesson.grammarRule.isNotEmpty())
            assertTrue("Lesson ${firstLesson.title} should have listening exercises", firstLesson.listening.options.isNotEmpty())
            assertTrue("Lesson ${firstLesson.title} should have speaking prompt", firstLesson.speaking.targetPhrase.isNotEmpty())
            assertTrue("Lesson ${firstLesson.title} should have quiz questions", firstLesson.quiz.isNotEmpty())
            assertTrue("Lesson ${firstLesson.title} should have roleplay scenario", firstLesson.roleplay.scenarioTitle.isNotEmpty())
        }
    }

    @Test
    fun testKinyarwandaContentAuthenticity() {
        val kinyarwanda = LanguageCurriculum.languages.find { it.code == "rw" }
        assertNotNull("Kinyarwanda language curriculum must exist", kinyarwanda)
        assertEquals("Kinyarwanda", kinyarwanda!!.name)
        assertEquals("🇷🇼", kinyarwanda.flag)

        val lessons = LanguageCurriculum.getLessonsForLanguage("rw")
        val allVocab = lessons.flatMap { it.vocabulary }
        val phrases = allVocab.map { it.word }
        assertTrue("Should include 'Muraho' greeting", phrases.contains("Muraho"))
        assertTrue("Should include 'Murakoze' (Thank you)", phrases.contains("Murakoze"))
        assertTrue("Should include 'Amakuru'", phrases.contains("Amakuru"))
    }

    @Test
    fun testMandarinChineseContentWithPinyin() {
        val chinese = LanguageCurriculum.languages.find { it.code == "zh" }
        assertNotNull("Mandarin Chinese curriculum must exist", chinese)
        assertEquals("🇨🇳", chinese!!.flag)

        val lessons = LanguageCurriculum.getLessonsForLanguage("zh")
        val allVocab = lessons.flatMap { it.vocabulary }
        // Verify phonetic pronunciation (Pinyin / transliteration) is supplied for Chinese vocabulary
        for (item in allVocab) {
            assertTrue("Transliteration should not be empty for Chinese: ${item.word}", item.transliteration.isNotEmpty())
        }
    }

    @Test
    fun testActiveQuizStateScoring() {
        val questions = listOf(
            LanguageQuizQuestion("What is 2 + 2?", listOf("3", "4", "5", "6"), 1, "2 + 2 = 4"),
            LanguageQuizQuestion("What is 3 * 3?", listOf("6", "9", "12", "15"), 1, "3 * 3 = 9")
        )

        val quiz = ActiveQuizState(
            subject = "Math",
            topic = "Arithmetic",
            difficulty = "Beginner",
            questions = questions,
            currentIndex = 0,
            selectedOptionIndex = 1,
            isAnswerConfirmed = true,
            score = 1,
            userAnswers = listOf(1, null)
        )

        assertEquals(1, quiz.score)
        assertEquals(1, quiz.selectedOptionIndex)
        assertTrue(quiz.isAnswerConfirmed)
    }

    @Test
    fun testChatMessageIntegrity() {
        val userMsg = ChatMessage(sender = "user", text = "Can you help me solve 3x + 1 = 10?")
        val aiMsg = ChatMessage(sender = "studymate", text = "Certainly! Subtract 1 from both sides: 3x = 9, so x = 3.")

        assertEquals("user", userMsg.sender)
        assertEquals("studymate", aiMsg.sender)
        assertTrue(userMsg.timestamp > 0)
        assertNotNull(userMsg.id)
    }

    @Test
    fun testAppDestinationCoverage() {
        val destinations = AppDestination.values()
        assertTrue(destinations.contains(AppDestination.HOME))
        assertTrue(destinations.contains(AppDestination.SCANNER))
        assertTrue(destinations.contains(AppDestination.NOTES))
        assertTrue(destinations.contains(AppDestination.FLASHCARDS))
        assertTrue(destinations.contains(AppDestination.QUIZ))
        assertTrue(destinations.contains(AppDestination.AI_TUTOR))
        assertTrue(destinations.contains(AppDestination.LEARN))
        assertTrue(destinations.contains(AppDestination.STUDY_PLANNER))
        assertTrue(destinations.contains(AppDestination.PROGRESS))
        assertTrue(destinations.contains(AppDestination.PROFILE))
        assertTrue(destinations.contains(AppDestination.SUBSCRIPTION))
    }
}
