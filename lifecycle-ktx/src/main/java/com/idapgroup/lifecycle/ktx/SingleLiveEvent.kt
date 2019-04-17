package com.idapgroup.lifecycle.ktx

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * MutableLiveData that emits only new updates after subscription.
 * Multiple observers registered but only one will be notified of changes.
 */
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val notify = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        val wrapping = Observer<T> {
            if (notify.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        }
        super.observe(owner, wrapping)
    }

    override fun observeForever(observer: Observer<in T>) = TODO("Not implemented")

    @MainThread
    override fun setValue(t: T?) {
        notify.set(true)
        super.setValue(t)
    }
}