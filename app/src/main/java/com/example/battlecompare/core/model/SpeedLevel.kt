package com.example.battlecompare.core.model

import kotlinx.serialization.Serializable

/**
 * Represents the levels for a character's Speed.
 *
 * These levels describe the character's movement or reaction speed in various units.
 * The values below are provided as labels; numeric ranges are given as context:
 *
 * - Immobile: 0 m/s, 0 Mach, etc.
 * - Below Average Human: 0 - 5 m/s, 0 - 0.0145773 Mach, etc.
 * - Average Human: 5 - 7.7 m/s, 0.0145773 - 0.022449 Mach, etc.
 * - Athletic Human: 7.7 - 10.03 m/s, 0.022449 - 0.029242 Mach, etc.
 * - Peak Human: 10.03 - 12.43 m/s, 0.029242 - 0.036239 Mach, etc.
 * - Superhuman: 12.43 - 34.3 m/s, 0.036239 - 0.1 Mach, etc.
 * - Subsonic (Faster than the Eye): 34.3 - 171.5 m/s, 0.1 - 0.5 Mach, etc.
 * - Subsonic+: 171.5 - 308.7 m/s, 0.5 - 0.9 Mach, etc.
 * - Transonic: 308.7 - 377.3 m/s, 0.9 - 1.1 Mach, etc.
 * - Supersonic: 377.3 - 857.5 m/s, 1.1 - 2.5 Mach, etc.
 * - Supersonic+: 857.5 - 1715 m/s, 2.5 - 5 Mach, etc.
 * - Hypersonic: 1715 - 3430 m/s, 5 - 10 Mach, etc.
 * - Hypersonic+: 3430 - 8575 m/s, 10 - 25 Mach, etc.
 * - High Hypersonic: 8575 - 17150 m/s, 25 - 50 Mach, etc.
 * - High Hypersonic+: 17150 - 34300 m/s, 50 - 100 Mach, etc.
 * - Massively Hypersonic: 34300 - 343000 m/s, 100 - 1000 Mach, etc.
 * - Massively Hypersonic+: 343000 - 2997925 m/s, 1000 - 8740.3 Mach, etc.
 * - Sub-Relativistic: 2997925 - 14989621.4 m/s, 8740.3 - 43701.52 Mach equivalent, etc.
 * - Sub-Relativistic+: 14989621.4 - 2.998e+7 m/s, 43701.52 - 87403 Mach equivalent, etc.
 * - Relativistic: 2.998e+7 - 1.499e+8 m/s, 87403 - 437015 Mach equivalent, etc.
 * - Relativistic+: 1.499e+8 - 299792458 m/s, 437015 - 874030 Mach equivalent, etc.
 * - Speed of Light: 299792458 m/s, by definition.
 * - FTL: 299792458 - 2.998e+9 m/s, (1x - 10x light speed), etc.
 * - FTL+: 2.998e+9 - 2.998e+10 m/s, (10x - 100x light speed), etc.
 * - Massively FTL: 2.998e+10 - 2.998e+11 m/s, (100x - 1000x light speed), etc.
 * - Massively FTL+: 2.998e+11 m/s and up, (1000x+ light speed).
 */

@Serializable
enum class SpeedLevel(val label: String) {
    IMMOBILE("Immobile"),
    BELOW_AVERAGE_HUMAN("Below Average Human"),
    AVERAGE_HUMAN("Average Human"),
    ATHLETIC_HUMAN("Athletic Human"),
    PEAK_HUMAN("Peak Human"),
    SUPERHUMAN("Superhuman"),
    SUBSONIC("Subsonic (Faster than the Eye)"),
    SUBSONIC_PLUS("Subsonic+"),
    TRANSONIC("Transonic"),
    SUPERSONIC("Supersonic"),
    SUPERSONIC_PLUS("Supersonic+"),
    HYPERSONIC("Hypersonic"),
    HYPERSONIC_PLUS("Hypersonic+"),
    HIGH_HYPERSONIC("High Hypersonic"),
    HIGH_HYPERSONIC_PLUS("High Hypersonic+"),
    MASSIVELY_HYPERSONIC("Massively Hypersonic"),
    MASSIVELY_HYPERSONIC_PLUS("Massively Hypersonic+"),
    SUB_RELATIVISTIC("Sub-Relativistic"),
    SUB_RELATIVISTIC_PLUS("Sub-Relativistic+"),
    RELATIVISTIC("Relativistic"),
    RELATIVISTIC_PLUS("Relativistic+"),
    SPEED_OF_LIGHT("Speed of Light"),
    FTL("FTL"),
    FTL_PLUS("FTL+"),
    MASSIVELY_FTL("Massively FTL"),
    MASSIVELY_FTL_PLUS("Massively FTL+")
}
