package com.idapgroup.lifecycle.ktx

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.instanceOf
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ViewModelTest {

    @get:Rule
    val rule = ActivityTestRule(ExampleActivity::class.java, false, true)

    @Test
    fun lazyViewModel() {
        val viewModel = rule.activity.viewModel
        assertThat(viewModel, instanceOf(ExampleViewModel::class.java))
        assertEquals(viewModel.liveData.value, viewModel.initValue)
    }

    @Test
    fun singleLiveEvent() {
        rule.activity.apply {
            runOnUiThread {
                observe()
                postEventValue(1)
                assertEquals(1, observedValue)
                removeObserver()
                postEventValue(2)
                assertEquals(1, observedValue)
                observe()
                postEventValue(2)
                assertEquals(2, observedValue)
            }
        }
    }

}

class ExampleActivity : FragmentActivity() {

    val viewModel: ExampleViewModel by lazyViewModel { ViewModelProvider.NewInstanceFactory() }
    private val event = SingleLiveEvent<Int>()
    private var observer: Observer<Int>? = null
    var observedValue: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doSomething()
    }

    fun postEventValue(value: Int) {
        event.value = value
    }

    fun observe() {
        observer = observe(event) {
            observedValue = it
        }
    }

    fun removeObserver() {
        observer?.let {
            event.removeObserver(it)
            observer = null
        }

    }

}

class ExampleViewModel : ViewModel() {
    val initValue = 10
    val liveData = mutableLiveDataOf(initValue)

    fun doSomething() {}

}