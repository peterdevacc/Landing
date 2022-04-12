package com.peter.landing.domain.terms

data class TermsItem(
    val text: String,

    val type: String
) {
    enum class Type(val cnValue: String) {
        SUBTITLE("subTitle"),
        TEXT("text"),
        SIGNATURE("signature")
    }
}