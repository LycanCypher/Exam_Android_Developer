package org.lycancypher.exam_android_developer.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun removeUser(user: User)

    @Query("DELETE FROM User WHERE id = :id")
    fun removeUserById(id: Int)

    @Delete
    fun removeUsers(vararg user: User)

    @Query("SELECT * FROM User")
    fun getUsers(): List<User>

    @Query("SELECT * FROM User WHERE id = :id")
    fun getUserById(id: Int): User
}