
import jp.co.orangesoft.util.CSVParser
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

fun main(args: Array<String>) {

    if (args.size < 2) {
        System.err.println("Illegal arguments.")
        System.err.println("usage: csv2md [CSV file path] [output file path]")
        exitProcess(-1)
    }
    val csvPath = args[0]
    val outPath = args[1]

    Files.newBufferedWriter(Paths.get(outPath), StandardCharsets.UTF_8).use {
        var writer = it
        val result = CSVParser.parse(Files.newBufferedReader(Paths.get(csvPath), Charset.forName("Shift_jis")))
        for((index, value) in result.withIndex()){
            if(value.isEmpty()) continue
            value.forEach {
                val escape = it.replace("\r\n", "<br>")
                        .replace("\r", "<br>")
                        .replace("\n", "<br>")
                        .replace("|", "&#124;")
                writer.write("|"+escape)
            }
            it.write("|")
            it.newLine()

            // 2行目の仕切り
            if(index == 0) {
                for(i in 1..value.size) {
                    it.write("|:--")
                }
                it.write("|")
                it.newLine()
            }
        }
    }

}

