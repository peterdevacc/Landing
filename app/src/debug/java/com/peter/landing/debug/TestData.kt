package com.peter.landing.debug

import com.peter.landing.data.local.history.SearchHistory
import com.peter.landing.data.local.note.Note
import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.data.local.word.Word
import com.peter.landing.data.local.wrong.Wrong
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.util.getTodayDateTime

// History
val expectSearchHistoryA = SearchHistory(
    "search input A"
)
val expectSearchHistoryB = SearchHistory(
    "search input B"
)

// Note
val expectNote = Note(4317)

// StudyPlan
val expectStudyPlanBeginner = StudyPlan(
    Vocabulary.Name.BEGINNER, 20, getTodayDateTime()
)

// Vocabulary for UseCase test
val expectVocabularyBeginner = Vocabulary(
    Vocabulary.Name.BEGINNER,
    2820,
    "2820",
    ""
)

// Word wordId = 4317
val expectWord = Word(
    "oral",
    "/ˈɔrəl/",
    "{\"形容词\": [\"口头的；口述的\", \"口的；口服的；口腔的\"], \"名词\": [\"口试\"]}",
    "{\"adjective\": [\"spoken and not written.\", " +
            "\"of, taken by, or done to the mouth.\"], " +
            "\"noun\": [\"a spoken examination or test.\"]}",
    "pron/o/oral.ogg"
)

// Wrong
val expectWrongA = Wrong(
    1, chosenWrong = true
)

val expectWrongB = Wrong(
    2, chosenWrong = true
)

fun getWrongWordListForTest(): List<WrongWord> {
    val expectWordA = Word(
        spelling = "aversion",
        ipa = "/əˈvɜr·ʒən/",
        cn = "{\"名词\": [\"厌恶，反感；讨厌的人（或事物）\"]}",
        en = "{\"noun\": [\"(a person or thing that causes) a feeling of strong dislike " +
                "or of not wishing to do something.\"]}",
        pronName = "pron/a/aversion.ogg"
    )
    expectWordA.id = 1

    val expectWordB = Word(
        spelling = "interrupt",
        ipa = "/ˌɪntəˈrʌpt/",
        cn = "{\"动词\": [\"打断（其他人说话）\", \"短暂中止\"]}",
        en = "{\"verb\": [\"to stop a person from speaking for a short period by something " +
                "you say or do.\", \"to stop something from happening for a short period.\"]}",
        pronName = "pron/i/interrupt.ogg"
    )
    expectWordB.id = 2

    val expectWrongA = Wrong(
        expectWordA.id, chosenWrong = true
    )
    val expectWrongB = Wrong(
        expectWordB.id, spelledWrong = true
    )

    return listOf(
        WrongWord(expectWrongA, expectWordA),
        WrongWord(expectWrongB, expectWordB)
    )
}