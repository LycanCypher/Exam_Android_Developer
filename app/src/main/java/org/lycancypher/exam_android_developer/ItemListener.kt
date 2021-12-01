package org.lycancypher.exam_android_developer

import org.lycancypher.exam_android_developer.room.User

interface ItemListener {
    fun onEdit(user: User)

    fun onDelete(user: User)
}