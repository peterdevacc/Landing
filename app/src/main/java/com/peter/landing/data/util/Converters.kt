package com.peter.landing.data.util

import androidx.room.TypeConverter
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.local.progress.ProgressState
import com.peter.landing.data.local.vocabulary.Vocabulary
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.util.*

class Converters {
    @TypeConverter
    fun calendarToDateStamp(calendar: Calendar?): Long? {
        return calendar?.timeInMillis
    }

    @TypeConverter
    fun dateStampToCalendar(dateStamp: Long?): Calendar? {
        return if (dateStamp != null) {
            Calendar.getInstance()
                .apply { timeInMillis = dateStamp }
        } else {
            null
        }
    }

    @Suppress("unused")
    @TypeConverter
    fun ipaTypeToString(type: Ipa.Type): String = type.name

    @TypeConverter
    fun stringToIpaType(value: String): Ipa.Type {
        return when (value) {
            Ipa.Type.CONSONANTS.name -> Ipa.Type.CONSONANTS
            Ipa.Type.VOWELS.name -> Ipa.Type.VOWELS
            else -> Ipa.Type.NONE
        }
    }

    @TypeConverter
    fun vocabularyNameToString(name: Vocabulary.Name) = name.name

    @TypeConverter
    fun stringToVocabularyName(value: String): Vocabulary.Name {
        return when (value) {
            Vocabulary.Name.BEGINNER.name -> Vocabulary.Name.BEGINNER
            Vocabulary.Name.INTERMEDIATE.name -> Vocabulary.Name.INTERMEDIATE
            else -> Vocabulary.Name.NONE
        }
    }

    @TypeConverter
    fun studySectionToString(progressState: ProgressState) = progressState.name

    @TypeConverter
    fun stringToStudySection(value: String): ProgressState {
        return when (value) {
            ProgressState.WORD_LIST.name -> ProgressState.WORD_LIST
            ProgressState.LEARN.name -> ProgressState.LEARN
            ProgressState.CHOICE.name -> ProgressState.CHOICE
            ProgressState.SPELLING.name -> ProgressState.SPELLING
            else -> ProgressState.FINISHED
        }
    }

    @Suppress("unused")
    @TypeConverter
    fun affixCatalogTypeToString(type: AffixCatalog.Type): String = type.name

    @TypeConverter
    fun stringToAffixCatalogType(value: String): AffixCatalog.Type {
        return when (value) {
            AffixCatalog.Type.PREFIX.name -> AffixCatalog.Type.PREFIX
            AffixCatalog.Type.SUFFIX.name -> AffixCatalog.Type.SUFFIX
            AffixCatalog.Type.MIXED.name -> AffixCatalog.Type.MIXED
            else -> AffixCatalog.Type.NONE
        }
    }

    @Suppress("unused")
    @TypeConverter
    fun explainToString(explain: Map<String, List<String>>): String = ""

    @TypeConverter
    fun stringToExplain(explainJson: String): Map<String, List<String>> {
        return Json.decodeFromString(explainJson)
    }

}