package hw1check

import kotlin.reflect.full.declaredFunctions

/**
 * Дан интерфейс interface TestRunner { fun  runTest(steps: T, test: () -> Unit) }.
 * Класс типа Т, передаваемый в steps: T,
 * содержит методы before* /  after*,
 * которые задают предусловия/чистят данные перед/после теста.
 *
 * class TestAround<T : Any> : TestRunner<T> реализует  Этот интерфейс
 * и (пере)определяет  override fun runTest(steps: T, test: () -> Unit)
 * Затем этот runTest будет вызываться из main с конкретными параметрами
 */
interface TestRunner<T> {
    // Generic в данном случае лучше вынести из функции в описание интерфейса
    // на работу написанного теста это не влияет, но дает возможности
    fun runTest(steps: T, test: () -> Unit)
}

/**
 *  class MyClass<T : Any> : TestRunner<T> реализует интерфейс
 *  и (пере)определяет  override fun runTest(steps: T, test: () -> Unit)
 *  Затем этот runTest будет вызываться из main с конкретными параметрами
 */
class MyClass<T: Any> : TestRunner<T> {
     override fun  runTest(steps: T, test: () -> Unit) {
     ">>> STARTing ===============${steps::class.simpleName}================".log()
         // стоит обращать вниманте на предупреждения IntelliJ
         // рекомендует стремиться к недублированию текста кода
         val stepsMemberFunctions = steps::class.declaredFunctions
         stepsMemberFunctions
             .filter { it.name.startsWith("before") }
             .forEach { it.call(steps) }
         test()
         stepsMemberFunctions
             .filter { it.name.startsWith("after") }
             .forEach { it.call(steps) }
     }
 }

/**
 * Тестовыe данные должны давать на вход теста разные данные,
 * в т.ч граничные. Задача тестовых данных - сломать программу.
 */
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

class EmptyTestClass {}
    class BeforeTwiceAndAfterTwiceTestClass {

        fun afterTest() {
            "afterTest".log()
        }
        fun afterTest2() {
            "afterTest2".log()
        }
        fun beforeTest() {
            "beforeTest".log()
        }
        fun beforeTestTest() {
            "beforeTestTest".log()
        }

    }

fun main() {

    MyClass<Methods>().runTest(Methods()) {
        println("I'm running test")
    }

// примеры вызова DSL
    myClass {
        runTest(Methods()) { println("I'm running test") }
    }

    myClass {
        runTest(EmptyTestClass()) { println("I'm running EmptyTest") }
    }

    myClass {
        runTest(BeforeTwiceAndAfterTwiceTestClass()) { println("I'm running BeforeTwiceAndAfterTwiceTest") }
    }


}

/**
 * Здесь определяется функция DSL  myClass
 * MyClass и есть основной окружающий класс
 * а initializer передаваемая ему функция- в этом примере println("I'm running test")
 * по правилам DSL она выносится за скобки при вызове в лямбду
 * Затем она используется в main()
 */
fun <T : Any> myClass(initializer: MyClass<T>.() -> Unit): MyClass<T> = MyClass<T>().also{ it.initializer() }

/**
 * функция расширения для String с именем .log приименяется см выше
 * логгирование - это не просто печать - log может перенаправляться в разные выводы
 * так это можно сделать в одном месте, не затрагивая стандартный вывод
 */
fun String.log() {
    println("-> $this running...")
}
