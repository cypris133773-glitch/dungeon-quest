package com.dungeonquest.app.game

import com.dungeonquest.app.model.*

class CombatEngine {

    fun initCombat(enemy: Enemy): CombatState {
        return CombatState(
            enemy = enemy,
            enemyCurrentHp = enemy.maxHp,
            isPlayerTurn = true,
            combatLog = listOf(
                "⚔️ Kampf beginnt: ${enemy.name}!",
                enemy.description
            ),
            round = 1
        )
    }

    fun playerAttack(state: CombatState, character: PlayerCharacter): Pair<CombatState, PlayerCharacter> {
        val attackRoll = Dice.rollD20WithModifier(character.attackBonus)
        val log = mutableListOf<String>()

        log.add("--- Runde ${state.round}: Dein Angriff ---")
        log.add("Angriffswurf: ${attackRoll.toDisplayString()} gegen RK ${state.enemy.armorClass}")

        var newEnemyHp = state.enemyCurrentHp
        var updatedCharacter = character

        when {
            attackRoll.isCriticalFail -> {
                log.add("💀 Kritischer Fehlschlag! Du stolperst und verfehlst komplett!")
            }
            attackRoll.isCritical -> {
                val damage = Dice.rollDamage(character.weaponDamage, character.damageBonus) * 2
                newEnemyHp = maxOf(0, newEnemyHp - damage)
                log.add("⚡ KRITISCHER TREFFER! Du verursachst $damage Schaden! (Doppelter Schaden!)")
            }
            attackRoll.meetsOrBeats(state.enemy.armorClass) -> {
                val damage = Dice.rollDamage(character.weaponDamage, character.damageBonus)
                newEnemyHp = maxOf(0, newEnemyHp - damage)
                log.add("✓ Treffer! Du verursachst $damage Schaden!")
            }
            else -> {
                log.add("✗ Daneben! Dein Angriff verfehlt das Ziel.")
            }
        }

        log.add("${state.enemy.name}: $newEnemyHp/${state.enemy.maxHp} HP")

        val isOver = newEnemyHp <= 0
        if (isOver) {
            log.add("")
            log.add("🏆 ${state.enemy.name} wurde besiegt!")
            log.add("Du erhältst ${state.enemy.xpReward} XP und ${state.enemy.goldReward} Gold!")
            updatedCharacter = updatedCharacter.copy(
                xp = updatedCharacter.xp + state.enemy.xpReward,
                gold = updatedCharacter.gold + state.enemy.goldReward,
                inventory = updatedCharacter.inventory + state.enemy.loot
            )
            if (state.enemy.loot.isNotEmpty()) {
                log.add("Beute: ${state.enemy.loot.joinToString { "${it.icon} ${it.name}" }}")
            }
        }

        val newState = state.copy(
            enemyCurrentHp = newEnemyHp,
            isPlayerTurn = if (isOver) true else false,
            combatLog = state.combatLog + log,
            isOver = isOver,
            playerWon = isOver,
            playerDefending = false
        )

        return Pair(newState, updatedCharacter)
    }

    fun playerDefend(state: CombatState): CombatState {
        val log = listOf(
            "--- Runde ${state.round}: Verteidigung ---",
            "🛡️ Du gehst in Verteidigungsstellung! (+2 RK diese Runde)"
        )
        return state.copy(
            isPlayerTurn = false,
            combatLog = state.combatLog + log,
            playerDefending = true
        )
    }

