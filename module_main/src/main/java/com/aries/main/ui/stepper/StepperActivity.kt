package com.aries.main.ui.stepper

import android.os.Bundle
import android.view.LayoutInflater
import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.common.base.CommonActivity
import com.aries.common.constants.RouterPaths
import com.aries.common.widget.Stepper
import com.aries.main.databinding.LayoutStepperDemoBinding
import com.orhanobut.logger.Logger

@Route(path = RouterPaths.STEPPER_DEMO)
class StepperActivity: CommonActivity() {
    private lateinit var binding: LayoutStepperDemoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()
        binding = LayoutStepperDemoBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initView()
    }

    fun initView() {
        binding.stepperDemo.setStep(2)
        binding.stepperDemo.setInputValue(10)
        binding.stepperDemo.setOnChangeValueListener(object: Stepper.OnChangeValueListener {
            override fun onChangeValue(value: String) {
                Logger.i(value)
            }
        })
    }
}