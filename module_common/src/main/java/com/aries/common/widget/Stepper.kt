package com.aries.common.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import com.aries.common.R
import java.lang.NumberFormatException
import android.text.method.DigitsKeyListener
import androidx.appcompat.widget.LinearLayoutCompat
import com.aries.common.databinding.LayoutStepperBinding

open class Stepper: LinearLayoutCompat {
    private lateinit var binding: LayoutStepperBinding

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init(context, attributeSet)
    }

    //步长--每次增加的个数，默认是1
    private var step: Int = 1
    //最大购买数量
    private val maxBuy: Int = 99
    //商品最小购买数量，默认值为1
    private var minBuy: Int = 1
    //商品库存，默认最大值
    private var inventory: Int = Integer.MAX_VALUE

    private var inputValue: Int = 1

    private var data:Any? = null

    private var onChangeValueListener: OnChangeValueListener? = null

    private fun init(context: Context, attrs: AttributeSet?) {
        binding = LayoutStepperBinding.inflate(LayoutInflater.from(this.context), this, true)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Stepper)
            val editable = typedArray.getBoolean(R.styleable.Stepper_editable, true)
            step = typedArray.getInteger(R.styleable.Stepper_step, 1)
            minBuy = typedArray.getInteger(R.styleable.Stepper_minBuy, 1)

            typedArray.recycle()

            setEditable(editable)
        }

        binding.numberEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onNumberInputChange()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.plusImage.setOnClickListener {
            if (inputValue <= maxBuy.coerceAtMost(inventory)) {
                inputValue += step
                //正常添加
                binding.numberEt.setText("$inputValue")
            }
        }
        binding.minusImage.setOnClickListener {
            if (inputValue > minBuy) {
                inputValue -= step
                binding.numberEt.setText("$inputValue")
            }
        }
        binding.numberEt.setOnClickListener { binding.numberEt.setSelection(binding.numberEt.text.length) }
    }

    /**
     * 监听输入的数据变化
     */
    private fun onNumberInputChange() {
        //当前数量
        val count = getNumber()
        if (count < minBuy) {
            //手动输入
            inputValue = minBuy
            binding.numberEt.setText("$inputValue")
            onChangeValueListener?.onChangeValue("$inputValue")
            return
        }
        val limit = maxBuy.coerceAtMost(inventory)
        if (count > limit) {
            if (inventory < maxBuy) {
                //库存不足
            } else {
                //超过最大购买数
            }
        } else {
            inputValue = count
            onChangeValueListener?.onChangeValue("$inputValue")
        }
    }


    /**
     * 得到输入框的数量
     *
     * @return
     */
    open fun getNumber(): Int {
        try {
            return binding.numberEt.text.toString().toInt()
        } catch (e: NumberFormatException) {
        }
        binding.numberEt.setText(minBuy.toString())
        return minBuy
    }


    open fun setOnChangeValueListener(onChangeValueListener: OnChangeValueListener) {
        this.onChangeValueListener = onChangeValueListener
    }

    open fun setStep(step: Int) {
        this.step = step
    }

    fun getStep(): Int {
        return this.step
    }

    open fun setBuyMin(minBuy: Int) {
        this.minBuy = minBuy
    }

    fun getBuyMin(): Int {
        return this.minBuy
    }

    open fun setInventory(inventory: Int) {
        this.inventory = inventory
    }

    fun getInventory(): Int {
        return this.inventory
    }

    open fun setInputValue(value: Int) {
        binding.numberEt.setText(value.toString())
        this.inputValue = value
    }

    fun getInputValue(): Int {
        return this.inputValue
    }

    fun setData(data: Any) {
        this.data = data
    }

    fun getData(): Any? {
        return this.data
    }

    private fun setEditable(editable: Boolean) {
        if (editable) {
            binding.numberEt.isFocusable = true
            binding.numberEt.keyListener = DigitsKeyListener()
        } else {
            binding.numberEt.isFocusable = false
            binding.numberEt.keyListener = null
        }
    }

    interface OnChangeValueListener {
        fun onChangeValue(value: String)
    }
}