package com.example.data.repository

import com.example.data.model.ConversationRoleplay
import com.example.data.model.LanguageInfo
import com.example.data.model.LanguageLesson
import com.example.data.model.LanguageQuizQuestion
import com.example.data.model.ListeningExercise
import com.example.data.model.RoleplayTurn
import com.example.data.model.SpeakingPrompt
import com.example.data.model.VocabularyItem

object LanguageCurriculum {

    val languages = listOf(
        LanguageInfo(
            code = "en",
            name = "English",
            nativeName = "English",
            flag = "🇬🇧",
            greeting = "Hello! How are you?",
            description = "Academic vocabulary, university discussions, and everyday fluency."
        ),
        LanguageInfo(
            code = "fr",
            name = "French",
            nativeName = "Français",
            flag = "🇫🇷",
            greeting = "Bonjour! Comment allez-vous?",
            description = "Grammar fundamentals, conversational elegance, and Francophone culture."
        ),
        LanguageInfo(
            code = "rw",
            name = "Kinyarwanda",
            nativeName = "Ikinyarwanda",
            flag = "🇷🇼",
            greeting = "Muraho! Amakuru ki?",
            description = "Comprehensive Rwandan language, cultural etiquette, noun classes, and everyday conversations."
        ),
        LanguageInfo(
            code = "es",
            name = "Spanish",
            nativeName = "Español",
            flag = "🇪🇸",
            greeting = "¡Hola! ¿Cómo estás?",
            description = "High-frequency vocabulary, verb conjugations, and global Hispanic dialogue."
        ),
        LanguageInfo(
            code = "ko",
            name = "Korean",
            nativeName = "한국어",
            flag = "🇰🇷",
            greeting = "안녕하세요! 반갑습니다.",
            description = "Hangul syllables, honorific levels, daily conversations, and natural idioms."
        ),
        LanguageInfo(
            code = "zh",
            name = "Chinese (Mandarin)",
            nativeName = "普通话 / 中文",
            flag = "🇨🇳",
            greeting = "你好！很高兴认识你。",
            description = "Simplified Chinese characters, Pinyin system, four tones, and practical communication."
        ),
        LanguageInfo(
            code = "pt",
            name = "Portuguese",
            nativeName = "Português",
            flag = "🇵🇹",
            greeting = "Olá! Como você está?",
            description = "Pronunciation nuance, verb moods, and Brazilian & European Portuguese vocabulary."
        )
    )

    fun getLessonsForLanguage(code: String): List<LanguageLesson> {
        return when (code) {
            "rw" -> kinyarwandaLessons
            "zh" -> chineseLessons
            "ko" -> koreanLessons
            "fr" -> frenchLessons
            "es" -> spanishLessons
            "pt" -> portugueseLessons
            else -> englishLessons
        }
    }

