package com.branch.exercise.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest
class GithubUserControllerTest {

	@Autowired
	lateinit var webApplicationContext: WebApplicationContext

	@Test
	fun getRequest_userExists_successfulResponse() {
		val mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
		val testUsername = "JMJay" // author's Github ID.
		val uri = "/user"

		val mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(APPLICATION_JSON_VALUE).param("userName", testUsername)).andReturn()

		assertThat(mvcResult.response.status).isEqualTo(200)
		assertThat(mvcResult.response.contentAsString).contains("JMJay")
	}

	@Test
	fun getRequest_userDoesNotExists_404Response() {
		val mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
		// Okay, this test is a little flaky because someone could make this ID and this test would start failing
		// thus is the nature of integration tests. They are a little more like the real experience but also a little more brittle.
		// We could generate a timestamp or some random number... I think you get the idea.
		val testUsername = "JMJayJMJayJMJayJMJayJMJayJMJay"
		val uri = "/user"

		val mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(APPLICATION_JSON_VALUE).param("userName", testUsername)).andReturn()

		assertThat(mvcResult.response.status).isEqualTo(404)
	}
}

