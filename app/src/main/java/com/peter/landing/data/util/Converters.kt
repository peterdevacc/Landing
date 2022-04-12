package com.peter.landing.data.util

import androidx.room.TypeConverter
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.local.vocabulary.Vocabulary
import java.util.*

class Converters {
    @TypeConverter
    fun calendarToDateStamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter
    fun dateStampToCalendar(dateStamp: Long): Calendar = Calendar.getInstance()
        .apply { timeInMillis = dateStamp }

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
}