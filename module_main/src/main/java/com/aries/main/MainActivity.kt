package com.aries.main

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.aries.cart.ui.CartFragment
import com.aries.category.ui.CategoryFragment
import com.aries.common.constants.RouterPaths
import com.aries.common.base.BaseActivity
import com.aries.common.util.UnreadMsgUtil
import com.aries.home.ui.HomeFragment
import com.aries.main.ui.mine.MineFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.layout_main.*
import com.google.android.material.bottomnavigation.BottomNavigationMenuView

@Route(path = RouterPaths.MAIN_ACTIVITY)
class MainActivity: BaseActivity(R.layout.layout_main) {
    private val homeFragment: Fragment = HomeFragment()
    private val categoryFragment: Fragment = CategoryFragment()
    private val cartFragment: Fragment = CartFragment()
    private val mineFragment: Fragment = MineFragment()

    override fun initView() {
        val navController = findNavController(R.id.container_fragment)
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        navView.itemIconTintList = null //保留icon原图颜色
        navView.setupWithNavController(navController)

        var active = homeFragment
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, homeFragment, "1").commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, cartFragment, "2").hide(cartFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, categoryFragment, "3").hide(categoryFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, mineFragment, "4").hide(mineFragment).commit()

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction().hide(active).show(homeFragment).commit()
                    active = homeFragment
                }
                R.id.navigation_category -> {
                    supportFragmentManager.beginTransaction().hide(active).show(categoryFragment).commit()
                    active = categoryFragment
                }
                R.id.navigation_cart -> {
                    supportFragmentManager.beginTransaction().hide(active).show(cartFragment).commit()
                    active = cartFragment
                }
                R.id.navigation_mine -> {
                    supportFragmentManager.beginTransaction().hide(active).show(mineFragment).commit()
                    active = mineFragment
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun initData() {
        showBadgeView(2, 1)
    }

    /**
     * BottomNavigationView显示角标
     *
     * @param viewIndex tab索引
     * @param showNumber 显示的数字，小于等于0是将不显示
     */
    @SuppressLint("RestrictedApi")
    private fun showBadgeView(viewIndex: Int, showNumber: Int) {
        val menuView = navView.getChildAt(0) as BottomNavigationMenuView
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