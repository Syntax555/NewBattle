package com.example.myapplication.model

/**
 * A common interface for any entity that can disable abilities or effects.
 * The disables property is defined as an immutable Set for efficient membership checking.
 */
interface DisableProvider {
    val disables: Set<String>
}
