package com.aries.common.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.aries.common.R
import kotlinx.android.synthetic.main.layout_stepper.view.*
import java.lang.NumberFormatException
import android.text.method.DigitsKeyListener

open class Stepper: LinearLayout {
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

    private var onChangeValueListener: OnChangeValueListener? = null

    private fun init(context: Context, attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.layout_stepper, this, true)

        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.Stepper)
            val editable = typedArray.getBoolean(R.styleable.Stepper_editable, true)
            step = typedArray.getInteger(R.styleable.Stepper_step, 1)
            minBuy = typedArray.getInteger(R.styleable.Stepper_minBuy, 1)

            typedArray.recycle()

            setEditable(editable)
        }

        numberEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                onNumberInputChange()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        plusImage.setOnClickListener {
            if (inputValue <= maxBuy.coerceAtMost(inventory)) {
                inputValue += step
                //正常添加
                numberEt.setText(inputValue.toString())
            }
        }
        minusImage.setOnClickListener {
            if (inputValue > minBuy) {
                inputValue -= step
                numberEt.setText(inputValue.toString())
            }
        }
        numberEt.setOnClickListener { numberEt.setSelection(numberEt.text.length) }
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
            numberEt.setText(inputValue.toString())
            onChangeValueListener?.onChangeValue(inputValue.toString())
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
            onChangeValueListener?.onChangeValue(inputValue.toString())
        }
    }


    /**
     * 得到输入框的数量
     *
     * @return
     */
    open fun getNumber(): Int {
        try {
            return numberEt.text.toString().toInt()
        } catch (e: NumberFormatException) {
        }
        numberEt.setText(minBuy.toString())
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
        numberEt.setText(value.toString())
        this.inputValue = value
    }

    fun getInputValue(): Int {
        return this.inputValue
    }

    private fun setEditable(editable: Boolean) {
        if (editable) {
            numberEt.isFocusable = true
            numberEt.keyListener = DigitsKeyListener()
        } else {
            numberEt.isFocusable = false
            numberEt.keyListener = null
        }
    }

    interface OnChangeValueListener {
        fun onChangeValue(value: String)
    }
}