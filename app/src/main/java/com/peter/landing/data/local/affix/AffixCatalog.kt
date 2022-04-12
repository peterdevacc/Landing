package com.peter.landing.data.local.affix

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "affix_catalog")
data class AffixCatalog(
    val type: Type,

    val description: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0

    enum class Type {
        PREFIX,
        SUFFIX,
        MIXED,
        NONE
    }
}
