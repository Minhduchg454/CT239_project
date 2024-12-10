package com.example.inventory.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * 1
 * Tao bang trong database
 */

@Entity(tableName = "AUTHOR")
data class AUTHOR(
    @PrimaryKey(autoGenerate = true)
    val AUTHOR_Id: Int = 0,
    val AUTHOR_Name: String
)

@Entity(tableName = "BOOK_TYPE")
data class BOOK_TYPE(
    @PrimaryKey(autoGenerate = true)
    val BT_Id: Int = 0,
    val BT_Name: String
)

@Entity(tableName = "SUBJECT")
data class SUBJECT(
    @PrimaryKey(autoGenerate = true)
    val SUBJECT_Id: Int = 0,
    val SUBJECT_Name: String
)

@Entity(
    tableName = "BOOK",
    foreignKeys = [
        ForeignKey(
            entity = AUTHOR::class, //Bang cha, bang tham chieu den
            parentColumns = ["AUTHOR_Id"], //Cot bang cha tham chieu
            childColumns = ["AUTHOR_Id"], //Cot bang con se giu gia tri khoa ngoai
            onDelete = ForeignKey.SET_NULL, //Khi khoa ngoai bi xoa thi gia tri trong BOOK se la null
            onUpdate = ForeignKey.CASCADE // Khi khoa ngoai bi thay doi thi gia tri trong BOOK se thay doi theo
        ),
        ForeignKey(
            entity = SUBJECT::class,
            parentColumns = ["SUBJECT_Id"],
            childColumns = ["SUBJECT_Id"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BOOK_TYPE::class,
            parentColumns = ["BT_Id"],
            childColumns = ["BT_Id"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["AUTHOR_Id"]),
        Index(value = ["SUBJECT_Id"]),
        Index(value = ["BT_Id"])
    ]
)
data class BOOK(
    @PrimaryKey (autoGenerate = true)
    val BOOK_Id: Int =0,
    val BOOK_Name: String,
    val Book_PublicationInfo: String,
    val BOOK_ShelfNumber: String,
    val BOOK_PhysicalDescription: String,
    val Book_MFN: Int,
    val BOOK_saveToLibrary: Boolean,
    val AUTHOR_Id: Int? = null,
    val SUBJECT_Id: Int? = null,
    val BT_Id: Int? = null
)
