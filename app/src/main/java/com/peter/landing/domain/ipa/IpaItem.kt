package com.peter.landing.domain.ipa

import com.peter.landing.data.local.ipa.Ipa

data class IpaItem(
    val type: Type,
    val data: Data
) {
    enum class Type {
        ItemIpa, IpaTypeHeader
    }

    sealed class Data {
        data class ItemIpa(
            val ipa: Ipa
        ) : Data()

        data class IpaTypeHeader(
            val type: String
        ) : Data()
    }
}