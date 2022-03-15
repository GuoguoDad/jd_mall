package com.example.main

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.category.CategoryFragment
import com.example.common.constants.RouterPaths
import com.example.common.base.BaseActivity
import com.example.home.ui.HomeFragment
import com.example.main.ui.cart.CartFragment
import com.example.main.ui.mine.MineFragment
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.layout_main.*

@Route(path = RouterPaths.MAIN_ACTIVITY)
class MainActivity: BaseActivity(R.layout.layout_main) {
    private val homeFragment: Fragment = HomeFragment()
    private val categoryFragment: Fragment = CategoryFragment()
    private val cartFragment: Fragment = CartFragment()
    private val mineFragment: Fragment = MineFragment()

    override fun initView() {
        val navController = findNavController(R.id.container_fragment)
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        navView.setupWithNavController(navController)

        var active = homeFragment
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, homeFragment, "1").commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, mineFragment, "2").hide(mineFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, categoryFragment, "3").hide(categoryFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, cartFragment, "4").hide(cartFragment).commit()

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

    override fun initData() {}
}