package com.peter.landing.domain.affix

import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.data.repository.affix.AffixCatalogRepository
import com.peter.landing.data.repository.affix.AffixRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AffixUseCase @Inject constructor(
    private val affixCatalogRepository: AffixCatalogRepository,
    private val affixRepository: AffixRepository
) {

    private val affixCatalogTypeMutableStateFlow =
        MutableStateFlow(AffixCatalog.Type.PREFIX)
    private val affixDataMutableStateFlow =
        MutableStateFlow(emptyMap<AffixCatalog, List<Affix>>())
    private var affixDataRaw =
        LinkedHashMap<AffixCatalog, List<Affix>>()
    val affixData: StateFlow<Map<AffixCatalog, List<Affix>>> =
        affixDataMutableStateFlow

    fun setAffixCatalogType(type: AffixCatalog.Type, scope: CoroutineScope) = scope.launch {
        affixCatalogTypeMutableStateFlow.emit(type)
    }

    fun initAffix(scope: CoroutineScope) = scope.launch {
        val affixCatalogList = affixCatalogRepository.getAffixCatalogList()
        for (catalog in affixCatalogList) {
            val affixList = affixRepository.getAffixListByCatalog(catalog.id)
            affixDataRaw[catalog] = affixList
        }
        affixCatalogTypeMutableStateFlow.asStateFlow().collect { type ->
            val data = when (type) {
                AffixCatalog.Type.PREFIX -> {
                    affixDataRaw.filter {
                        it.key.type == AffixCatalog.Type.PREFIX
                    }
                }
                AffixCatalog.Type.SUFFIX -> {
                    affixDataRaw.filter {
                        it.key.type == AffixCatalog.Type.SUFFIX
                    }
                }
                AffixCatalog.Type.MIXED -> {
                    affixDataRaw.filter {
                        it.key.type == AffixCatalog.Type.MIXED
                    }
                }
                AffixCatalog.Type.NONE -> emptyMap()
            }
            affixDataMutableStateFlow.emit(data)
        }
    }

}