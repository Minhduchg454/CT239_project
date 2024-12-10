package com.example.inventory

import android.app.Application
import com.example.inventory.data.AppContainer
import com.example.inventory.data.AppDataContainer
import com.example.inventory.data.DatabaseHelper

//Khoi tao database, them du lieu vao ung dung

class InventoryApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)

        // Khởi tạo DatabaseHelper
        val databaseHelper = DatabaseHelper(this)
        databaseHelper.copyDatabase()
    }
}
