package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 * Lớp cơ sở dữ liệu trung tâm trong ứng dụng, chịu trách nhiệm quản lý kết nối với
 * cơ sở dữ liệu và cung cấp DAO (Data Access Object) để truy vấn dữ liệu.
 */

@Database(entities = [BOOK::class, AUTHOR::class, SUBJECT::class, BOOK_TYPE::class], version = 4 , exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    //Dinh nghia co so du lieu
    abstract fun bookDao(): BooksDao
    abstract fun authorDao(): AuthorDao
    abstract fun subjectDao(): SubjectDao
    abstract fun bookTypeDao(): BookTypeDao

    companion object {
        //Dam bao chi co mot the hien duy nhat cua lop
        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "inventory_database")
                    //.fallbackToDestructiveMigration() //Xóa bảng cũ và tạo bảng mới khi có thay đổi dữ liệu
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
