package com.branch.exercise.unit

import com.branch.exercise.domain.GithubUser
import com.branch.exercise.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserServiceTest {

    @Test
    fun getUserByNameOrNull_userNotInCache_returnsNull() {
        val sut = UserService(cacheLimit = 10)

        val result = sut.getUserByNameOrNull("test")

        assertThat(result).isNull()
    }

    @Test
    fun getUserByNameOrNull_userInCache_returnsUser() {
        val sut = UserService(cacheLimit = 10)
        sut.addUser(GithubUser(user_name = "test"))

        val result = sut.getUserByNameOrNull("test")

        assertThat(result?.user_name).isEqualTo("test")
    }

    @Test
    fun addUser_cacheGrowsTooLarge_invalidatesCache() {
        val sut = UserService(cacheLimit = 2)
        sut.addUser(GithubUser(user_name = "test"))
        sut.addUser(GithubUser(user_name = "test2"))
        sut.addUser(GithubUser(user_name = "test3"))

        val result = sut.getUserByNameOrNull("test")
        val result2 = sut.getUserByNameOrNull("test2")
        val result3 = sut.getUserByNameOrNull("test3")

        assertThat(result).isNull()
        assertThat(result2).isNull()
        assertThat(result3?.user_name).isEqualTo("test3")
    }
}