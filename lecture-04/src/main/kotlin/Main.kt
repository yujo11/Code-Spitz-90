fun main(args: Array<String>) {
    val a= Infinity(0){it + 1}
    val limit = 20
    for (i in a ) {
        if(limit-- > 0) println("$i") else break
    }
}