package com.idapgroup.lifecycle.ktx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe

/**
 * Returns a MutableLiveData with value = [initValue]
 */
fun <T> mutableLiveDataOf(initValue: T) = MutableLiveData<T>().apply { value = initValue }


/**
 * Observes given liveData and returns it Observer.
 */
fun <T : Any?, L : LiveData<T>> FragmentActivity.observe(
    liveData: L,
    observer: (T) -> Unit
): Observer<T> {
    return liveData.observe(this, observer)
}

/**
 * Observes given liveData based on [Fragment]'s viewLifecycleOwner and returns it Observer.
 */
fun <T : Any?, L : LiveData<T>> Fragment.observe(
    liveData: L,
    observer: (T) -> Unit
): Observer<T> {
    return liveData.observe(viewLifecycleOwner, observer)
}