    fun playerUseItem(state: CombatState, character: PlayerCharacter, item: Item): Pair<CombatState, PlayerCharacter> {
        val log = mutableListOf<String>()
        var updatedCharacter = character

        log.add("--- Runde ${state.round}: Gegenstand ---")

        when (item.type) {
            ItemType.POTION -> {
                val healRoll = if (item.healAmount >= 20) {
                    Dice.rollMultipleDamage(4, 4, 4)
                } else {
                    Dice.rollMultipleDamage(2, 4, 2)
                }
                val newHp = minOf(updatedCharacter.maxHp, updatedCharacter.currentHp + healRoll)
                log.add("🧪 Du trinkst ${item.name} und heilst $healRoll HP!")
                log.add("HP: ${updatedCharacter.currentHp} → $newHp/${updatedCharacter.maxHp}")
                updatedCharacter = updatedCharacter.copy(
                    currentHp = newHp,
                    inventory = updatedCharacter.inventory.toMutableList().also {
                        val idx = it.indexOf(item)
                        if (idx >= 0) it.removeAt(idx)
                    }
                )
            }
            ItemType.SCROLL -> {
                val damage = Dice.rollMultipleDamage(3, 6)
                val newEnemyHp = maxOf(0, state.enemyCurrentHp - damage)
                log.add("📜 Du verwendest ${item.name}! $damage Schaden!")
                updatedCharacter = updatedCharacter.copy(
                    inventory = updatedCharacter.inventory.toMutableList().also {
                        val idx = it.indexOf(item)
                        if (idx >= 0) it.removeAt(idx)
                    }
                )

                val isOver = newEnemyHp <= 0
                if (isOver) {
                    log.add("🏆 ${state.enemy.name} wurde besiegt!")
                    updatedCharacter = updatedCharacter.copy(
                        xp = updatedCharacter.xp + state.enemy.xpReward,
                        gold = updatedCharacter.gold + state.enemy.goldReward,
                        inventory = updatedCharacter.inventory + state.enemy.loot
                    )
                }

                return Pair(
                    state.copy(
                        enemyCurrentHp = newEnemyHp,
                        isPlayerTurn = if (isOver) true else false,
                        combatLog = state.combatLog + log,
                        isOver = isOver,
                        playerWon = isOver
                    ),
                    updatedCharacter
                )
            }
            else -> {
                log.add("Du kannst ${item.name} im Kampf nicht verwenden.")
                return Pair(state.copy(combatLog = state.combatLog + log), character)
            }
        }

        return Pair(
            state.copy(
                isPlayerTurn = false,
                combatLog = state.combatLog + log,
                playerDefending = false
            ),
            updatedCharacter
        )
    }

    fun playerFlee(state: CombatState, character: PlayerCharacter): Pair<CombatState, Boolean> {
        val fleeCheck = Dice.rollD20WithModifier(character.stats.dexMod)
        val log = mutableListOf<String>()

        log.add("--- Fluchtversuch ---")
        log.add("Geschicklichkeitswurf: ${fleeCheck.toDisplayString()} gegen SG 12")

        return if (fleeCheck.meetsOrBeats(12)) {
            log.add("🏃 Du fliehst erfolgreich aus dem Kampf!")
            Pair(
                state.copy(
                    combatLog = state.combatLog + log,
                    isOver = true,
                    playerWon = false
                ),
                true
            )
        } else {
            log.add("✗ Flucht fehlgeschlagen! Der Gegner versperrt den Weg!")
            Pair(
                state.copy(
                    isPlayerTurn = false,
                    combatLog = state.combatLog + log,
                    playerDefending = false
                ),
                false
            )
        }
    }

