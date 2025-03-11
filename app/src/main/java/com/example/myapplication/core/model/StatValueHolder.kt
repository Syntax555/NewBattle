package com.example.myapplication.core.model

/**
 * A common interface for any stat value holder that wraps an enum.
 *
 * @param T The enum type representing the stat levels.
 */
interface StatValueHolder<T : Enum<T>> {
    var value: T
    var modifier: StatModifier?
}
