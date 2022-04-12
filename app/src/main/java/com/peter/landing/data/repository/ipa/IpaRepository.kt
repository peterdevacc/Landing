package com.peter.landing.data.repository.ipa

import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.local.ipa.IpaDAO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IpaRepository @Inject constructor(
    private val ipaDAO: IpaDAO
) {

    private var ipaList = emptyList<Ipa>()

    suspend fun getIpaList(): List<Ipa> {
        if (ipaList.isEmpty()) {
            ipaList = ipaDAO.getIpaList()
        }
        return ipaList
    }

}