    private val kinyarwandaLessons = listOf(
        LanguageLesson(
            id = "rw_1",
            languageCode = "rw",
            level = "Beginner",
            title = "Greetings & Polite Etiquette",
            subtitle = "Kwakira abantu & Indangagaciro",
            vocabulary = listOf(
                VocabularyItem("Muraho", "Moo-rah-ho", "Hello / Greetings", "Muraho neza, amakuru?", "Hello, how are you?"),
                VocabularyItem("Murakoze", "Moo-rah-koh-zeh", "Thank you", "Murakoze cyane!", "Thank you very much!"),
                VocabularyItem("Amakuru", "Ah-mah-koo-roo", "What is the news? / How are you?", "Amakuru yawe?", "How are you doing?"),
                VocabularyItem("Ni meza", "Nee meh-zah", "I am well / It is good", "Ni meza cyane.", "I am very well."),
                VocabularyItem("Mwaramutse", "Mwah-rah-moo-tseh", "Good morning", "Mwaramutse neza.", "Good morning to you."),
                VocabularyItem("Mwiriwe", "Mwee-ree-weh", "Good afternoon / Good evening", "Mwiriwe neza.", "Good evening to you.")
            ),
            grammarRule = "In Kinyarwanda, greetings reflect social respect. 'Muraho' is general, while 'Mwaramutse' (morning) and 'Mwiriwe' (after midday) use the plural honorific prefix 'Mu-'. When someone asks 'Amakuru?' (news?), standard replies are 'Ni meza' (good) or 'Ni meza cyane' (very good).",
            grammarExamples = listOf(
                "Amakuru? → Ni meza. (How are you? → I am fine.)",
                "Mwaramutse ho? → Mwaramutse neza. (Good morning → Good morning to you.)",
                "Murakoze cyane. (Thank you very much.)"
            ),
            listening = ListeningExercise(
                speechText = "Muraho, amakuru yawe? Ni meza cyane.",
                questionPrompt = "What did the speaker respond to the question 'Amakuru yawe?'",
                options = listOf("They are very well (Ni meza cyane)", "They said good morning", "They asked for directions", "They declined the greeting"),
                correctIndex = 0,
                explanation = "'Ni meza cyane' translates directly to 'Very good' or 'I am very well'."
            ),
            speaking = SpeakingPrompt(
                targetPhrase = "Murakoze cyane",
                transliteration = "Moo-rah-koh-zeh chyah-neh",
                translation = "Thank you very much",
                phoneticTip = "Pronounce 'cyane' with a soft 'chya' sound and light stress on the second syllable."
            ),
            roleplay = ConversationRoleplay(
                scenarioTitle = "Meeting a Friend in Kigali",
                situation = "You meet your study partner, Keza, outside Kigali Public Library.",
                partnerRole = "Keza (Study Partner)",
                initialMessage = "Muraho! Amakuru ki ya mugitondo?",
                initialTranslation = "Hello! What is the news of this morning?",
                sampleTurns = listOf(
                    RoleplayTurn("You", "Ni meza cyane! Wowe se amakuru?", "I am very well! How about you?", "Say 'Ni meza cyane!' and ask about Keza."),
                    RoleplayTurn("Keza", "Ni meza! Witeguye kwiga uyu munsi?", "Good! Are you ready to study today?", "Reply 'Yego' (Yes) and say you brought your notes.")
                )
            ),
            quiz = listOf(
                LanguageQuizQuestion("How do you greet someone in the morning in Kinyarwanda?", listOf("Mwaramutse", "Mwiriwe", "Ijoro ryiza", "Urugendo rwiza"), 0, "'Mwaramutse' is used in the morning until midday."),
                LanguageQuizQuestion("What is the polite translation of 'Thank you'?", listOf("Oya", "Murakoze", "Yego", "Bite"), 1, "'Murakoze' means thank you.")
            )
        ),
        LanguageLesson(
            id = "rw_2",
            languageCode = "rw",
            level = "Intermediate",
            title = "Academic Studies & Books",
            subtitle = "Amasomo no Gusoma",
            vocabulary = listOf(
                VocabularyItem("Igitabo", "Ee-gee-tah-boh", "Book", "Iki gitabo ni cyiza.", "This book is great."),
                VocabularyItem("Ishuri", "Ee-shoo-ree", "School / University", "Ndi ku ishuri.", "I am at school."),
                VocabularyItem("Umwarimu", "Oom-wah-ree-moo", "Teacher / Professor", "Umwarimu ari gusobanura.", "The teacher is explaining."),
                VocabularyItem("Kwiga", "Kwee-gah", "To study / To learn", "Ndashaka kwiga ubu.", "I want to study now."),
                VocabularyItem("Ikizamini", "Ee-kee-zah-mee-nee", "Exam / Test", "Mfite ikizamini ejo.", "I have an exam tomorrow.")
            ),
            grammarRule = "Verb conjugation with 'Nda-' (Present affirmative for first person 'I'): Ndashaka (I want), Ndiga (I study), Ndasoma (I read). Noun classes: Igitabo (singular, class 7) becomes Ibitabo (plural, class 8).",
            grammarExamples = listOf(
                "Ndashaka gusoma igitabo. (I want to read a book.)",
                "Ibitabo byanjye biri ku meza. (My books are on the table.)"
            ),
            listening = ListeningExercise(
                speechText = "Mfite ikizamini cy'imibare ejo ku ishuri.",
                questionPrompt = "When is the speaker taking their mathematics exam?",
                options = listOf("Tomorrow (Ejo)", "Next month", "Today", "Yesterday"),
                correctIndex = 0,
                explanation = "'Ejo' means tomorrow (or yesterday depending on tense; here 'mfite' indicates upcoming tomorrow)."
            ),
            speaking = SpeakingPrompt(
                targetPhrase = "Ndashaka kwiga neza",
                transliteration = "N-dah-shah-kah kwee-gah neh-zah",
                translation = "I want to study well",
                phoneticTip = "Keep the vowel in 'kwi-ga' clean and bright."
            ),
            roleplay = ConversationRoleplay(
                scenarioTitle = "Forming a Study Group",
                situation = "Discussing exam prep with Gatete in the university cafeteria.",
                partnerRole = "Gatete",
                initialMessage = "Wasoje gusubiramo amasomo y'ikizamini?",
                initialTranslation = "Did you finish reviewing the exam materials?",
                sampleTurns = listOf(
                    RoleplayTurn("You", "Hafi ya byose! Dukeneye kwitoza ibibazo.", "Almost all! We need to practice questions.", "Confirm you are reviewing and want to solve quiz problems.")
                )
            ),
            quiz = listOf(
                LanguageQuizQuestion("What does 'Igitabo' mean?", listOf("Pen", "Book", "Desk", "Lesson"), 1, "'Igitabo' is the Kinyarwanda word for book.")
            )
        )
    )

