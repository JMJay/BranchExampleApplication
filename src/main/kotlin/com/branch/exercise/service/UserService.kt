package com.branch.exercise.service

import com.branch.exercise.domain.GithubUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * Holds User Information returned from the Github api.
 * Users of the cache should be aware of the any invalidation windows to account for when having the latest data is important.
 * This cache does not invalidate on its own unless it grows past a certain point.
 */
@Service
class UserService(@Autowired val cacheManager: CacheManager) {

    /**
     * Retrieves the user identified by the provided userName or null.
     */
    @Cacheable(cacheNames = ["user_cache"], key = "#userName")
    fun getUserByNameOrNull(userName: String): GithubUser? {
        val cache = cacheManager.getCache("user_cache")
        return cache?.get(userName)?.get() as GithubUser?
    }

    /**
     * Adds a user to the service cache. Cache entries are identified by the user's name.
     * Subsequent calls to add a user with the same user as a previous call will overwrite any previously saved data.
     */
    fun addUser(githubUser: GithubUser) {
        val cache = cacheManager.getCache("user_cache")
        cache?.put(githubUser.user_name, githubUser)
    }
}