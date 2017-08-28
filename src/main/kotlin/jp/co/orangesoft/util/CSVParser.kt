package jp.co.orangesoft.util

import java.io.IOException
import java.io.PushbackReader
import java.io.Reader

class CSVParser {

    companion object {

        /**
         * CSVデータの解析
         * @param reader
         * @throws IOException
         */
        @Throws(IOException::class)
        fun parse(reader: Reader): List<List<String>> {
            val lines = mutableListOf<List<String>>()
            PushbackReader(reader).use { rd ->
                while (true) {
                    val pair = parseOne(rd)
                    lines.add(pair.second)
                    if (pair.first) {
                        break
                    }
                }
                return lines
            }
        }

        // 戻り値pairのleftはEOFかどうか
        private fun parseOne(reader: PushbackReader): Pair<Boolean, List<String>> {
            val tokens = mutableListOf<String>()

            var inQuote = false
            var token = StringBuilder()

            var value: Int
            loop@ while (true) {
                value = reader.read()
                if (value == -1) break
                val ch = value.toChar()

                if (inQuote) {
                    if (ch == '"') {
                        val next = reader.read()
                        if (next.toChar() == '"') {
                            token.append('"')// ""
                        } else {
                            reader.unread(next)
                            inQuote = false// クオート終了
                        }
                    } else {
                        token.append(ch)
                    }

                } else {
                    when (ch) {
                        '"' -> inQuote = true

                        ',' -> {
                            tokens.add(token.toString())
                            token = StringBuilder()
                        }

                        '\r' -> {
                            val next = reader.read()
                            if (next.toChar() != '\n') {
                                reader.unread(next)
                            }
                            reader.unread('\n'.toInt())
                        }

                        '\n' -> {
                            if (token.isNotEmpty()) {
                                tokens.add(token.toString())
                            }
                            break@loop
                        }

                        else -> token.append(ch)
                    }
                }
            }

            return Pair(value == -1, tokens)
        }

    }
}
