package com.aries.common.config

interface ModuleConfig {

    companion object {

        private const val MODULE_MAIN = "com.example.main.MainApplication"

        private const val MODULE_HOME = "com.example.home.HomeApplication"

        private const val MODULE_COMMON = "com.aries.common.CommonApplication"

        val modules = arrayOf( MODULE_MAIN, MODULE_HOME, MODULE_COMMON )
    }
}