package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * Represents a specific tier level.
 *
 * Each level is defined by a label and a full description.
 * For example:
 * - 11-C: Low Hypoverse level
 * - 10-A: Athlete level
 * - 0: Boundless
 */
@Serializable
enum class TierLevel(val label: String, val description: String) {
    // Tier 11
    _11C("11-C", "Low Hypoverse level"),
    _11B("11-B", "Hypoverse level"),
    _11A("11-A", "High Hypoverse level"),
    // Tier 10
    _10C("10-C", "Below Average Human level"),
    _10B("10-B", "Human level"),
    _10A("10-A", "Athlete level"),
    // Tier 9
    _9C("9-C", "Street level"),
    _9B("9-B", "Wall level"),
    _9A("9-A", "Small Building level"),
    // Tier 8
    _8C("8-C", "Building level"),
    HIGH_8C("High 8-C", "Large Building level"),
    _8B("8-B", "City Block level"),
    _8A("8-A", "Multi-City Block level"),
    // Tier 7
    LOW_7C("Low 7-C", "Small Town level"),
    _7C("7-C", "Town level"),
    HIGH_7C("High 7-C", "Large Town level"),
    LOW_7B("Low 7-B", "Small City level"),
    _7B("7-B", "City level"),
    _7A("7-A", "Mountain level"),
    HIGH_7A("High 7-A", "Large Mountain level"),
    // Tier 6
    _6C("6-C", "Island level"),
    HIGH_6C("High 6-C", "Large Island level"),
    LOW_6B("Low 6-B", "Small Country level"),
    _6B("6-B", "Country level"),
    HIGH_6B("High 6-B", "Large Country level"),
    _6A("6-A", "Continent level"),
    HIGH_6A("High 6-A", "Multi-Continent level"),
    // Tier 5
    _5C("5-C", "Moon level"),
    LOW_5B("Low 5-B", "Small Planet level"),
    _5B("5-B", "Planet level"),
    _5A("5-A", "Large Planet level"),
    HIGH_5A("High 5-A", "Brown Dwarf level"),
    // Tier 4
    LOW_4C("Low 4-C", "Small Star level"),
    _4C("4-C", "Star level"),
    HIGH_4C("High 4-C", "Large Star level"),
    _4B("4-B", "Solar System level"),
    _4A("4-A", "Multi-Solar System level"),
    // Tier 3
    _3C("3-C", "Galaxy level"),
    _3B("3-B", "Multi-Galaxy level"),
    _3A("3-A", "Universe level"),
    HIGH_3A("High 3-A", "High Universe level"),
    // Tier 2
    LOW_2C("Low 2-C", "Universe level+"),
    _2C("2-C", "Low Multiverse level"),
    _2B("2-B", "Multiverse level"),
    _2A("2-A", "Multiverse level+"),
    // Tier 1
    LOW_1C("Low 1-C", "Low Complex Multiverse level"),
    _1C("1-C", "Complex Multiverse level"),
    HIGH_1C("High 1-C", "High Complex Multiverse level"),
    _1B("1-B", "Hyperverse level"),
    HIGH_1B("High 1-B", "High Hyperverse level"),
    LOW_1A("Low 1-A", "Low Outerverse level"),
    _1A("1-A", "Outerverse level"),
    HIGH_1A("High 1-A", "High Outerverse level"),
    // Boundless
    BOUNDLESS("0", "Boundless")
}
