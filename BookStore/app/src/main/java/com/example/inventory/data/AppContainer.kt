package com.example.inventory.data

import android.content.Context

/**
 * 5.
 * App container for Dependency injection.
 * AppDataContainer đóng vai trò là một nơi tập trung quản lý việc tạo
 * và cung cấp các repository này cho toàn bộ ứng dụng.
 */

//Khai bao cac thuoc tinh truu tuong, phan cai dat tuy thuoc vao ham
interface AppContainer {
    val booksRepository: BooksRepository
    val authorsRepository: AuthorsRepository
    val subjectsRepository: SubjectsRepository
    val bookTypesRepository: BookTypesRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineItemsRepository]
 */

class AppDataContainer(private val context: Context) : AppContainer {

    //Từ khóa lazy cho phép trì hoãn (lazy initialization) việc khởi tạo repository cho đến khi nó thực sự được sử dụng lần đầu tiên. Điều này giúp:
    override val booksRepository: BooksRepository by lazy {
        OfflineBookRepository(InventoryDatabase.getDatabase(context).bookDao())
    }

    override val authorsRepository: AuthorsRepository by lazy {
        OfflineAuthorRepository(InventoryDatabase.getDatabase(context).authorDao())
    }

    override val subjectsRepository: SubjectsRepository by lazy {
        OfflineSubjectRepository(InventoryDatabase.getDatabase(context).subjectDao())
    }

    override val bookTypesRepository: BookTypesRepository by lazy {
        OfflineBookTypeRepository(InventoryDatabase.getDatabase(context).bookTypeDao())
    }
}
