package com.peter.landing.data.local.affix

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "affix_catalog")
data class AffixCatalog(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,

    val type: Type,

    val description: String
) {
    enum class Type(val cnValue: String) {
        PREFIX("前缀"),
        SUFFIX("后缀"),
        MIXED("混合"),
        NONE("")
    }
}
