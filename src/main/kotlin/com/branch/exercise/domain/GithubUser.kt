package com.branch.exercise.domain

/**
 * Describes data related to a Github User.
 *
 * Could use jackson's @Jsonsetter/Getter to reduce mapping and decouple implementation from view.
 * Requires getters/setters to be implemented which is somewhat funky in Kotlin.
 */
data class GithubUser(val user_name: String = "", val display_name: String = "", val avatar: String = "",
                      val geo_location: String = "", val email: String = "", val url: String = "",
                      val created_at: String = "", val repos: Set<GithubRepo> = setOf())
