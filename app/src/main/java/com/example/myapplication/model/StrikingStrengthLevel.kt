package com.example.myapplication.model

/**
 * Represents the levels for a character's Striking Strength.
 *
 * These levels describe the force and impact behind a character's attack.
 * They range from lower-tier forces (e.g. Low Hypoverse level) to extremely
 * high-impact, reality-altering levels (e.g. High Outerverse level), and include
 * both conventional physical ranges and higher-dimensional scales.
 *
 * Levels:
 * 1. Low Hypoverse level
 * 2. Hypoverse level
 * 3. High Hypoverse level
 * 4. Below Average Human level
 * 5. Human level
 * 6. Athlete level
 * 7. Street level
 * 8. Wall level
 * 9. Small Building level
 * 10. Building level
 * 11. Large Building level
 * 12. City Block level
 * 13. Multi-City Block level
 * 14. Small Town level
 * 15. Town level
 * 16. Large Town level
 * 17. Small City level
 * 18. City level
 * 19. Mountain level
 * 20. Large Mountain level
 * 21. Island level
 * 22. Large Island level
 * 23. Small Country level
 * 24. Country level
 * 25. Large Country level
 * 26. Continent level
 * 27. Multi-Continent level
 * 28. Moon level
 * 29. Small Planet level
 * 30. Planet level
 * 31. Large Planet level
 * 32. Brown Dwarf level
 * 33. Small Star level
 * 34. Star level
 * 35. Large Star level
 * 36. Solar System level
 * 37. Multi-Solar System level
 * 38. Galaxy level
 * 39. Multi-Galaxy level
 * 40. Universe level
 * 41. High Universe level
 * 42. Universe level+
 * 43. Low Multiverse level
 * 44. Multiverse level
 * 45. Multiverse level+
 * 46. Low Complex Multiverse level
 * 47. Complex Multiverse level
 * 48. High Complex Multiverse level
 * 49. Hyperverse level
 * 50. High Hyperverse level
 * 51. Low Outerverse level
 * 52. Outerverse level
 * 53. Outerverse level+
 * 54. High Outerverse level
 * 55. Inapplicable
 */
enum class StrikingStrengthLevel(val label: String) {
    LOW_HYPOVERSE("Low Hypoverse level"),
    HYPOVERSE("Hypoverse level"),
    HIGH_HYPOVERSE("High Hypoverse level"),
    BELOW_AVERAGE_HUMAN("Below Average Human level"),
    HUMAN("Human level"),
    ATHLETE("Athlete level"),
    STREET("Street level"),
    WALL("Wall level"),
    SMALL_BUILDING("Small Building level"),
    BUILDING("Building level"),
    LARGE_BUILDING("Large Building level"),
    CITY_BLOCK("City Block level"),
    MULTI_CITY_BLOCK("Multi-City Block level"),
    SMALL_TOWN("Small Town level"),
    TOWN("Town level"),
    LARGE_TOWN("Large Town level"),
    SMALL_CITY("Small City level"),
    CITY("City level"),
    MOUNTAIN("Mountain level"),
    LARGE_MOUNTAIN("Large Mountain level"),
    ISLAND("Island level"),
    LARGE_ISLAND("Large Island level"),
    SMALL_COUNTRY("Small Country level"),
    COUNTRY("Country level"),
    LARGE_COUNTRY("Large Country level"),
    CONTINENT("Continent level"),
    MULTI_CONTINENT("Multi-Continent level"),
    MOON("Moon level"),
    SMALL_PLANET("Small Planet level"),
    PLANET("Planet level"),
    LARGE_PLANET("Large Planet level"),
    BROWN_DWARF("Brown Dwarf level"),
    SMALL_STAR("Small Star level"),
    STAR("Star level"),
    LARGE_STAR("Large Star level"),
    SOLAR_SYSTEM("Solar System level"),
    MULTI_SOLAR_SYSTEM("Multi-Solar System level"),
    GALAXY("Galaxy level"),
    MULTI_GALAXY("Multi-Galaxy level"),
    UNIVERSE("Universe level"),
    HIGH_UNIVERSE("High Universe level"),
    UNIVERSE_PLUS("Universe level+"),
    LOW_MULTIVERSE("Low Multiverse level"),
    MULTIVERSE("Multiverse level"),
    MULTIVERSE_PLUS("Multiverse level+"),
    LOW_COMPLEX_MULTIVERSE("Low Complex Multiverse level"),
    COMPLEX_MULTIVERSE("Complex Multiverse level"),
    HIGH_COMPLEX_MULTIVERSE("High Complex Multiverse level"),
    HYPERVESE("Hyperverse level"),
    HIGH_HYPERVESE("High Hyperverse level"),
    LOW_OUTERVERSAL("Low Outerverse level"),
    OUTERVERSAL("Outerverse level"),
    OUTERVERSAL_PLUS("Outerverse level+"),
    HIGH_OUTERVERSAL("High Outerverse level"),
    INAPPLICABLE("Inapplicable")
}
