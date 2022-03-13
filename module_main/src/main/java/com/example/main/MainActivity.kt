package com.example.main

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.category.CategoryFragment
import com.example.common.constants.RouterPaths
import com.example.common.base.BaseActivity
import com.example.home.ui.HomeFragment
import com.example.main.ui.dashboard.DashboardFragment
import com.example.main.ui.setting.SettingFragment
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import kotlinx.android.synthetic.main.layout_main.*

@Route(path = RouterPaths.MAIN_ACTIVITY)
class MainActivity: BaseActivity(R.layout.layout_main) {
    private val homeFragment: Fragment = HomeFragment()
    private val dashboardFragment: Fragment = DashboardFragment()
    private val categoryFragment: Fragment = CategoryFragment()
    private val settingFragment: Fragment = SettingFragment()

    override fun initView() {
        val navController = findNavController(R.id.container_fragment)
        navView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        navView.setupWithNavController(navController)

        var active = homeFragment
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, homeFragment, "1").commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, dashboardFragment, "2").hide(dashboardFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, categoryFragment, "3").hide(categoryFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.container_fragment, settingFragment, "4").hide(settingFragment).commit()

        navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction().hide(active).show(homeFragment).commit()
                    active = homeFragment
                }
                R.id.navigation_dashboard -> {
                    supportFragmentManager.beginTransaction().hide(active).show(dashboardFragment).commit()
                    active = dashboardFragment
                }
                R.id.navigation_category -> {
                    supportFragmentManager.beginTransaction().hide(active).show(categoryFragment).commit()
                    active = categoryFragment
                }
                R.id.navigation_setting -> {
                    supportFragmentManager.beginTransaction().hide(active).show(settingFragment).commit()
                    active = settingFragment
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun initData() {}
}