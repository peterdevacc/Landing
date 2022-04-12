package com.peter.landing.ui.common.adapter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class ExplainItem(
    val type: Type,
    val data: Data
) {
    enum class Type {
        ItemLanguage,
        ItemWordClass, ItemWordExplain,
        ItemWordRearrangeExercise
    }

    sealed class Data {

        data class ItemLanguage(
            val language: Type
        ) : Data() {
            enum class Type {
                CN, EN
            }
        }

        data class ItemWordClass(
            val wordClass: String
        ) : Data()

        data class ItemWordExplain(
            val wordExplain: String
        ) : Data()

        data class ItemWordRearrangeExercise(
            val wordSpelling: String
        ) : Data()
    }

    companion object {

        private val googleJson = Gson()

        fun getWordExplainSingleType(explainJson: String): List<ExplainItem> {
            val explainItemList = mutableListOf<ExplainItem>()
            val wordExplainMap = stringToMapForWordExplain(explainJson)
            wordExplainMap.forEach { (key, list) ->
                explainItemList.add(createItemWordClass(key))
                var start = 1
                list.forEach { value ->
                    val wordExplain = "$start. $value"
                    explainItemList.add(createItemWordExplain(wordExplain))
                    start += 1
                }
            }
            return explainItemList
        }

        fun addItemLanguageTypeHeaderCN(explainItemList: List<ExplainItem>): List<ExplainItem> {
            val cnExplainItemList = explainItemList.toMutableList()
            cnExplainItemList.add(0, createItemLanguageCN())
            return cnExplainItemList
        }

        fun addItemLanguageTypeHeaderEN(explainItemList: List<ExplainItem>): List<ExplainItem> {
            val enExplainItemList = explainItemList.toMutableList()
            enExplainItemList.add(0, createItemLanguageEN())
            return enExplainItemList
        }

        fun getWordExplain(cnExplainJson: String, enExplainJson: String): List<ExplainItem> {
            val explainItemList = mutableListOf<ExplainItem>()
            explainItemList.add(createItemLanguageCN())
            explainItemList.addAll(getWordExplainSingleType(cnExplainJson))
            explainItemList.add(createItemLanguageEN())
            explainItemList.addAll(getWordExplainSingleType(enExplainJson))
            return explainItemList
        }

        fun addWordRearrangeExercise(explainItemList: List<ExplainItem>, wordSpelling: String)
                : List<ExplainItem> {
            val explainItemWithExerciseList = explainItemList.toMutableList()
            explainItemWithExerciseList.add(
                explainItemWithExerciseList.lastIndex + 1,
                createItemWordRearrangeExercise(wordSpelling)
            )
            return explainItemWithExerciseList
        }

        private fun createItemLanguageCN() =
            ExplainItem(Type.ItemLanguage, Data.ItemLanguage(Data.ItemLanguage.Type.CN))

        private fun createItemLanguageEN() =
            ExplainItem(Type.ItemLanguage, Data.ItemLanguage(Data.ItemLanguage.Type.EN))

        private fun createItemWordClass(wordClass: String) =
            ExplainItem(Type.ItemWordClass, Data.ItemWordClass(wordClass))

        private fun createItemWordExplain(wordExplain: String) =
            ExplainItem(Type.ItemWordExplain, Data.ItemWordExplain(wordExplain))

        private fun createItemWordRearrangeExercise(wordSpelling: String) =
            ExplainItem(
                Type.ItemWordRearrangeExercise,
                Data.ItemWordRearrangeExercise(wordSpelling)
            )

        private fun stringToMapForWordExplain(value: String): Map<String, List<String>> =
            googleJson.fromJson(value, object : TypeToken<Map<String, List<String>>>() {}.type)
    }
}