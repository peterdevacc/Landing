package com.peter.landing.data.local.terms

import com.peter.landing.domain.terms.TermsItem

data class Terms(
    val type: String,

    val data: List<TermsItem>
)