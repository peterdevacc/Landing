package com.peter.landing.data.local.terms

@kotlinx.serialization.Serializable
data class Terms(
    val type: Type,

    val data: List<Item>
) {
    @kotlinx.serialization.Serializable
    enum class Type {
        SERVICE, PRIVACY, ACKNOWLEDGE;

        fun getFileName(): String {
            return "${name.lowercase()}_terms.json"
        }
    }

    @kotlinx.serialization.Serializable
    data class Item(
        val type: Type,
        val text: String,
    ) {
        enum class Type {
            TITLE,
            TEXT,
            SIGNATURE
        }
    }

}