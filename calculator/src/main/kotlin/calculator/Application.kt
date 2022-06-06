package calculator

fun main() {
    println("test")
    println("${calculator("2 * 8  ")} expect: 16")
    println("${calculator("2 * 8  + 4")} expect: 20")
    println("${calculator("2 * (8  + 4)")} expect: 24")
    println("${calculator("2 * (8  + 4)")} expect: 24")
}
