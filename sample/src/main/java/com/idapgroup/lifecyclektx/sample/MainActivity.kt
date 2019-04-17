package com.idapgroup.lifecyclektx.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.idapgroup.lifecycle.ktx.lazyViewModel
import com.idapgroup.lifecycle.ktx.observe
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    val viewModel by lazyViewModel<MainViewModel> { ViewModelProvider.NewInstanceFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        incButton.setOnClickListener {
            viewModel.inc()
        }
        decButton.setOnClickListener {
            viewModel.dec()
        }
        subscribeButton.setOnClickListener {
            observe(viewModel.lastPressedEvent) {
                lastPressedInfoView.text = it
            }
        }
        observe(viewModel.counterLiveData) {
            counterView.text = it.toString()
        }
    }
}
