package com.peter.landing.data.local.help

import androidx.room.*

@Entity(
    tableName = "help",
    indices = [Index(value = ["catalog_id"])],
    foreignKeys = [
        ForeignKey(
            entity = HelpCatalog::class,
            parentColumns = ["id"], childColumns = ["catalog_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Help(
    @ColumnInfo(name = "catalog_id")
    var catalogId: Long,

    @PrimaryKey
    val title: String,

    val content: String
)