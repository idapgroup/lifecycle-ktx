package com.idapgroup.lifecycle.ktx

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

typealias OnChanged<T> = (T) -> Unit

/**
 * MutableLiveData that emits only new updates after subscription.
 * Multiple observers registered but only one will be notified of changes.
 */
class SingleLiveEvent<T> : MutableLiveData<T>()  {

    private var pending = AtomicBoolean(false)

    override fun setValue(value: T) {
        pending.set(true)
        super.setValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, SingleLiveEventObserver(pending, observer))
    }

    fun observeForever(onChanged: OnChanged<T>) {
        observeForever(LambdaObserver(onChanged))
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(SingleLiveEventObserver(pending, observer))
    }

    fun removeObserver(onChanged: OnChanged<T>) {
        removeObserver(LambdaObserver(onChanged))
    }

    override fun removeObserver(observer: Observer<in T>) {
        super.removeObserver(SingleLiveEventObserver(pending, observer))
    }

    private class LambdaObserver<T>(
        private val onChanged: OnChanged<T>
    ) : Observer<T> {

        override fun onChanged(value: T) {
            onChanged.invoke(value)
        }

        override fun hashCode(): Int = onChanged.hashCode()

        override fun equals(other: Any?): Boolean =
            other is LambdaObserver<*> && other.onChanged == onChanged
    }

    private class SingleLiveEventObserver<T>(
        private val pending: AtomicBoolean,
        private val observer: Observer<T>
    ) : Observer<T> {

        override fun onChanged(value: T) {
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        }

        override fun hashCode(): Int = observer.hashCode()

        override fun equals(other: Any?): Boolean =
            other is SingleLiveEventObserver<*> && other.observer == observer
    }
}