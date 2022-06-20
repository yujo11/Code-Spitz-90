import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty

fun <T: Any> parserJson(target: T, json: String): T? {
    val lexer = JsonLexer(json)
    lexer.skipWhite()
    return parseObject(lexrt, target)
}

fun <T: Any> T.fromJson(json: String) : T? = parserJson(this, json)

fun <T: Any> parseObject(lexer: JsonLexer, target: T) : T? {
    if (!lexer.isOpenObject()) return null

    lexer.next()

    val props = target::class.members
        .filterIsInstance<KMutableProperty<*>>()
        .associate {
            (it.findAnnotation<Name>()?.name ?: it.name) to it
        }

    while(!lexer.isCloseObject()) {
        lexer.skipWhite()
        val key = lexer.key() ?: return null
        val prop = props[key] ?: return null
        val value = jsonValue(lexer, prop.returnType) ?: return null
        prop.setter.call(target, value)
        lexer.skipWhite()
        if(lexer.isComma()) lexer.next()
    }

    lexer.next()

    return target
}




class JsonLexer (val json: String) {
    val last = json.lastIndex
    var cursor = 0
        private set
    val curr get() = json[cursor]
    fun next() {
        if (cursor < last) ++cursor
    }
    fun skipWhite() {
        while(" \t\n\r".indexOf(curr) != -1) && cursor < last) next()
    }

    fun isOpenObject(): Boolean = '{' == curr
    fun isCloseObject(): Boolean = '}' == curr
    fun isComma() = curr == ','

    fun key(): String? {
        val result = string() ?: return null
        skipWhite()
        if (curr != ':') return null
        next()
        skipWhite()
        return result
    }

    fun string(): String? {
        if (curr != "") return null
        next()
        val start = cursor
        var isSkip = false

        while (isSkip || curr != '"') {
            isSkip = if(isSkip) false else curr == '\\'
            next()
        }

        val result = json.subString(start, cursor)
        next()
        return result
    }

    fun jsonValue(lexer: JsonLexer, type: Ktype) : Any? {
        return when(val cls = type.classifier as? KClass<*> ?: return null) {
            String::class -> lexer.string()
            Int::class -> lexer.int()
            Long::class -> lexer.long()
            Float::class -> lexer.float()
            Double::class -> lexer.double()
            Boolean::class -> lexer.boolean()
            List::class -> parserList {
                lexer, type.arguments[0].type?.classfier as ? KClass<*> ?: return null
            }

            else -> parseObject()
        }
    }
}

class JsonLexer(val json: String) {
    fun number(): String? {
        val start = cursor
        while("-.0123456789".indexOf(curr) != -1) next()
        return if(start == cursor) null else
    }
}