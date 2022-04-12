package com.peter.landing.domain.affix

import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog

data class AffixItem(
    val type: Type,
    val data: Data
) {

    enum class Type {
        CATALOG, ITEM,
        CATALOG_WITHOUT_MEANING, ITEM_WITHOUT_MEANING
    }

    sealed class Data {

        data class ItemAffix (
            val affix: Affix
        ): Data()

        data class ItemAffixCatalog (
            val affixCatalog: AffixCatalog
        ): Data()
    }

}
