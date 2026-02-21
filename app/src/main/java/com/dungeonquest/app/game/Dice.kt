package com.dungeonquest.app.game

import kotlin.random.Random

object Dice {
    private val random = Random

    fun d4(): Int = random.nextInt(1, 5)
    fun d6(): Int = random.nextInt(1, 7)
    fun d8(): Int = random.nextInt(1, 9)
    fun d10(): Int = random.nextInt(1, 11)
    fun d12(): Int = random.nextInt(1, 13)
    fun d20(): Int = random.nextInt(1, 21)
    fun d100(): Int = random.nextInt(1, 101)

    fun roll(sides: Int): Int = random.nextInt(1, sides + 1)

    fun rollMultiple(count: Int, sides: Int): List<Int> =
        (1..count).map { roll(sides) }

    fun rollWithModifier(sides: Int, modifier: Int): RollResult {
        val roll = roll(sides)
        return RollResult(roll, modifier, roll + modifier)
    }

    fun rollD20WithModifier(modifier: Int): RollResult {
        val roll = d20()
        return RollResult(roll, modifier, roll + modifier)
    }

    fun rollDamage(damageDie: Int, modifier: Int): Int {
        val roll = roll(damageDie)
        return maxOf(1, roll + modifier)
    }

    fun rollMultipleDamage(count: Int, sides: Int, modifier: Int = 0): Int {
        val rolls = rollMultiple(count, sides)
        return maxOf(1, rolls.sum() + modifier)
    }

    fun rollHeal(count: Int = 2, sides: Int = 4, bonus: Int = 2): Int {
        return rollMultiple(count, sides).sum() + bonus
    }
}

data class RollResult(
    val naturalRoll: Int,
    val modifier: Int,
    val total: Int
) {
    val isCritical: Boolean get() = naturalRoll == 20
    val isCriticalFail: Boolean get() = naturalRoll == 1

    fun meetsOrBeats(dc: Int): Boolean = total >= dc

    fun toDisplayString(): String {
        val modStr = when {
            modifier > 0 -> "+$modifier"
            modifier < 0 -> "$modifier"
            else -> ""
        }
        return if (modStr.isNotEmpty()) {
            "🎲 $naturalRoll $modStr = $total"
        } else {
            "🎲 $naturalRoll"
        }
    }
}
