package com.example.inventory.data

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

//Cung cap mot so sach san co cho ung dung

class DatabaseHelper(private val context: Context) {
    fun copyDatabase() {
        val dbName = "inventory_database_1.db" // Tên file db bạn muốn sao chép
        val dbPath = context.getDatabasePath("inventory_database").path // Đường dẫn đến inventory_database

        // Kiểm tra xem file db đã tồn tại hay chưa
        val dbFile = File(dbPath)
        if (dbFile.exists()) return // Nếu đã tồn tại, không cần sao chép

        // Tạo thư mục databases nếu chưa có
        dbFile.parentFile?.mkdirs()

        // Sao chép file db từ thư mục assets vào thư mục databases
        context.assets.open(dbName).use { inputStream ->
            FileOutputStream(dbPath).use { outputStream ->
                copyFile(inputStream, outputStream)
            }
        }
    }

    private fun copyFile(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) {
            outputStream.write(buffer, 0, length)
        }
        outputStream.flush()
    }
}

