package woowacourse.movie.common.database

sealed class SqlType {
    object INTEGER : SqlType() {
        override fun toString(): String = "INTEGER"
    }

    object TEXT : SqlType() {
        override fun toString(): String = "TEXT"
    }
}
