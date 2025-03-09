package com.example.myapplication.model

/**
 * Represents a resistance attribute for a character key.
 *
 * Resistances do not directly change stats. Instead, they nullify or reduce the effect of powers,
 * equipment, or attacks from an enemy by matching ability tags.
 *
 * @property name The name of the resistance.
 * @property description A description of the resistance.
 * @property negates A set of ability tags that this resistance negates.
 */
data class Resistance(
    val name: String,
    val description: String,
    val negates: Set<String> = emptySet()
)
