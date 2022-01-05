package com.branch.exercise.service

import com.branch.exercise.domain.GithubUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

/**
 * Holds User Information returned from the Github api.
 * Users of the cache should be aware of the any invalidation windows to account for when having the latest data is important.
 * This cache does not invalidate on its own unless it grows past a certain point.
 */
@Service
class UserService(@Value("\${user.service.cache.limit}") private val cacheLimit: Int) {

    private val users: MutableMap<String, GithubUser> = mutableMapOf()

    /**
     * Retrieves the user identified by the provided userName or null.
     */
    fun getUserByNameOrNull(userName: String): GithubUser? {
        return users[userName]
    }

    /**
     * Adds a user to the service cache. Cache entries are identified by the user's name.
     * Subsequent calls to add a user with the same user as a previous call will overwrite any previously saved data.
     */
    fun addUser(githubUser: GithubUser) {
        // Simple way to prevent cache from growing too large.
        // In a real system, we would want a more sophisticated solution involving expiration times on each entry.
        if (users.size >= cacheLimit) {
            resetCache()
        }
        users[githubUser.user_name] = githubUser
    }

    /**
     * Clears the user cache.
     */
    fun resetCache() {
        users.clear()
    }
}