Lifecycle Kotlin Extensions
============

Several Kotlin extensions for convenient use of ViewModel and LiveData

Download
--------


Add repository to your root `build.gradle`

```groovy
repositories {
    jcenter()
}
```


```groovy
dependencies {
  implementation 'com.idapgroup:lifecycle-ktx:1.0.2'
}
```


Usage sample
-------------

```kotlin

class ExampleActivity : AppCompatActivity() {

    private lateinit var factory: ViewModelProvider.Factory
    val viewModel: ExampleViewModel by lazyViewModel { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observe(viewModel.liveData, ::observeLiveData)
    }

    private fun observeLiveData(value: String) {
        // do something with value
    }

}

class ExampleViewModel : ViewModel() {

    val liveData = mutableLiveDataOf("1")

}

```

SingleLiveEvent
----------------

SingleLiveEvent - class that extends MutableLiveData and emits value only once for observer.
Multiple observers registered but only one will be notified of changes.


Bonus
-------------

For __Dagger__ users added __ViewModelProvider.Factory__ class and _@ViewModelKey_ annotation.

