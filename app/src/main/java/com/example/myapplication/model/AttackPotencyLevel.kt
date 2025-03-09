package com.example.myapplication.model

/**
 * Represents the levels for a character's Attack Potency.
 *
 * Each value contains a descriptive level name.
 */
enum class AttackPotencyLevel(val label: String) {
    BELOW_AVERAGE_HUMAN("Below Average Human"),
    HUMAN("Human"),
    ATHLETE("Athlete"),
    STREET("Street"),
    WALL("Wall"),
    SMALL_BUILDING("Small Building"),
    BUILDING("Building"),
    LARGE_BUILDING("Large Building"),
    CITY_BLOCK("City Block"),
    MULTI_CITY_BLOCK("Multi-City Block"),
    SMALL_TOWN("Small Town"),
    TOWN("Town"),
    LARGE_TOWN("Large Town"),
    SMALL_CITY("Small City"),
    CITY("City"),
    MOUNTAIN("Mountain"),
    LARGE_MOUNTAIN("Large Mountain"),
    ISLAND("Island"),
    LARGE_ISLAND("Large Island"),
    SMALL_COUNTRY("Small Country"),
    COUNTRY("Country"),
    LARGE_COUNTRY("Large Country"),
    CONTINENT("Continent"),
    MULTI_CONTINENT("Multi-Continent"),
    MOON("Moon"),
    SMALL_PLANET("Small Planet"),
    PLANET("Planet"),
    LARGE_PLANET("Large Planet"),
    BROWN_DWARF("Brown Dwarf"),
    SMALL_STAR("Small Star"),
    STAR("Star"),
    LARGE_STAR("Large Star"),
    SOLAR_SYSTEM("Solar System"),
    MULTI_SOLAR_SYSTEM("Multi-Solar System"),
    GALAXY("Galaxy"),
    MULTI_GALAXY("Multi-Galaxy"),
    UNIVERSE("Universe")
}
