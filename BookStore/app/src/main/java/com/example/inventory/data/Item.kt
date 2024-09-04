package com.example.inventory.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity data class represents a single row in the database.
 */

@Entity(tableName = "authors")
data class Author(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String
)




@Entity(
    tableName = "books",
    foreignKeys = [
        ForeignKey(
            entity = Author::class, //xd bảng khóa ngoại tham chiếu
            parentColumns = ["id"], //cột bảng cha mà con tham chiếu
            childColumns = ["authorId"], //xđ khóa ngoại
            onDelete = ForeignKey.SET_NULL, //Cha xoa thì con sẽ thiết lập là null
            onUpdate = ForeignKey.CASCADE  // Khi cập nhật Id của tác giả, authorId trong bảng Book cũng sẽ được cập nhật
        )
    ],
    indices = [Index(value = ["authorId"])]
)
data class Book(
    @PrimaryKey (autoGenerate = true)
    val bookId: Int =0,
    val name: String,
    val type: String,
    val authorId: Int? = null, //cho phep gia tri null
    val publicationInfo: String,
    val shelfNumber: String,
    val subject: String,
    val physicalDescription: String,
    val saveToLibrary: Boolean
)
