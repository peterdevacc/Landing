package com.peter.landing.data.local.wrong

import androidx.room.ColumnInfo

data class ProgressWrongWord(
    @ColumnInfo(name = "study_progress_id")
    val studyProgressId: Long,

    @ColumnInfo(name = "spelling")
    val spelling: String,

    @ColumnInfo(name = "ipa")
    val ipa: String,

    @ColumnInfo(name = "cn")
    val cn: Map<String, List<String>>,

    @ColumnInfo(name = "en")
    val en: Map<String, List<String>>,

    @ColumnInfo(name = "pron_name")
    val pronName: String
)
