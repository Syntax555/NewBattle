package com.example.battlecompare.core.model

import kotlinx.serialization.Serializable

/**
 * Represents a classification or trait for a character key.
 * Examples include: Human Mutate, Scientist, Fugitive on the run, Avenger, Defender.
 */
@Serializable
enum class Classification(val label: String) {
    HUMAN_MUTATE("Human Mutate"),
    SCIENTIST("Scientist"),
    FUGITIVE_ON_THE_RUN("Fugitive on the run"),
    AVENGER("Avenger"),
    DEFENDER("Defender");

    override fun toString(): String = label
}