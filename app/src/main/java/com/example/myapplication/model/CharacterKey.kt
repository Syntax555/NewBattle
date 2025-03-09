package com.example.myapplication.model

/**
 * Represents a specific variant (or "key") of a character.
 *
 * @property name The key name (e.g., "Bruce Banner", "Grey Hulk").
 * @property origin The origin of the character (e.g., MARVEL, DC).
 * @property gender The character’s gender.
 * @property classifications A set of classifications (e.g., SCIENTIST, PHYSICIST).
 * @property imagePath The local file path or asset name for the key's image.
 * @property statTiers The tiered statistics for this key.
 * @property powers A list of powers that this key has.
 * @property resistances A list of resistances for this key.
 * @property equipment A list of equipment items associated with this key.
 * @property attacks A list of attacks that this key can perform.
 */
data class CharacterKey(
    val name: String,
    val origin: Origin,
    val gender: Gender,
    val classifications: Set<Classification>,
    val imagePath: String,
    val statTiers: StatTiers,
    val powers: List<Power> = emptyList(),
    val resistances: List<Resistance> = emptyList(),
    val equipment: List<Equipment> = emptyList(),
    val attacks: List<Attack> = emptyList()
)
