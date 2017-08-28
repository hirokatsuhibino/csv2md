package jp.co.orangesoft.util

import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import java.io.IOException
import java.io.StringReader

class CSVParserTest {

    @Test
    fun testCSVParser1() {

        try {
            val csv = "aaa,bbb,ccc\r\n" + //0

                    "2a,2b,2c\r\n" + //1

                    "\"aaaa\",bbbb\r\n" + //2

                    "\"aa\"\"AA\",b2bbb\r\n" + //3

                    "\"aa,aa\",bbbb\r\n" + //4

                    "\"aa, aa\",bbbb\r\n" + //5

                    "2a,,2c\r\n" + //6

                    "\"aa, aa\",\"bb, cc\"\r\n" + //7

                    "#2a,,2c\r\n" + //8

                    "2a,2b cc,2c\r\n" + // 9

                    "2a,2b cc,\"2c\r\nABC\"" + // 10

                    "\r\n" // 10

            val result = CSVParser.parse(StringReader(csv))

            var line = result.get(0)
            assertTrue(line.get(0) == "aaa" && line.get(1) == "bbb" && line.get(2) == "ccc")
            line = result.get(1)
            assertTrue(line.get(0) == "2a" && line.get(1) == "2b" && line.get(2) == "2c")
            line = result.get(2)
            assertTrue(line.get(0) == "aaaa" && line.get(1) == "bbbb")
            line = result.get(3)
            assertTrue(line.get(0) == "aa\"AA" && line.get(1) == "b2bbb")
            line = result.get(4)
            assertTrue(line.get(0) == "aa,aa" && line.get(1) == "bbbb")
            line = result.get(5)
            assertTrue(line.get(0) == "aa, aa" && line.get(1) == "bbbb")
            line = result.get(6)
            assertTrue(line.get(0) == "2a" && line.get(1).isEmpty() && line.get(2) == "2c")
            line = result.get(7)
            assertTrue(line.get(0) == "aa, aa" && line.get(1) == "bb, cc")
            line = result.get(8)
            assertTrue(line.get(0) == "#2a" && line.get(1).isEmpty() && line.get(2) == "2c")
            line = result.get(9)
            assertTrue(line.get(0) == "2a" && line.get(1) == "2b cc" && line.get(2) == "2c")
            line = result.get(10)
            assertTrue(line.get(0) == "2a" && line.get(1) == "2b cc" && line.get(2) == "2c\r\nABC")

            println("OK")

        } catch (e: IOException) {
            e.printStackTrace()
            fail()
        }

    }

}