package com.aries.main.ui.stepper

import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.widget.Stepper
import com.aries.main.R
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.layout_stepper_demo.*

@Route(path = RouterPaths.STEPPER_DEMO)
class StepperActivity: BaseActivity(R.layout.layout_stepper_demo) {
    override fun initView() {
        stepperDemo.setStep(2)
        stepperDemo.setInputValue(10)
        stepperDemo.setOnChangeValueListener(object: Stepper.OnChangeValueListener {
            override fun onChangeValue(value: String, position: Int) {
                Logger.i(value)
            }
        })
    }

    override fun initData() {
    }
}