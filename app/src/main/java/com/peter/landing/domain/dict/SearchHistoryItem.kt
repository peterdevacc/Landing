package com.peter.landing.domain.dict

import com.peter.landing.data.local.history.SearchHistory

data class SearchHistoryItem(
    val type: Type,
    val data: Data
) {
    enum class Type {
        ItemSearchHistory, SeparatorSearchDate
    }

    sealed class Data {
        data class ItemSearchHistory(
            val searchHistory: SearchHistory
        ) : Data()

        data class SeparatorSearchDate(
            val searchDate: String
        ) : Data()
    }
}