    private val chineseLessons = listOf(
        LanguageLesson(
            id = "zh_1",
            languageCode = "zh",
            level = "Beginner",
            title = "Pinyin, Tones & Essential Greetings",
            subtitle = "拼音、声调与基础问候",
            vocabulary = listOf(
                VocabularyItem("你好", "Nǐ hǎo", "Hello", "你好！我是学生。", "Hello! I am a student.", "3rd tone + 3rd tone → 2nd + 3rd"),
                VocabularyItem("谢谢", "Xièxiè", "Thank you", "谢谢你的帮助！", "Thank you for your help!", "4th tone + neutral tone"),
                VocabularyItem("不客气", "Bú kèqi", "You're welcome", "不用谢，不客气。", "No need to thank, you're welcome.", "2nd tone + 4th + neutral"),
                VocabularyItem("再见", "Zàijiàn", "Goodbye", "明天见，再见！", "See you tomorrow, goodbye!", "4th tone + 4th tone"),
                VocabularyItem("对不起", "Duìbuqǐ", "Sorry", "对不起，我迟到了。", "Sorry, I am late.", "4th + neutral + 3rd"),
                VocabularyItem("没关系", "Méiguānxi", "It doesn't matter / No problem", "没关系，别担心。", "It's fine, don't worry.", "2nd + 1st + neutral")
            ),
            grammarRule = "Mandarin Chinese has 4 main tones: 1st (high flat: ā), 2nd (rising: á), 3rd (dipping: ǎ), 4th (falling: à). Tone Sandhi: When two 3rd tones appear consecutively (like Nǐ hǎo), the first becomes a 2nd tone (Ní hǎo).",
            grammarExamples = listOf(
                "Nǐ hǎo! (你好！) - Hello!",
                "Xièxiè nǐ! (谢谢你！) - Thank you!",
                "Duìbuqǐ! → Méiguānxi! (对不起！→ 没关系！) - Sorry! → It's okay!"
            ),
            listening = ListeningExercise(
                speechText = "你好！很高兴认识你。谢谢你的帮助。",
                questionPrompt = "What courteous expression did the speaker use at the end?",
                options = listOf("Thank you (Xièxiè)", "Goodbye (Zàijiàn)", "Sorry (Duìbuqǐ)", "Excuse me"),
                correctIndex = 0,
                explanation = "'Xièxiè nǐ de bāngzhù' means 'Thank you for your help'."
            ),
            speaking = SpeakingPrompt(
                targetPhrase = "你好，谢谢！",
                transliteration = "Nǐ hǎo, xièxie!",
                translation = "Hello, thank you!",
                phoneticTip = "Dip your pitch downward and back up on Nǐ, then drop sharply on Xiè."
            ),
            roleplay = ConversationRoleplay(
                scenarioTitle = "Meeting a Classmate in Beijing",
                situation = "First day in Chinese language exchange class.",
                partnerRole = "Li Ming (李明)",
                initialMessage = "你好！我是李明，很高兴认识你！",
                initialTranslation = "Hello! I am Li Ming, very pleased to meet you!",
                sampleTurns = listOf(
                    RoleplayTurn("You", "你好李明！我也很高兴认识你。", "Hello Li Ming! I am also very glad to meet you.", "Greet Li Ming and say you're pleased to meet him.")
                )
            ),
            quiz = listOf(
                LanguageQuizQuestion("What tone is the character '谢' (Xiè)?", listOf("First (high flat)", "Second (rising)", "Third (dipping)", "Fourth (sharp falling)"), 3, "'Xiè' is 4th tone (falling)."),
                LanguageQuizQuestion("What is the Pinyin for 你好?", listOf("Nǐ hǎo", "Nǐ men", "Xiè xiè", "Zài jiàn"), 0, "你好 is romanized as Nǐ hǎo.")
            )
        )
    )

