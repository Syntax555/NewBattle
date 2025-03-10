package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * Additional modifiers to nuance a stat value.
 *
 * For example: PLUS, AT_LEAST, AT_MOST, LIKELY, POSSIBLY, HIGHER, LOWER, VARIES.
 */
@Serializable
enum class StatModifier(val symbol: String, val bonusValue: Int) {
    PLUS("+", 4),
    AT_LEAST("at least", 2),
    AT_MOST("at most", 1),
    LIKELY("likely", 3),
    POSSIBLY("possibly", 2),
    HIGHER("higher", 2),
    LOWER("lower", 0),
    VARIES("varies", 2);
}