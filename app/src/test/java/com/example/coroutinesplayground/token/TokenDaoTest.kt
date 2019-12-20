package com.example.coroutinesplayground.token

import com.example.coroutinesplayground.db.TokenDb
import io.kotlintest.assertions.arrow.either.shouldBeLeft
import io.kotlintest.assertions.arrow.either.shouldBeRight
import io.kotlintest.matchers.string.shouldNotBeEmpty
import io.kotlintest.shouldBe
import io.kotlintest.shouldThrow
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.net.UnknownHostException

class TokenDaoTest {

    private val tokenDb: TokenDb = mockk {
        coEvery { getToken() } coAnswers { "111" }
    }

    private fun tokenDao() = TokenDao(tokenDb)

    @Test
    fun `test when token exists THEN emit Right`() = runBlockingTest {
        tokenDao().userFlow.collect {
            it.shouldBeRight()
        }
    }

    @Test
    fun `test when token doesn't exist THEN emit Left`() = runBlockingTest {
        coEvery { tokenDb.getToken() } coAnswers { null }
        tokenDao().userFlow.collect {
            it.shouldBeLeft()
        }
    }

    @Test
    fun `test successful call with token`() = runBlockingTest {
        tokenDao().userFlow.collect {
            it shouldBeRight { it.callWithToken { successfulRequest() }.shouldNotBeEmpty() }
        }
    }

    @Test
    fun `test unsuccessful call with 401 response`() = runBlockingTest {
        tokenDao().userFlow.collect {
            shouldThrow<HttpException> { it shouldBeRight { it.callWithToken { errorRequest() } } }.code() shouldBe 401
        }
    }

    @Test
    fun `test unsuccessful call when no internet`() = runBlockingTest {
        tokenDao().userFlow.collect {
            shouldThrow<UnknownHostException> { it shouldBeRight { it.callWithToken { requestWithNoInternetError() } } }
        }
    }

    private fun errorRequest(): String =
        throw HttpException(Response.error<String>(401, "".toResponseBody()))

    private fun successfulRequest(): String = "Successful request"
    private fun requestWithNoInternetError(): String = throw UnknownHostException()
}