    private val koreanLessons = listOf(
        LanguageLesson(
            id = "ko_1",
            languageCode = "ko",
            level = "Beginner",
            title = "Hangul Syllables & Polite Greetings",
            subtitle = "한글 음절과 기본 인사",
            vocabulary = listOf(
                VocabularyItem("안녕하세요", "Annyeonghaseyo", "Hello / Good day (Polite)", "안녕하세요! 저는 학생입니다.", "Hello! I am a student."),
                VocabularyItem("감사합니다", "Gamsahamnida", "Thank you (Formal)", "도와주셔서 감사합니다.", "Thank you for helping me."),
                VocabularyItem("죄송합니다", "Joesonghamnida", "I'm sorry (Formal)", "늦어서 죄송합니다.", "I am sorry for being late."),
                VocabularyItem("네", "Ne", "Yes", "네, 맞아요.", "Yes, that is correct."),
                VocabularyItem("아니요", "Aniyo", "No", "아니요, 괜찮습니다.", "No, it is okay."),
                VocabularyItem("안녕히 계세요", "Annyeonghi gyeseyo", "Goodbye (To someone staying)", "저는 먼저 갑니다. 안녕히 계세요.", "I am leaving first. Goodbye.")
            ),
            grammarRule = "Korean sentences follow Subject-Object-Verb (SOV) order. The polite verb ending '-요' (-yo) and formal '-습니다' (-seumnida) show respect to your listener.",
            grammarExamples = listOf(
                "저는 학생입니다. (I am a student.)",
                "한국어를 공부합니다. (I study Korean - Object precedes Verb)."
            ),
            listening = ListeningExercise(
                speechText = "안녕하세요! 오늘 만나서 반갑습니다. 감사합니다.",
                questionPrompt = "How did the speaker express gratitude at the end?",
                options = listOf("감사합니다 (Gamsahamnida)", "죄송합니다 (Joesonghamnida)", "안녕히 가세요 (Annyeonghi gaseyo)", "네 (Ne)"),
                correctIndex = 0,
                explanation = "'감사합니다' is the polite formal expression for thank you."
            ),
            speaking = SpeakingPrompt(
                targetPhrase = "안녕하세요, 반갑습니다!",
                transliteration = "Annyeonghaseyo, bangabseumnida!",
                translation = "Hello, nice to meet you!",
                phoneticTip = "Soften the 'h' in 'haseyo' and pronounce 'seumnida' smoothly without harsh stops."
            ),
            roleplay = ConversationRoleplay(
                scenarioTitle = "First Day at Seoul National University",
                situation = "Meeting Minji in the campus library cafe.",
                partnerRole = "Minji (민지)",
                initialMessage = "안녕하세요! 처음 뵙겠습니다. 이름이 뭐예요?",
                initialTranslation = "Hello! Nice to meet you for the first time. What is your name?",
                sampleTurns = listOf(
                    RoleplayTurn("You", "안녕하세요! 제 이름은 Danos입니다. 만나서 반가워요.", "Hello! My name is Danos. Nice to meet you.", "Say hello, share your name, and express delight.")
                )
            ),
            quiz = listOf(
                LanguageQuizQuestion("What is the polite Korean greeting for 'Hello'?", listOf("안녕하세요", "감사합니다", "아니요", "실례합니다"), 0, "'안녕하세요' (Annyeonghaseyo) is the universal polite greeting."),
                LanguageQuizQuestion("What is the word order of Korean sentences?", listOf("Subject-Verb-Object (SVO)", "Subject-Object-Verb (SOV)", "Verb-Subject-Object (VSO)", "Object-Verb-Subject"), 1, "Korean is an SOV language.")
            )
        )
    )

