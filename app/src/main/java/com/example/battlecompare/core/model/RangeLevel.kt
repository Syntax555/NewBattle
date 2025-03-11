package com.example.battlecompare.core.model

import kotlinx.serialization.Serializable

/**
 * Represents the levels for a character's Range (attack or ability reach).
 *
 * Range is a measurement that refers to how far the attacks or abilities of a character,
 * weapon, or other entity can efficiently reach on their own.
 *
 * A general guide for categorizing range is as follows:
 *
 * - Below Standard Melee Range: 0 - 50 cm
 *   (Applies to small characters.)
 *
 * - Standard Melee Range: 50 cm - 1 m
 *   (Applies to fighters using primarily arms/legs or short melee weapons.)
 *
 * - Extended Melee Range: 1 - 3 m
 *   (For long melee weapons or extended attacks.)
 *
 * - Several Meters: 3 - 10 m
 *   (For throwing weapons or extremely long melee weapons.)
 *
 * - Tens of Meters: 10 - 100 m
 *   (Typical for larger combatants or slingshot ranges.)
 *
 * - Hundreds of Meters: 100 - 1,000 m
 *   (For bows, crossbows, or unsighted firearm ranges.)
 *
 * - Kilometers: 1 - 10 km
 *   (The width of an average city.)
 *
 * - Tens of Kilometers: 10 - 100 km
 *
 * - Hundreds of Kilometers: 100 - 1,000 km
 *
 * - Thousands of Kilometers: 1,000 - 20,037 km
 *
 * - Planetary: 20,037 - 1,391,400 km
 *   (Half the circumference of Earth starts at ~20,037 km.)
 *
 * - Stellar: 1,391,400 - 50,290,000 km
 *   (For example, the diameter of the Sun.)
 *
 * - Interplanetary: 50,290,000 km - 4.22 LY
 *   (Distances between planets.)
 *
 * - Interstellar: 4.22 - 50,000 LY
 *   (For example, the distance to the nearest star.)
 *
 * - Galactic: 50,000 - 2,500,000 LY
 *   (Approximately the radius of the Milky Way.)
 *
 * - Intergalactic: 2,500,000 - 46.6 billion LY
 *   (Distances between galaxies.)
 *
 * - Universal: 46.6 billion LY and up
 *   (The radius of the observable universe.)
 *
 * Beyond these standard measurements, additional categories quantify abilities that
 * reach into higher-dimensional or abstract spaces:
 *
 * - High Universal: Attacks that can reach anywhere within an infinite 3-D space.
 * - Universal+: Attacks that can reach anywhere within a single 4-dimensional space-time continuum.
 * - Interdimensional: Attacks that reach beyond conventional space-time (into pocket realities, etc.).
 * - Low Multiversal: Reach within 2 to 1,000 separate 4-D space-time continuums.
 * - Multiversal: Reach within 1,001 to any higher finite number of 4-D continuums.
 * - Multiversal+: Reach within an infinite number of 4-D continuums.
 * - Extradimensional: Reach outside a single universe in ways not covered by other ratings.
 * - Low Complex Multiversal: Reach corresponding to one uncountably infinite level above standard universal.
 * - Complex Multiversal: Reach spanning 2 to 5 uncountably infinite levels above standard universal.
 * - High Complex Multiversal: Reach spanning 6 to 7 such levels.
 * - Hyperversal: Reach corresponding to 8 or more uncountably infinite levels (R^12 and up).
 * - High Hyperversal: Reach within an infinite number of dimensions (Hilbert space).
 * - Low Outerversal: Reach that exceeds and encompasses all standard dimensional levels.
 * - Outerversal: Qualitatively above all previously mentioned structures.
 * - Outerversal+: At least countably infinite hierarchical steps above baseline.
 * - High Outerversal: Completely beyond all 1-A hierarchies and extensions.
 * - Boundless: Characters existing beyond any quantitative or qualitative distinction.
 */
@Serializable
enum class RangeLevel(val label: String) {
    BELOW_STANDARD("Below Standard Melee Range"),
    STANDARD("Standard Melee Range"),
    EXTENDED("Extended Melee Range"),
    SEVERAL_METERS("Several Meters"),
    TENS_OF_METERS("Tens of Meters"),
    HUNDREDS_OF_METERS("Hundreds of Meters"),
    KILOMETERS("Kilometers"),
    TENS_OF_KILOMETERS("Tens of Kilometers"),
    HUNDREDS_OF_KILOMETERS("Hundreds of Kilometers"),
    THOUSANDS_OF_KILOMETERS("Thousands of Kilometers"),
    PLANETARY("Planetary"),
    STELLAR("Stellar"),
    INTERPLANETARY("Interplanetary"),
    INTERSTELLAR("Interstellar"),
    GALACTIC("Galactic"),
    INTERGALACTIC("Intergalactic"),
    UNIVERSAL("Universal"),
    HIGH_UNIVERSAL("High Universal"),
    UNIVERSAL_PLUS("Universal+"),
    INTERDIMENSIONAL("Interdimensional"),
    LOW_MULTIVERSAL("Low Multiversal"),
    MULTIVERSAL("Multiversal"),
    MULTIVERSAL_PLUS("Multiversal+"),
    EXTRADIMENSIONAL("Extradimensional"),
    LOW_COMPLEX_MULTIVERSAL("Low Complex Multiversal"),
    COMPLEX_MULTIVERSAL("Complex Multiversal"),
    HIGH_COMPLEX_MULTIVERSAL("High Complex Multiversal"),
    HYPERVERSAL("Hyperversal"),
    HIGH_HYPERVERSAL("High Hyperversal"),
    LOW_OUTERVERSAL("Low Outerversal"),
    OUTERVERSAL("Outerversal"),
    OUTERVERSAL_PLUS("Outerversal+"),
    HIGH_OUTERVERSAL("High Outerversal"),
    BOUNDLESS("Boundless")
}
