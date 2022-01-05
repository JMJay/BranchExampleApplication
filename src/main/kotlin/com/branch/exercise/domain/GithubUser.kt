package com.branch.exercise.domain

/**
 * Describes data related to a Github User.
 */
data class GithubUser(val user_name: String = "", val display_name: String = "", val avatar: String = "",
                      val geo_location: String = "", val email: String = "", val url: String = "",
                      val created_at: String = "", val repos: Set<GithubRepo> = setOf())
