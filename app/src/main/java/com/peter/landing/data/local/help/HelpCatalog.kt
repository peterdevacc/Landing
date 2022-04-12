package com.peter.landing.data.local.help

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "help_catalog")
data class HelpCatalog(
    val name: String,

    val description: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}