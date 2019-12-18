package com.example.coroutinesplayground

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

class LoggerRule : TestRule {

    override fun apply(base: Statement?, description: Description?): Statement {
        println("BeforeTest: $description")

        try {
            return object : Statement() {
                override fun evaluate() {
                    base?.evaluate()
                }
            }
        } finally {
            println("After test: $description")
        }
    }

}