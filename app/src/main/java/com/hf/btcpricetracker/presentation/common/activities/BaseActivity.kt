package com.hf.btcpricetracker.presentation.common.activities

import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.hf.btcpricetracker.BtcApplication
import com.hf.btcpricetracker.common.di.presentation.PresentationComponent
import com.hf.btcpricetracker.common.di.presentation.PresentationModule

open class BaseActivity : AppCompatActivity() {

    private var mIsInjectorUsed: Boolean = false

    @UiThread
    protected fun getPresentationComponent(): PresentationComponent {
        if (mIsInjectorUsed) {
            throw RuntimeException("there is no need to use injector more than once")
        }
        mIsInjectorUsed = true
        return (application as BtcApplication).applicationComponent
            .newPresentationComponent(PresentationModule(this))

    }

}