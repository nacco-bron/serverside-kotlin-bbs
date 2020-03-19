package com.example.app.bbs.domain.repository

import com.example.app.bbs.domain.entity.PostUser
import org.springframework.data.jpa.repository.JpaRepository

interface PostUserRepository : JpaRepository<PostUser, Int>{
    fun findByName(name : String) : PostUser?
}