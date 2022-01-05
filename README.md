Author: Joe Jay

<h2>Setup</h2>
Application can most easily be run from Jetbrain's Intellij IDE
<a href=https://www.jetbrains.com/idea/download/>Different clients can be downloaded for Windows, Linux, and Mac</a>
- clone repo from 
- unzipping the downloaded repo
- open the cloned repo as a gradle project in Intellij by selecting the build.gradle.kts
- let the IDE "sync" to download dependencies
- run the ExerciseApplicationKt run target from the IDE
- call the api, curl example `curl http://localhost:8080/user?userName=JMJay` or postman
- can run the integration tests in GithubUserControllerTest to simulate calls for both existing and non-existent users
  - note, this requires a connection to the www
- view api documentation at http://localhost:8080/swagger-ui/index.html

<h2>Tech choices</h2>
Gradle files and project structure generated using Spring Initializr at https://start.spring.io/
Gradle chosen as the industry standard preferred build system for modern java projects.

Spring Boot was chosen because it is a fast and easy way to get a java web server up and running (tomcat embedded) with
the minimum number of dependencies.

Java 11 was chosen because I have worked with it before in an enterprise setting and am comfortable with its stability.
I would probably use a newer version for my own java projects.

Kotlin was chosen as the preferred language to Java for the following reasons:
- Null safety built into the language. Unless you explicitly tell the compiler you want to expose yourself to an NPE,
you will never hit one inside a pure Kotlin application. You may still hit an NPE if using Java libraries or interoperating
Java and Kotlin in the same project.
- Read-only data structures. Variables in Kotlin are "immutable" when defined with the 'val' keyword.
You can still define java like variables (updatable references) using the 'var' keyword. 
The advantage of immutable objects is that they reduce side effects caused by complex state, are thread safe, eliminating race conditions and concurrency problems.
- General improvements to readability, is more expressible, and naturally lends itself to a more functional coding style.

The application could be further improved by setting up a Https cert, and expanding the cache service to include
some kind of recurring invalidation, as well as adding in an access token to the auth header to avoid
rate limiting.
------------------------------------------------------------------------------------------------------------------------
Below are the instructions provided to me by the recruiter for this coding challenge.

<h2>Backend Engineer</h2>
A recently signed customer wants to integrate some data from Github into their client application. We have discussed their needs and
essentially what they want is an endpoint they can call with a username that will return the data in JSON format as specified below (that also
serves as an example):
```json
{
user_name: "octocat",
display_name: "The Octocat" ,
avatar: "https://avatars3.githubusercontent.com/u/583231?v=4 ",
geo_location: "San Francisco" ,
email: null,
url: "https://github.com/octocat ",
created_at: "2011-01-25 18:44:36" ,
repos: [
{
name: "boysenberry-repo-1" ,
url: "https://github.com/octocat/boysenberry-repo-1 "
}, 
...]
}
```

<h3>DATA FROM</h3>
https://api.github.com/users/octocat
https://api.github.com/users/octocat/repos

The example response above is returned when calling the api sending the username “octocat”. Data to form the response comes from the
two APIs noted. Be sure to take note of the difference(s) in parameter names as well as formatting for the created_at value when
compared to what is returned from the API.

No token or signup is necessary to use these Github APIs (https://docs.github.com/en/rest/guides/getting-started-with-the-rest-api), however,
you can be rate limited. Perhaps implementing a caching mechanism might help? Or of course you could get an access token that can
maybe be set at runtime (we do not expect this).

<h3>In summary</h3>
<ul>
<li>Stand up a server.</li>
<li>Have an endpoint that takes a username.</li>
<li>Fetch or retrieve the data matching to that username using the API noted above.</li>
<li>Return the JSON object defined above with the data you pulled.</li>
<li>What if no user exists?</li>
<li>Should also include some unit tests.</li>
<li>Make sure final code is in a public repo (github, bitbucket, gitlab). Provide a readMe explaining your decisions, architecture, and how to
install/run and utilize your service. We look forward to seeing your code!</li>
</ul>

<h2>FAQ</h2>
- What language/tools do I use? Can I use a framework or library?

You can choose any language/tools and use any framework or library. We are interested in knowing what you feel will be the best choice in
implementing the above. Whatever choices you make explain those in the readMe and be prepared to discuss further. Some suggestions for
languages (and frameworks): Java (Spring Boot), Node (Express), Go (Gin).

- What am I being evaluated on exactly?

We want to see production-ready code. Primary criteria is as follows:
<ul>
<li><b>Organization Is the code well organized where it can be easily extended?</b> Not one big function or lack of modularity.</li>
<li><b>Readability Can the code be easily read and followed for a new developer or when you come back to it months later?</b></li>
<li><b>Language/Libraries/Framework Are these good choices? Is the reasoning sound?</b></li>
<li><b>Documentation Can we run and use the code based on the readMe? Is the code itself well documented in some way?</b></li>
<li><b>Depth of Knowledge Are the right data structures being used? Are the strengths of the language chosen being utilized? Does theAPI follow industry standards?</b></li>
<li><b>Stability/Security Can the code handle bad input? Is the code programmed defensively?</b></li>
<li><b>Can I have someone help me?</b> Please no. We expect the code to reflect your choices and knowledge. That said, feel free to use online resources as needed for reference… But no plagiarism.</li>
</ul>
