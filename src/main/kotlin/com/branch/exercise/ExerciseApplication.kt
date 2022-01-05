package com.branch.exercise

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
class ExerciseApplication

fun main(args: Array<String>) {
	runApplication<ExerciseApplication>(*args)
}
