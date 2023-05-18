import kotlin.reflect.full.declaredFunctions

interface TestRunner {
    fun <T: Any> runTest(steps: T, test: () -> Unit)
}

class MyClass : TestRunner {
     override fun <T: Any> runTest(steps: T, test: () -> Unit) {
         steps::class.declaredFunctions
             .filter { it.name.startsWith("before") }
             .forEach { it.call(steps) }
         test()
         steps::class.declaredFunctions
             .filter { it.name.startsWith("after") }
             .forEach { it.call(steps) }
     }
 }

class Methods {

        fun beforeTest() {
            println("beforeTest")
        }
        fun beforeTestTest() {
            println("beforeTestTest")
        }
        fun afterTest() {
            println("afterTest")
        }
}

fun main() {
    MyClass().runTest(Methods()) {
        println("I'm running test")
    }
}
