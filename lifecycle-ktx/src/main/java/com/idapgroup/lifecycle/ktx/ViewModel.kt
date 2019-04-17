package com.idapgroup.lifecycle.ktx

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

/**
 * Simple get [ViewModel] for Kotlin users
 */
inline fun <reified T: ViewModel> ViewModelProvider.get() = get(T::class.java)

/**
 * Creates [ViewModel] based on [ViewModelProviders]
 */
inline fun <reified T: ViewModel> LifecycleOwner.getViewModel(
    factory: ViewModelProvider.Factory
) : T =  when(this) {
    is Fragment -> ViewModelProviders.of(this, factory).get()
    is FragmentActivity -> ViewModelProviders.of(this, factory).get()
    else -> throw RuntimeException("LifecycleOwner doesn't instance of Fragment or Activity")
}

/**
 * Creates a new instance of the [Lazy] that uses [getViewModel] initialization function
 */
inline fun <reified T: ViewModel> LifecycleOwner.lazyViewModel(
    crossinline factory: () -> ViewModelProvider.Factory
) =
    lazy(LazyThreadSafetyMode.NONE) {
        getViewModel<T>(factory())
    }