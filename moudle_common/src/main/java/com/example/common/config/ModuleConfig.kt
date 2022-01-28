package com.example.common.config

interface ModuleConfig {

    companion object {

        private const val MODULE_MAIN = "com.example.main.MainApplication"

        val modules = arrayOf( MODULE_MAIN)
    }
}