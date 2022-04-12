package com.peter.landing.data.local.affix

import androidx.room.*

@Entity(
    tableName = "affix",
    indices = [Index(value = ["catalog_id"])],
    foreignKeys = [
        ForeignKey(
            entity = AffixCatalog::class,
            parentColumns = ["id"], childColumns = ["catalog_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Affix(
    val text: String,

    val meaning: String,

    val example: String,

    @ColumnInfo(name = "catalog_id")
    val catalogId: Long
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}