package com.idapgroup.lifecycle.ktx

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class SingleLiveEventTests {

    @get:Rule val rule = InstantTaskExecutorRule()

    var firstObserver = spy<Observer<Int>> { }
    var secondObserver = spy<Observer<Int>> { }
    var thirdObserver = spy<Observer<Int>> { }

    @Test fun getAndSetValue() {
        val liveData = SingleLiveEvent<Int>()
        assertNull(liveData.value)

        liveData.setValue(290)
        assertEquals(290, liveData.value)
    }

    @Test fun observeForever() {
        val liveData = SingleLiveEvent<Int>()

        liveData.setValue(5)
        liveData.observeForever(firstObserver::onChanged)
        liveData.observeForever(secondObserver::onChanged)
        verify(firstObserver).onChanged(5)
        verifyZeroInteractions(secondObserver)

        liveData.observeForever(thirdObserver::onChanged)
        verifyZeroInteractions(thirdObserver)
    }

    @Test fun removeObserver() {
        val liveData = SingleLiveEvent<Int>()

        liveData.observeForever(firstObserver::onChanged)
        liveData.observeForever(secondObserver::onChanged)
        assertTrue(liveData.hasObservers())

        liveData.removeObserver(firstObserver::onChanged)
        liveData.removeObserver(secondObserver::onChanged)
        assertFalse(liveData.hasObservers())
    }

    @Test fun autoRemoveObserverOnDestroy() {
        val liveData = SingleLiveEvent<Int>()

        val scenario = launchFragment { EmptyViewFragment() }
        scenario.onFragment { fragment ->
            fragment.observe(liveData, firstObserver::onChanged)
        }

        assertTrue(liveData.hasActiveObservers())

        scenario.moveToState(Lifecycle.State.DESTROYED)
        assertFalse(liveData.hasObservers())
    }

    class EmptyViewFragment : Fragment() {
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            View(requireContext())
    }
}