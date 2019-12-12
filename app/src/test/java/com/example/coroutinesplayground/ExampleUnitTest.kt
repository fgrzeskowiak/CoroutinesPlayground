package com.example.coroutinesplayground

import arrow.core.Try
import arrow.core.extensions.`try`.applicative.just
import com.example.coroutinesplayground.common.ProgressError
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `test new Try left`() {
        val either = Try { generateException() }
            .toEither { ProgressError }
        assert(either.isLeft())
    }

    @Test
    fun `test new Try right`() {
        val either = "Not an Exception".just()
            .toEither { ProgressError }

        assert(either.isRight())
    }

    private fun generateException(): String {
        throw RuntimeException()
    }
}
