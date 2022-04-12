package com.peter.landing.domain.ipa

import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.data.repository.ipa.IpaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class IpaUseCase @Inject constructor(
    private val ipaRepository: IpaRepository
) {

    private val ipaTypeMutableStateFlow = MutableStateFlow(Ipa.Type.CONSONANTS)
    private val ipaListMutableStateFlow = MutableStateFlow(emptyList<IpaItem>())
    private var ipaFullList = emptyList<Ipa>()
    val ipaList: StateFlow<List<IpaItem>> = ipaListMutableStateFlow

    fun setIpaType(type: Ipa.Type, scope: CoroutineScope) = scope.launch {
        ipaTypeMutableStateFlow.emit(type)
    }

    fun initIpa(scope: CoroutineScope) = scope.launch {
        ipaFullList = ipaRepository.getIpaList()
        ipaTypeMutableStateFlow.asStateFlow().collect { type ->
            val data = when (type) {
                Ipa.Type.CONSONANTS -> {
                    ipaFullList.filter {
                        it.type == Ipa.Type.CONSONANTS
                    }
                }
                Ipa.Type.VOWELS -> {
                    ipaFullList.filter {
                        it.type == Ipa.Type.VOWELS
                    }
                }
                Ipa.Type.NONE -> emptyList()
            }
            if (data.isNotEmpty()) {
                val ipaTypeHeader = IpaItem(
                    IpaItem.Type.IpaTypeHeader,
                    IpaItem.Data.IpaTypeHeader(data.first().type.cnValue)
                )
                val ipaItemList = mutableListOf<IpaItem>()
                ipaItemList.add(ipaTypeHeader)
                ipaItemList.addAll(
                    data.map {
                        IpaItem(
                            IpaItem.Type.ItemIpa,
                            IpaItem.Data.ItemIpa(it)
                        )
                    }
                )
                ipaListMutableStateFlow.emit(ipaItemList)
            }
        }
    }

}