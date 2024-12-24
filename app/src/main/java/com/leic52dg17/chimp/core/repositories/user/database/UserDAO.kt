package com.leic52dg17.chimp.core.repositories.user.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.leic52dg17.chimp.core.repositories.user.model.UserEntity

@Dao
interface UserDAO {
    @Query("SELECT * FROM user")
    fun getAll(): List<UserEntity>

    @Insert
    fun insertAll(users: List<UserEntity>)

    @Insert
    fun insert(user: UserEntity)

    @Delete
    fun delete(user: UserEntity)

    @Query("DELETE FROM user")
    fun deleteAll()

    @Query("SELECT * FROM user WHERE id = :id")
    fun getById(id: Int): UserEntity?
}