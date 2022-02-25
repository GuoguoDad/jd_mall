package com.example.common.config

interface ModuleConfig {

    companion object {

        private const val MODULE_MAIN = "com.example.main.MainApplication"

        private const val MODULE_HOME = "com.example.home.HomeApplication"

        private const val MODULE_COMMON = "com.example.common.CommonApplication"

        val modules = arrayOf( MODULE_MAIN, MODULE_HOME, MODULE_COMMON )
    }
}