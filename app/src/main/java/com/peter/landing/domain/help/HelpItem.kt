package com.peter.landing.domain.help

import com.peter.landing.data.local.help.Help
import com.peter.landing.data.local.help.HelpCatalog

data class HelpItem(
    val type: Type,
    val data: Data
) {
    enum class Type {
        ItemHelpCatalog, ItemHelp, Footer
    }

    sealed class Data {
        data class ItemHelpCatalog(
            val helpCatalog: HelpCatalog
        ) : Data()

        data class ItemHelp(
            val help: Help
        ) : Data()

        object ItemHelpFooter : Data()
    }
}