package com.example.myapplication.model

import kotlinx.serialization.Serializable

/**
 * Represents the levels for a character's Lifting Strength.
 *
 * The levels are defined as follows:
 * - Inapplicable: Self-explanatory.
 * - Below Average Human: 0-50 kg (0-0.05 metric tons, 0-490.5 N)
 * - Average Human: 50-80 kg (0.05-0.08 metric tons, 490.5-784.8 N)
 * - Above Average Human: 80-120 kg (0.08-0.12 metric tons, 784.8-1177.2 N)
 * - Athletic Human: 120-227 kg (0.12-0.227 metric tons, 1177.2-2226.87 N)
 * - Peak Human: 227-545.2 kg (0.227-0.5452 metric tons, 2226.87-5348.412 N)
 * - Superhuman: Any level clearly above peak human (exact values indeterminate)
 * - Class 1: 545.2-1000 kg (0.5452-1 metric ton, 5348.412-9810 N)
 * - Class 5: 1000-5000 kg (1-5 metric tons, 9810-4.905×10^4 N)
 * - Class 10: 5000-10^4 kg (5-10 metric tons, 4.905×10^4-9.81×10^4 N)
 * - Class 25: 10^4-2.5×10^4 kg (10-25 metric tons, 9.81×10^4-2.4525×10^5 N)
 * - Class 50: 2.5×10^4-5×10^4 kg (25-50 metric tons, 2.4525×10^5-4.905×10^5 N)
 * - Class 100: 5×10^4-10^5 kg (50-100 metric tons, 4.905×10^5-9.81×10^5 N)
 * - Class K: 10^5-10^6 kg (100-1000 metric tons, 9.81×10^5-9.81×10^6 N)
 * - Class M: 10^6-10^9 kg (1000-10^6 metric tons, 9.81×10^6-9.81×10^9 N)
 * - Class G: 10^9-10^12 kg (10^6-10^9 metric tons, 9.81×10^9-9.81×10^12 N)
 * - Class T: 10^12-10^15 kg (10^9-10^12 metric tons, 9.81×10^12-9.81×10^15 N)
 * - Class P: 10^15-10^18 kg (10^12-10^15 metric tons, 9.81×10^15-9.81×10^18 N)
 * - Class E: 10^18-10^21 kg (10^15-10^18 metric tons, 9.81×10^18-9.81×10^21 N)
 * - Class Z: 10^21-10^24 kg (10^18-10^21 metric tons, 9.81×10^21-9.81×10^24 N)
 * - Class Y: 10^24-10^27 kg (10^21-10^24 metric tons, 9.81×10^24-9.81×10^27 N)
 * - Pre-Stellar: 10^27-2×10^29 kg (10^24-2×10^26 metric tons, 9.81×10^27-1.962×10^30 N)
 * - Stellar: 2×10^29-3.977×10^32 kg (2×10^26-3.977×10^29 metric tons, 1.962×10^30-3.9×10^33 N)
 * - Multi-Stellar: 3.977×10^32-1.6×10^42 kg (3.977×10^29-1.6×10^39 metric tons, 3.9×10^33-1.569×10^43 N)
 * - Galactic: 1.6×10^42-6×10^43 kg (1.6×10^39-6×10^40 metric tons, 1.569×10^43-5.886×10^44 N)
 * - Multi-Galactic: 6×10^43-1.5×10^53 kg (6×10^40-1.5×10^50 metric tons, 5.886×10^44-1.4715×10^54 N)
 * - Universal: ≥1.5×10^53 kg (1.5×10^50+ metric tons, 1.4715×10^54+ N)
 * - Infinite: Infinite weight by 3-dimensional standards.
 * - Immeasurable: Lifting objects wholly superior to 3-dimensional space.
 */

@Serializable
enum class LiftingStrengthLevel(val label: String) {
    INAPPLICABLE("Inapplicable"),
    BELOW_AVERAGE_HUMAN("Below Average Human"),
    AVERAGE_HUMAN("Average Human"),
    ABOVE_AVERAGE_HUMAN("Above Average Human"),
    ATHLETIC_HUMAN("Athletic Human"),
    PEAK_HUMAN("Peak Human"),
    SUPERHUMAN("Superhuman"),
    CLASS_1("Class 1"),
    CLASS_5("Class 5"),
    CLASS_10("Class 10"),
    CLASS_25("Class 25"),
    CLASS_50("Class 50"),
    CLASS_100("Class 100"),
    CLASS_K("Class K"),
    CLASS_M("Class M"),
    CLASS_G("Class G"),
    CLASS_T("Class T"),
    CLASS_P("Class P"),
    CLASS_E("Class E"),
    CLASS_Z("Class Z"),
    CLASS_Y("Class Y"),
    PRE_STELLAR("Pre-Stellar"),
    STELLAR("Stellar"),
    MULTI_STELLAR("Multi-Stellar"),
    GALACTIC("Galactic"),
    MULTI_GALACTIC("Multi-Galactic"),
    UNIVERSAL("Universal"),
    INFINITE("Infinite"),
    IMMEASURABLE("Immeasurable")
}
