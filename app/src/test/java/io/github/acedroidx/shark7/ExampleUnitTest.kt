package io.github.acedroidx.shark7

import io.github.acedroidx.shark7.Utils.formatMilliseconds
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
    fun format() {
        assertEquals("1m01s",formatMilliseconds(61 * 1000))
        assertEquals("6s",formatMilliseconds(6 * 1000))
    }
}