    private val frenchLessons = listOf(
        LanguageLesson(
            id = "fr_1",
            languageCode = "fr",
            level = "Beginner",
            title = "Salutations & Everyday Courtesies",
            subtitle = "Les salutations et la politesse",
            vocabulary = listOf(
                VocabularyItem("Bonjour", "bon-zhoor", "Hello / Good morning", "Bonjour, comment allez-vous?", "Hello, how are you?"),
                VocabularyItem("Merci beaucoup", "mair-see boh-koo", "Thank you very much", "Merci beaucoup pour vos conseils.", "Thank you very much for your advice."),
                VocabularyItem("S'il vous plaît", "seel voo pleh", "Please (Formal)", "Un café, s'il vous plaît.", "A coffee, please."),
                VocabularyItem("Au revoir", "oh ruh-vwahr", "Goodbye", "Bonne journée et au revoir!", "Have a nice day and goodbye!"),
                VocabularyItem("Enchanté", "ahn-shahn-tay", "Nice to meet you", "Enchanté de faire votre connaissance.", "Pleased to make your acquaintance.")
            ),
            grammarRule = "Formal vs Informal: Use 'vous' for professors, strangers, or superiors, and 'tu' with close friends and fellow classmates. Grammatical gender: all French nouns are masculine ('le/un') or feminine ('la/une').",
            grammarExamples = listOf(
                "Comment allez-vous ? (Formal: How are you?)",
                "Comment vas-tu ? (Informal: How are you?)"
            ),
            listening = ListeningExercise(
                speechText = "Bonjour madame, enchanté de faire votre connaissance. Merci beaucoup pour votre temps.",
                questionPrompt = "What was the tone and register of this conversation?",
                options = listOf("Formal and polite (using madame and votre)", "Slang and casual", "Angry and demanding", "A bedtime story"),
                correctIndex = 0,
                explanation = "The speaker used formal vocabulary: 'madame', 'enchanté', and 'votre temps'."
            ),
            speaking = SpeakingPrompt(
                targetPhrase = "Bonjour, merci beaucoup!",
                transliteration = "bon-zhoor, mair-see boh-koo",
                translation = "Hello, thank you very much!",
                phoneticTip = "Pronounce the nasal 'on' in 'Bonjour' without sounding out a hard 'n'."
            ),
            roleplay = ConversationRoleplay(
                scenarioTitle = "Ordering at a Paris Bistro",
                situation = "You sit at a cafe terrace near the Sorbonne university.",
                partnerRole = "Le Serveur (Waiter)",
                initialMessage = "Bonjour ! Que désirez-vous prendre aujourd'hui ?",
                initialTranslation = "Hello! What would you like to have today?",
                sampleTurns = listOf(
                    RoleplayTurn("You", "Bonjour, je voudrais un croissant et un thé, s'il vous plaît.", "Hello, I would like a croissant and a tea, please.", "Order politely using 'Je voudrais' and 's'il vous plaît'.")
                )
            ),
            quiz = listOf(
                LanguageQuizQuestion("Which pronoun is used for formal address in French?", listOf("Tu", "Vous", "Ils", "On"), 1, "'Vous' is the formal second-person pronoun.")
            )
        )
    )

