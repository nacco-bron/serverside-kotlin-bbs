package com.example.app.bbs.app.service

import com.example.app.bbs.domain.entity.PostUser
import com.example.app.bbs.domain.repository.PostUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    lateinit var postUserRepository: PostUserRepository
    override fun loadUserByUsername(username: String?): UserDetails {
        var postUser : PostUser? = null
        if (username != null){
            postUser = postUserRepository.findByName(username)
        }
        if (postUser == null){
            throw UsernameNotFoundException(username)
        }
        return UserDetailsImpl(postUser)
    }
}