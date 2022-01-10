package com.branch.exercise.unit

import com.branch.exercise.domain.GithubUser
import com.branch.exercise.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager

class UserServiceTest {

    private val cacheManager = mock(CacheManager::class.java)
    private val cache = mock(Cache::class.java)

    @Test
    fun getUserByNameOrNull_userNotInCache_returnsNull() {
        `when`(cacheManager.getCache("user_cache")).thenReturn(cache)
        `when`(cache.get("test")).thenReturn(null)
        val sut = UserService(cacheManager)

        val result = sut.getUserByNameOrNull("test")

        assertThat(result).isNull()
        verify(cache).get("test")
    }

    @Test
    fun getUserByNameOrNull_userInCache_returnsUser() {
        `when`(cacheManager.getCache("user_cache")).thenReturn(cache)
        `when`(cache.get("test"))
            .thenReturn(Cache.ValueWrapper { GithubUser("test") })
        val sut = UserService(cacheManager)
        sut.addUser(GithubUser(user_name = "test"))

        val result = sut.getUserByNameOrNull("test")

        assertThat(result?.user_name).isEqualTo("test")
        verify(cache).get("test")
    }
}