    private val spanishLessons = listOf(
        LanguageLesson(
            id = "es_1",
            languageCode = "es",
            level = "Beginner",
            title = "Greetings & First Introductions",
            subtitle = "Saludos y Presentaciones",
            vocabulary = listOf(
                VocabularyItem("¡Hola!", "oh-lah", "Hello", "¡Hola! ¿Cómo te llamas?", "Hello! What is your name?"),
                VocabularyItem("Gracias", "grah-syahs", "Thank you", "Muchas gracias por tu ayuda.", "Thank you very much for your help."),
                VocabularyItem("Por favor", "por fah-vor", "Please", "Pásame el libro, por favor.", "Pass me the book, please."),
                VocabularyItem("Mucho gusto", "moo-choh goos-toh", "Nice to meet you", "Mucho gusto en conocerte.", "Pleasure to meet you."),
                VocabularyItem("Hasta luego", "ahs-tah lweh-goh", "See you later", "Nos vemos mañana, hasta luego.", "See you tomorrow, see you later.")
            ),
            grammarRule = "In Spanish, punctuation includes inverted question marks (¿) and exclamation points (¡) at the beginning of clauses. Subject pronouns (yo, tú) are often omitted because verb endings specify the person.",
            grammarExamples = listOf(
                "¿Cómo estás? (How are you? - subject 'tú' implied)",
                "Estoy muy bien, gracias. (I am very well, thanks.)"
            ),
            listening = ListeningExercise(
                speechText = "¡Hola! Buenas tardes. Estoy muy contento de estudiar contigo hoy.",
                questionPrompt = "What time of day did the speaker greet their companion?",
                options = listOf("Afternoon (Buenas tardes)", "Morning (Buenos días)", "Midnight", "Dawn"),
                correctIndex = 0,
                explanation = "'Buenas tardes' is used from noon until evening."
            ),
            speaking = SpeakingPrompt(
                targetPhrase = "¡Hola! Mucho gusto.",
                transliteration = "Oh-lah, moo-choh goos-toh",
                translation = "Hello! Nice to meet you.",
                phoneticTip = "Remember the 'H' in 'Hola' is completely silent."
            ),
            roleplay = ConversationRoleplay(
                scenarioTitle = "Study Partner in Madrid",
                situation = "Meeting Carlos at the student center.",
                partnerRole = "Carlos",
                initialMessage = "¡Hola! Bienvenido al grupo de estudio. ¿Cómo estás?",
                initialTranslation = "Hello! Welcome to the study group. How are you?",
                sampleTurns = listOf(
                    RoleplayTurn("You", "¡Hola Carlos! Estoy genial, gracias. ¿Listo para repasar?", "Hello Carlos! I am great, thanks. Ready to review?", "Say you are great and eager to study.")
                )
            ),
            quiz = listOf(
                LanguageQuizQuestion("What does 'Hasta luego' mean?", listOf("Good morning", "See you later", "Please", "Thank you"), 1, "'Hasta luego' means see you later.")
            )
        )
    )

    private val portugueseLessons = listOf(
        LanguageLesson(
            id = "pt_1",
            languageCode = "pt",
            level = "Beginner",
            title = "Saudações & Boas Maneiras",
            subtitle = "Greetings & Polite Phrases",
            vocabulary = listOf(
                VocabularyItem("Olá / Oi", "oh-lah / oy", "Hello / Hi", "Olá, tudo bem com você?", "Hello, is everything good with you?"),
                VocabularyItem("Obrigado / Obrigada", "oh-bree-gah-doo / dah", "Thank you", "Muito obrigado pela sua atenção!", "Thank you very much for your attention!"),
                VocabularyItem("Por favor", "poor fah-voor", "Please", "Você pode me ajudar, por favor?", "Can you help me, please?"),
                VocabularyItem("Prazer em conhecer", "prah-zair aing kohn-yee-sair", "Pleasure to meet you", "Muito prazer em conhecê-lo.", "Great pleasure to meet you."),
                VocabularyItem("Tchau", "chow", "Bye", "Até amanhã, tchau!", "See you tomorrow, bye!")
            ),
            grammarRule = "Gender agreement in gratitude: Male speakers say 'Obrigado', while female speakers say 'Obrigada'. 'Tudo bem?' is the quintessential conversational opening in Portuguese.",
            grammarExamples = listOf(
                "— Tudo bem? — Tudo bem! (— Everything good? — Everything good!)",
                "Muito obrigado! (Male speaker: Thank you very much!)",
                "Muito obrigada! (Female speaker: Thank you very much!)"
            ),
            listening = ListeningExercise(
                speechText = "Bom dia! Tudo bem com você? Muito prazer em conhecer.",
                questionPrompt = "What was the initial greeting phrase?",
                options = listOf("Good morning (Bom dia)", "Good night (Boa noite)", "Goodbye (Tchau)", "Please (Por favor)"),
                correctIndex = 0,
                explanation = "'Bom dia' means Good morning in Portuguese."
            ),
            speaking = SpeakingPrompt(
                targetPhrase = "Olá! Tudo bem?",
                transliteration = "Oh-lah, too-doo baing?",
                translation = "Hello! Is everything good?",
                phoneticTip = "Pronounce the ending 'm' in 'bem' with a gentle nasal buzz without sealing your lips tight."
            ),
            roleplay = ConversationRoleplay(
                scenarioTitle = "Checking into a Hotel in Lisbon",
                situation = "Arriving at the hotel reception after a flight.",
                partnerRole = "Recepcionista (Receptionist)",
                initialMessage = "Olá! Seja bem-vindo. Tem uma reserva com a gente?",
                initialTranslation = "Hello! Welcome. Do you have a reservation with us?",
                sampleTurns = listOf(
                    RoleplayTurn("You", "Olá! Sim, tenho uma reserva no meu nome. Aqui está meu documento.", "Hello! Yes, I have a reservation in my name. Here is my ID.", "Confirm your reservation politely.")
                )
            ),
            quiz = listOf(
                LanguageQuizQuestion("A female speaker should say which word for 'Thank you'?", listOf("Obrigado", "Obrigada", "Obrigados", "Obrigando"), 1, "Female speakers say 'Obrigada'.")
            )
        )
    )

