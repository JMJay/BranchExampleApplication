package com.branch.exercise.unit

import com.branch.exercise.domain.GithubUser
import com.branch.exercise.rest.GithubUserController
import com.branch.exercise.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI

class GithubUserControllerUnitTest {

    private val userService: UserService = mock(UserService::class.java)
    private val rest: RestTemplate = mock(RestTemplate::class.java)

    @Test
    fun getUser_userExistsInCache_returnsUser() {
        val sut = GithubUserController(userService)
        val testUserName = "test"
        val testUser = GithubUser(user_name = testUserName)
        `when`(userService.getUserByNameOrNull(testUserName)).thenReturn(testUser)

        val result = sut.getUser(testUserName)

        assertThat(result.user_name).isEqualTo("test")
    }

    @Test
    fun getUser_userDoesNotExistInCache_returnsUserFromAPI() {
        val sut = GithubUserController(userService, rest)
        val testUserName = "test"
        `when`(userService.getUserByNameOrNull(testUserName))
            .thenReturn(null)
        `when`(rest.getForEntity(URI("https://api.github.com/users/$testUserName"), Map::class.java))
            .thenReturn(ResponseEntity(mapOf("login" to testUserName, "created_at" to "2011-01-25T18:44:36Z"), HttpStatus.OK))
        `when`(rest.getForEntity(URI("https://api.github.com/users/$testUserName/repos"), Array<Any>::class.java))
            .thenReturn(ResponseEntity(arrayOf(), HttpStatus.OK))

        val result = sut.getUser(testUserName)

        assertThat(result.user_name).isEqualTo(testUserName)
    }

    @Test
    fun getUser_userDoesNotExistOnApi_throwsNotFoundException() {
        val sut = GithubUserController(userService, rest)
        val testUserName = "test"
        `when`(rest.getForEntity(URI("https://api.github.com/users/$testUserName"), Map::class.java))
            .thenThrow(HttpClientErrorException(HttpStatus.NOT_FOUND))

        val thrown = catchThrowable { sut.getUser(testUserName) }

        assertThat(thrown).isInstanceOf(GithubUserController.UserNotFoundException::class.java)
    }
}