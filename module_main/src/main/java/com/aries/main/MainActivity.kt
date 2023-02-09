package com.aries.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.cart.ui.CartFragment
import com.aries.category.ui.CategoryFragment
import com.aries.common.constants.RouterPaths
import com.aries.common.base.CommonActivity
import com.aries.common.util.UnreadMsgUtil
import com.aries.home.ui.HomeFragment
import com.aries.main.databinding.LayoutMainBinding
import com.aries.mine.ui.MineFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.navigation.NavigationBarView

@Route(path = RouterPaths.MAIN_ACTIVITY)
class MainActivity: CommonActivity() {
    private lateinit var binding: LayoutMainBinding

    private val homeFragment: Fragment = HomeFragment()
    private val categoryFragment: Fragment = CategoryFragment()
    private val cartFragment: Fragment = CartFragment()
    private val mineFragment: Fragment = MineFragment()

    private var isCategoryFragmentAdd: Boolean = false
    private var isCartFragmentAdd: Boolean = false
    private var isMineFragmentAdd: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()
        binding = LayoutMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initView()
        initData()
    }

    fun initView() {
        val navController = findNavController(R.id.container_fragment)
        binding.navView.labelVisibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED
        binding.navView.itemIconTintList = null //保留icon原图颜色
        binding.navView.setupWithNavController(navController)

        supportFragmentManager.beginTransaction().add(R.id.container_fragment, homeFragment, "1").show(homeFragment).commit()

        var active = homeFragment
        binding.navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction().hide(active).show(homeFragment).commit()
                    active = homeFragment
                }
                R.id.navigation_category -> {
                    when(isCategoryFragmentAdd) {
                        false -> {
                            supportFragmentManager.beginTransaction().add(R.id.container_fragment, categoryFragment, "3").hide(active).show(categoryFragment).commit()
                            isCategoryFragmentAdd = true
                        }
                        true -> supportFragmentManager.beginTransaction().hide(active).show(categoryFragment).commit()
                    }
                    active = categoryFragment
                }
                R.id.navigation_cart -> {
                    when(isCartFragmentAdd) {
                        false -> {
                            supportFragmentManager.beginTransaction().add(R.id.container_fragment, cartFragment, "2").hide(active).show(cartFragment).commit()
                            isCartFragmentAdd = true
                        }
                        true -> supportFragmentManager.beginTransaction().hide(active).show(cartFragment).commit()
                    }
                    active = cartFragment
                }
                R.id.navigation_mine -> {
                    when(isMineFragmentAdd) {
                        false -> {
                            supportFragmentManager.beginTransaction().add(R.id.container_fragment, mineFragment, "4").hide(active).show(mineFragment).commit()
                            isMineFragmentAdd = true
                        }
                        true -> supportFragmentManager.beginTransaction().hide(active).show(mineFragment).commit()
                    }
                    active = mineFragment
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    fun initData() {
        showBadgeView(2, 3)
    }

    /**
     * BottomNavigationView显示角标
     *
     * @param viewIndex tab索引
     * @param showNumber 显示的数字，小于等于0是将不显示
     */
    @SuppressLint("RestrictedApi")
    private fun showBadgeView(viewIndex: Int, showNumber: Int) {
        val menuView = binding.navView.getChildAt(0) as BottomNavigationMenuView
        if (viewIndex < menuView.childCount) {
            val itemView: BottomNavigationItemView = menuView.getChildAt(viewIndex) as BottomNavigationItemView
            var badgeView = LayoutInflater.from(this).inflate(com.aries.common.R.layout.badge_layout, itemView, false)

            UnreadMsgUtil.show(badgeView.findViewById(R.id.badgeNum), showNumber)

            var lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            lp.leftMargin = menuView.itemIconSize
            lp.bottomMargin = menuView.itemIconSize

            itemView.addView(badgeView, lp)
        }
    }
}