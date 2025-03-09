package com.example.myapplication.model

/**
 * Additional modifiers to nuance a stat value.
 *
 * For example: PLUS, AT_LEAST, AT_MOST, LIKELY, POSSIBLY, HIGHER, LOWER, VARIES.
 */
enum class StatModifier(val symbol: String) {
    PLUS("+"),
    AT_LEAST("at least"),
    AT_MOST("at most"),
    LIKELY("likely"),
    POSSIBLY("possibly"),
    HIGHER("higher"),
    LOWER("lower"),
    VARIES("varies");

    fun bonusValue(): Int = when (this) {
        LOWER -> 0
        AT_MOST -> 1
        LIKELY -> 3
        PLUS -> 4
        AT_LEAST, POSSIBLY, HIGHER, VARIES -> 2
    }
}
