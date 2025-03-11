package com.example.myapplication.core.model

import kotlinx.serialization.Serializable

/**
 * Represents the levels for a character's Intelligence.
 *
 * These are our official standard terms and must be included in each intelligence section.
 * They help quickly summarize the intellectual scale of our characters and are accompanied
 * by detailed justifications elsewhere (such as impressive feats, references, and scans).
 *
 * Standard levels:
 * - Mindless: No capacity for thought.
 * - Instinctive: Simple, pre-programmed behavior.
 * - Animalistic: Basic reasoning and awareness.
 * - High Animalistic: Highly intelligent animals with notable problem-solving (e.g. dolphins, chimpanzees).
 * - Below Average: Unremarkable intellect.
 * - Average: Typical intelligence.
 * - Above Average: Better than the norm.
 * - Gifted: High reasoning ability, equivalent to real-world experts.
 * - Genius: Exceptional capacity in one or more fields.
 * - Extraordinary Genius: Knowledge spanning multiple disciplines, surpassing normal genius.
 * - Supergenius: Unfathomably superhuman intelligence capable of reality-warping feats.
 * - Nigh-Omniscient: Nearly all-knowing with only minor gaps.
 * - Omniscient: Knows literally everything.
 */
@Serializable
enum class IntelligenceLevel(val label: String) {
    MINDLESS("Mindless"),
    INSTINCTIVE("Instinctive"),
    ANIMALISTIC("Animalistic"),
    HIGH_ANIMALISTIC("High Animalistic"),
    BELOW_AVERAGE("Below Average"),
    AVERAGE("Average"),
    ABOVE_AVERAGE("Above Average"),
    GIFTED("Gifted"),
    GENIUS("Genius"),
    EXTRAORDINARY_GENIUS("Extraordinary Genius"),
    SUPERGENIUS("Supergenius"),
    NIGH_OMNISCIENT("Nigh-Omniscient"),
    OMNISCIENT("Omniscient")
}
