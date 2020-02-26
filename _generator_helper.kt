import java.io.File
import java.io.InputStream
import java.util.regex.Matcher
import java.util.regex.Pattern

// region Readme generator
val variableList = listOf("versionName", "libraryName", "libraryGroup")

fun getVariables(): Map<String, String> {
    val variables: MutableMap<String, String> = mutableMapOf()
    val inputStream: InputStream = File("gradle.properties").inputStream()

    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val vList = variableList.filter { variable -> line.contains(variable) }
            if (vList.isNotEmpty()) {
                val key = vList[0]
                val value = line.trim().replace("$key=", "")
                variables[key] = value
            }
        }
    }
    inputStream.close()

    return variables
}

fun generateReadme() {
    val variables = getVariables()

    // read file and replace variables
    val inputStream: InputStream = File("_readme_raw.md").inputStream()
    var data = inputStream.bufferedReader().readText()
    variableList.forEach { variable ->
        data = data.replace("{{$variable}}", variables[variable] ?: "", false)
    }
    inputStream.close()

    // save result file
    File("README.md").writeText(data)

    println("README.md created successfully !!")
}
// endregion

// region dimen.xml generator
val dimenPath = "module\\src\\main\\res\\values\\dimens.xml"
val dimenSmallPath = "module\\src\\main\\res\\values-small\\dimens.xml"
val dimenLargePath = "module\\src\\main\\res\\values-large\\dimens.xml"

fun extractNumbers(input: String): String {
    val pattern = Pattern.compile("[0-9]*\\.?[0-9]+")
    val matcher: Matcher = pattern.matcher(input)
    return if (matcher.find())
        matcher.group()
    else
        "0"
}

fun generateDimens(multiply: Float, filePath: String) {
    val inputStream: InputStream = File(dimenPath).inputStream()

    inputStream.bufferedReader().useLines { lines ->

        File(filePath).printWriter().use { out ->
            lines.forEach { line ->
                if (line.contains("dimen")) {
                    val start = line.indexOf(">") + 1
                    val end = line.indexOf("</")
                    val dimen = line.substring(start, end)

                    val number = extractNumbers(dimen)

                    val finalValue = "%.1f".format((number.toDouble()) * multiply)
                    val finalDimen = dimen.replace(number, finalValue)

                    out.println(line.replace(dimen, finalDimen))
                } else {
                    out.println(line)
                }
            }
        }
    }

    inputStream.close()

    println("Dimen File Generated Successfully !!")
}
// endregion

fun main(args: Array<String>) {
    //generate Readme
    generateReadme()

    // generate small dimens
    generateDimens(0.8f, dimenSmallPath)

    // generate large dimens
    generateDimens(1.4f, dimenLargePath)
}

// create jar file command:
// kotlinc _generator_helper.kt -include-runtime -d _generator.jar

// call generator command:
// java -jar _generator.jar