    private val englishLessons = listOf(
        LanguageLesson(
            id = "en_1",
            languageCode = "en",
            level = "Intermediate",
            title = "Academic Discourse & Essay Writing",
            subtitle = "Presenting Arguments & Critical Thinking",
            vocabulary = listOf(
                VocabularyItem("Hypothesis", "hahy-poth-uh-sis", "A proposed explanation made on the basis of limited evidence", "The experimental results support our initial hypothesis.", "Data matches predictions."),
                VocabularyItem("Furthermore", "fur-ther-mohr", "In addition to what will be or has been said", "Furthermore, the methodology was verified by peer review.", "Used to add strength to an argument."),
                VocabularyItem("Consequently", "kon-si-kwent-lee", "As a result", "Consequently, the final conclusions are robust.", "Shows cause and effect."),
                VocabularyItem("Comprehensive", "kom-pri-hen-siv", "Complete; including all or nearly all elements", "The professor delivered a comprehensive lecture.", "Thorough coverage.")
            ),
            grammarRule = "Transitions in academic English link premises to conclusions smoothly. Using words like 'Therefore', 'Nevertheless', and 'Furthermore' structures argumentative clarity in essays and exams.",
            grammarExamples = listOf(
                "The initial trial failed; nevertheless, the researchers persisted.",
                "Consequently, the university approved the updated syllabus."
            ),
            listening = ListeningExercise(
                speechText = "Good morning everyone. Today we will conduct a comprehensive review of our thesis hypothesis.",
                questionPrompt = "What will the speaker review during the session?",
                options = listOf("Their thesis hypothesis", "Campus sports schedule", "Cafeteria lunch menu", "Parking rules"),
                correctIndex = 0,
                explanation = "The speaker explicitly stated they will conduct a comprehensive review of their thesis hypothesis."
            ),
            speaking = SpeakingPrompt(
                targetPhrase = "The evidence supports our hypothesis.",
                transliteration = "theh eh-vih-dens suh-ports ow-er hahy-poth-uh-sis",
                translation = "The factual data corroborates the theoretical assumption.",
                phoneticTip = "Place primary stress on the second syllable of hy-POTH-e-sis."
            ),
            roleplay = ConversationRoleplay(
                scenarioTitle = "University Seminar Discussion",
                situation = "Discussing research findings with Professor Evans.",
                partnerRole = "Professor Evans",
                initialMessage = "Welcome. What conclusions did you reach in your latest research assignment?",
                initialTranslation = "Welcome. What conclusions did you reach in your latest research assignment?",
                sampleTurns = listOf(
                    RoleplayTurn("You", "We examined the statistical metrics, and consequently, we confirmed the primary hypothesis.", "We examined the statistical metrics, and consequently confirmed the hypothesis.", "Present your conclusions using academic transition terms.")
                )
            ),
            quiz = listOf(
                LanguageQuizQuestion("Which word means 'as a direct result'?", listOf("Furthermore", "Consequently", "Meanwhile", "Seldom"), 1, "'Consequently' denotes causal outcome.")
            )
        )
    )
}
