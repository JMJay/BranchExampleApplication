package com.branch.exercise.rest

import com.branch.exercise.domain.GithubRepo
import com.branch.exercise.domain.GithubUser
import com.branch.exercise.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * API capable of serving Github users publicly accessible information (like their followers and public repos).
 * An HttpClient (org.springframework.web.client.RestTemplate) provided by default but exposed as an injectable dependency.
 */
@RestController
class GithubUserController(private val cache: UserService,
                           private val rest: RestTemplate = RestTemplate()) {

    private val baseUrl = "https://api.github.com"

    /**
     * Api to get the GithubUser object associated with the provided Github username.
     * This api does not require authentication/authorization.
     */
    @GetMapping(value = ["/user"])
    @ResponseBody
    fun getUser(@RequestParam(name = "userName") userName: String): GithubUser {
        val user = cache.getUserByNameOrNull(userName)
        if (user != null) return user
        val response = makeGithubRequestForUser(userName)
        cache.addUser(response)
        return response
    }

    // Two notes about the implementation:
    // One might think to pull the repo_urls to make the second call to get the repo's information backwards compatible with Github's api.
    // Because we would be taking that string and directly sticking it into a URI object, and making a rest request, that could be a security issue.
    // So, we prefer to hard code both URIs.
    //
    // These could be pulled out into the application configuration if wanted to avoid changing code when Github's URL might one day update.
    // If either of the URIs ever changed on their end, or Github was down, we would throw an error.
    // Our customers would likely get upset and let us know... a more complete solution might include an alarm or health check of some kind.
    private fun makeGithubRequestForUser(userName: String): GithubUser {
        val userUri = URI("$baseUrl/users/$userName")
        val userReposUri = URI("$baseUrl/users/$userName/repos")
        try {
            val userResponse = rest.getForEntity(userUri, Map::class.java).body
            val userReposResponse = rest.getForObject(userReposUri, Array<GithubRepo>::class.java)
            val temporal = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .parse(userResponse?.get("created_at").toString())
            val createdAtOutput = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss")
                .format(temporal)
            return GithubUser(
                user_name = userResponse?.get("login").toString(),
                display_name = userResponse?.get("name").toString(),
                avatar = userResponse?.get("avatar_url").toString(),
                geo_location = userResponse?.get("geo_location").toString(),
                email = userResponse?.get("email").toString(),
                url = userResponse?.get("url").toString(),
                created_at = createdAtOutput,
                repos = userReposResponse?.toSet() ?: setOf()
            )
        } catch (e: HttpClientErrorException) {
            // Could get any type of error back from Github's api, so we just tell the user of OUR api that the user could not be found.
            throw UserNotFoundException()
        }
    }

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Could not get data for user.")
    class UserNotFoundException : RuntimeException()
}
