package com.idapgroup.lifecyclektx.sample

import androidx.lifecycle.ViewModel
import com.idapgroup.lifecycle.ktx.SingleLiveEvent
import com.idapgroup.lifecycle.ktx.mutableLiveDataOf
class MainViewModel : ViewModel() {

    val counterLiveData = mutableLiveDataOf(0)
    val lastPressedEvent = SingleLiveEvent<String?>()

    fun inc() {
        lastPressedEvent.value = "INC have pressed"
        counterLiveData.value = (counterLiveData.value ?: 0).inc()
    }

    fun dec() {
        lastPressedEvent.value = "DEC have pressed"
        counterLiveData.value = (counterLiveData.value ?: 0).dec()
    }

}
