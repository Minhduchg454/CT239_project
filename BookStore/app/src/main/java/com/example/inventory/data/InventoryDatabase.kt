package com.example.inventory.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [Book::class, Author::class], version = 3, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {


    abstract fun bookDao(): BooksDao
    abstract fun authorDao(): AuthorDao

    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null
        /*
        Các tính năng này giúp đảm bảo giá trị của Instance luôn cập nhật
            và giống nhau đối với tất cả các luồng thực thi.

        Điều đó có nghĩa là những thay đổi do một luồng thực hiện đối với Instance
            sẽ hiển thị ngay lập tức đối với tất cả các luồng khác.

        */

        fun getDatabase(context: Context): InventoryDatabase {
            // if Instance không rỗng, trả nó.
            // Ngược lại, tạo nó trong synchronized, đảm bảo chỉ có một luồng chi cập tại một thời điểm

            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, "item_database")
                    //.fallbackToDestructiveMigration() //Xoa bang cu va tao bang moi khi co them du lieu
                    .build()
                    .also { Instance = it }

            }
        }
    }
}

