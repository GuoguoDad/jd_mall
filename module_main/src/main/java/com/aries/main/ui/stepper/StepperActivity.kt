package com.aries.main.ui.stepper

import android.view.LayoutInflater
import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.common.base.BaseActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.widget.Stepper
import com.aries.main.databinding.LayoutStepperDemoBinding
import com.orhanobut.logger.Logger

@Route(path = RouterPaths.STEPPER_DEMO)
class StepperActivity: BaseActivity<LayoutStepperDemoBinding>() {
    override fun getViewBinding(): LayoutStepperDemoBinding {
        return LayoutStepperDemoBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        binding.stepperDemo.setStep(2)
        binding.stepperDemo.setInputValue(10)
        binding.stepperDemo.setOnChangeValueListener(object: Stepper.OnChangeValueListener {
            override fun onChangeValue(value: String) {
                Logger.i(value)
            }
        })
    }

    override fun initData() {
    }
}