    fun enemyTurn(state: CombatState, character: PlayerCharacter): Pair<CombatState, PlayerCharacter> {
        val log = mutableListOf<String>()
        var updatedCharacter = character
        val enemy = state.enemy

        log.add("")
        log.add("--- ${enemy.name} greift an! ---")

        // Check for special ability
        val useSpecial = !state.enemySpecialUsed &&
                enemy.specialAbility != null &&
                (state.round >= 2 || state.enemyCurrentHp < enemy.maxHp / 2)

        if (useSpecial && enemy.specialAbility != null) {
            log.add("💥 ${enemy.name} setzt Spezialfähigkeit ein!")
            log.add(enemy.specialAbility)

            val specialDamage = when (enemy.id) {
                "goblin_shaman" -> Dice.rollMultipleDamage(2, 4)
                "giant_spider" -> Dice.rollMultipleDamage(2, 4)
                "dark_mage" -> Dice.rollMultipleDamage(3, 6)
                "necromancer" -> {
                    val heal = 10
                    log.add("${enemy.name} heilt sich um $heal HP!")
                    Dice.rollMultipleDamage(2, 6)
                }
                "dragon" -> Dice.rollMultipleDamage(4, 6)
                else -> Dice.rollMultipleDamage(2, 6)
            }

            val defenseBonus = if (state.playerDefending) 2 else 0
            val saveRoll = Dice.rollD20WithModifier(character.stats.wisMod)
            val saveDc = 10 + enemy.attackBonus / 2

            if (saveRoll.meetsOrBeats(saveDc)) {
                val reducedDamage = specialDamage / 2
                log.add("Rettungswurf: ${saveRoll.toDisplayString()} ✓ Halber Schaden: $reducedDamage")
                updatedCharacter = updatedCharacter.copy(
                    currentHp = maxOf(0, updatedCharacter.currentHp - reducedDamage)
                )
            } else {
                log.add("Rettungswurf: ${saveRoll.toDisplayString()} ✗ Voller Schaden: $specialDamage")
                updatedCharacter = updatedCharacter.copy(
                    currentHp = maxOf(0, updatedCharacter.currentHp - specialDamage)
                )
            }

            val newEnemyHp = if (enemy.id == "necromancer") {
                minOf(enemy.maxHp, state.enemyCurrentHp + 10)
            } else {
                state.enemyCurrentHp
            }

            log.add("Deine HP: ${updatedCharacter.currentHp}/${updatedCharacter.maxHp}")

            val isOver = updatedCharacter.currentHp <= 0
            if (isOver) {
                log.add("")
                log.add("☠️ Du wurdest besiegt...")
            }

            return Pair(
                state.copy(
                    enemyCurrentHp = newEnemyHp,
                    isPlayerTurn = !isOver,
                    combatLog = state.combatLog + log,
                    isOver = isOver,
                    playerWon = false,
                    round = state.round + 1,
                    enemySpecialUsed = true,
                    playerDefending = false
                ),
                updatedCharacter
            )
        }

        // Normal attack
        val effectiveAC = character.armorClass + if (state.playerDefending) 2 else 0
        val attackRoll = Dice.rollD20WithModifier(enemy.attackBonus)

        log.add("Angriffswurf: ${attackRoll.toDisplayString()} gegen deine RK $effectiveAC")

        when {
            attackRoll.isCriticalFail -> {
                log.add("💀 Kritischer Fehlschlag! ${enemy.name} stolpert!")
            }
            attackRoll.isCritical -> {
                val damage = Dice.rollDamage(enemy.damage, 0) * 2
                updatedCharacter = updatedCharacter.copy(
                    currentHp = maxOf(0, updatedCharacter.currentHp - damage)
                )
                log.add("⚡ KRITISCHER TREFFER! ${enemy.name} verursacht $damage Schaden!")
            }
            attackRoll.meetsOrBeats(effectiveAC) -> {
                val damage = Dice.rollDamage(enemy.damage, 0)
                updatedCharacter = updatedCharacter.copy(
                    currentHp = maxOf(0, updatedCharacter.currentHp - damage)
                )
                log.add("✓ Treffer! ${enemy.name} verursacht $damage Schaden!")
            }
            else -> {
                log.add("✗ ${enemy.name} verfehlt!")
            }
        }

        log.add("Deine HP: ${updatedCharacter.currentHp}/${updatedCharacter.maxHp}")

        val isOver = updatedCharacter.currentHp <= 0
        if (isOver) {
            log.add("")
            log.add("☠️ Du wurdest besiegt...")
        }

        return Pair(
            state.copy(
                isPlayerTurn = !isOver,
                combatLog = state.combatLog + log,
                isOver = isOver,
                playerWon = false,
                round = state.round + 1,
                playerDefending = false
            ),
            updatedCharacter
        )
    }
}
