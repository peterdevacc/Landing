package com.peter.landing.ui.navigation

import com.peter.landing.R
import com.peter.landing.data.local.terms.Terms
import com.peter.landing.data.util.SPELLING_SAVED_KEY
import com.peter.landing.data.util.TERMS_TYPE_SAVED_KEY

sealed interface LandingDestination {

    val route: String
    val textId: Int

    sealed interface Main: LandingDestination {

        val iconId: Int
        val iconCd: Int

        object Home: Main {
            override val route = "home"
            override val textId = R.string.screen_home
            override val iconId = R.drawable.ic_home_24dp
            override val iconCd = R.string.ic_content_description
        }

        object Plan: Main {
            override val route = "plan"
            override val textId = R.string.screen_plan
            override val iconId = R.drawable.ic_plan_24dp
            override val iconCd = R.string.ic_content_description
        }

        object Search: Main {
            override val route = "search"
            override val textId = R.string.screen_search
            override val iconId = R.drawable.ic_search_24dp
            override val iconCd = R.string.ic_content_description

            fun getNavDefinitionRoute(spelling: String): String {
                return "${General.Definition.route}/$spelling"
            }

        }

        object Note: Main {
            override val route = "note"
            override val textId = R.string.screen_note
            override val iconId = R.drawable.ic_note_24dp
            override val iconCd = R.string.ic_content_description
        }

        object Ipa: Main {
            override val route = "ipa"
            override val textId = R.string.screen_ipa
            override val iconId = R.drawable.ic_ipa_24dp
            override val iconCd = R.string.ic_content_description
        }

        object Affix: Main {
            override val route = "affix"
            override val textId = R.string.screen_affix
            override val iconId = R.drawable.ic_affix_24dp
            override val iconCd = R.string.ic_content_description
        }

        object Help: Main {
            override val route = "help"
            override val textId = R.string.screen_help
            override val iconId = R.drawable.ic_help_24dp
            override val iconCd = R.string.ic_content_description
        }

        object About: Main {
            override val route = "about"
            override val textId = R.string.screen_about
            override val iconId = R.drawable.ic_about_24dp
            override val iconCd = R.string.ic_content_description

            fun getNavTermsRoute(termsType: Terms.Type): String {
                return "${General.Terms.route}/${termsType.name}"
            }
        }

        companion object {
            private val mainList = listOf(
                Home, Plan, Search, Note, Ipa, Affix, Help, About
            )

            fun getNavigationList(): List<Main> {
                return mainList
            }
        }

    }

    sealed interface General: LandingDestination {

        object Spelling: General {
            override val route = "spelling"
            override val textId = R.string.screen_spelling
        }

        object Choice: General {
            override val route = "choice"
            override val textId = R.string.screen_choice
        }

        object Learn: General {
            override val route = "learn"
            override val textId = R.string.screen_learn
        }

        object WordList: General {
            override val route = "list"
            override val textId = R.string.screen_word_list
        }

        object Definition: General {
            override val route = "definition"
            override val textId = R.string.screen_definition

            val destRoute = "$route/{$SPELLING_SAVED_KEY}"
        }

        object Terms: General {
            override val route = "terms"
            override val textId = R.string.screen_terms

            val destRoute = "$route/{$TERMS_TYPE_SAVED_KEY}"
        }

        object Greeting: General {
            override val route = "greeting"
            override val textId = R.string.screen_greeting
        }

    }

}
