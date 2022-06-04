// 괄호를 a로 치환하면 됨
// 그 다음에 다시 바라보면 됨

val trim = """[^.\d-+*/]""".toRegex()
fun trim(v: String): String {
    return v.replace(trim, "")
}

fun repMtoPM(v:String): String = v.replace("-", "+-")

val groupMD = """((?:\+|\+-)?[.\d]+)([*/])((?:\+|/+-)?[.\d]+)""".toRegex()


// findAll의 return 값은 Sequence<MatchResult> // 정규식에 일치하는 객체
// 내부구조 Sequence<T>.fold<R>(초기값 R){이전까지 합산한 값 R, 현재요소 T -> ... 다음 요소에 넘길 합산값 R } = R
fun foldGroup(v: String): Double = groupMD.findAll(v).fold(0.0){acc, curr ->
    // MatchResult.groupValues = List<String>(전체, 그룹1, 그룹2, 그룹3 ...
    val(_, left, op, right) = curr.groupValues
    val leftValue = left.replace("+", "").toDouble()
    val rightValue = right.replace("+", "").toDouble()
    val result = when(op) {
        "*" -> leftValue * rightValue
        "/" -> leftValue / rightValue
        else -> throw Throwable("invalid operator $op")
    }
    acc + result
}

fun calc(v: String) = foldGroup(repMtoPM(trim(v)))

// 코틀린의 when과 if문은 식이다.
// v:T = when(대상) {
//  값1 -> T
//  값2 -> { ... return T }
//  else -> T
// }
// 위 코드에서 하나라도 return 값이 T가 아니라면 반환값은 any가 된다.

//fun delegate(a: Int, b: (Int) -> Int): Int {
//    return b(a)
//}
//
//delegate(3, { it * 3 })
//delegate(3){it * 3} // -> Passing trailing lambdas

// 1. val sum:(Int, Int) -> Int = {a: Int, b: Int -> a + b}

// 2. val sum:(

// 11. val twice = {a: Int -> a + a} === // val twice: (Int) -> Int = {it + it}

// 코틀린의 정규식 메서드
// findAll의 return 값은 Sequence<MatchResult> // 정규식에 일치하는 객체
// Kotlin의 replace는 JS의 replaceAll과 일치함

// 정규식 문법 정리
// [...] Character Class
// [^...]: Exception Character Class
// \d: digits: 0 ~ 9
// (...) capturing group
// (?:...) non-capturing group
// (..|..) alternative
// ? zero or one = {0, 1}
// + one or more = {1, }

// 꼬물최 같은거 빼고는 Kotlin에서 형을 지정할 의무는 없다. 그러나 컴파일 시간이 늘어난다. // 컴파일 시 strict모드를 통해 형 지정을 강제할 수 있음.