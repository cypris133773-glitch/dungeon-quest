package com.dungeonquest.app.game

import com.dungeonquest.app.model.*

class GameEngine {

    private val storyNodes = StoryData.getAllNodes()
    val combatEngine = CombatEngine()

    fun createCharacter(name: String, race: Race, charClass: CharacterClass): PlayerCharacter {
        val combinedStats = charClass.baseStats + race.statBonus
        val maxHp = charClass.hitDie + combinedStats.conMod + 4 // Bonus for starting
        val starterItems = Items.starterItems(charClass)
        val weapon = starterItems.firstOrNull { it.type == ItemType.WEAPON }
        val armor = starterItems.firstOrNull { it.type == ItemType.ARMOR }

        return PlayerCharacter(
            name = name,
            race = race,
            charClass = charClass,
            level = 1,
            xp = 0,
            maxHp = maxHp,
            currentHp = maxHp,
            stats = combinedStats,
            baseArmorClass = 10,
            inventory = starterItems,
            equippedWeapon = weapon,
            equippedArmor = armor,
            gold = 10
        )
    }

    fun getStoryNode(nodeId: String): StoryNode? = storyNodes[nodeId]

    fun processChoice(
        gameState: GameState,
        choice: StoryChoice
    ): GameState {
        var character = gameState.character
        val log = mutableListOf<String>()

        // Check required item
        if (choice.requiredItem != null) {
            val hasItem = character.inventory.any { it.id == choice.requiredItem }
            if (!hasItem) {
                log.add("⚠️ Du brauchst einen bestimmten Gegenstand dafür!")
                return gameState.copy(gameLog = gameState.gameLog + log)
            }
        }

        // Handle skill check
        if (choice.skillCheck != null) {
            val check = choice.skillCheck
            val modifier = character.getSkillModifier(check.skill)
            val roll = Dice.rollD20WithModifier(modifier)

            log.add("${check.skill.displayName}-Probe (SG ${check.dc}): ${roll.toDisplayString()}")

            if (!roll.meetsOrBeats(check.dc)) {
                log.add("✗ Fehlgeschlagen!")
                val failNode = choice.failNodeId ?: choice.nextNodeId
                return gameState.copy(
                    currentNodeId = failNode,
                    visitedNodes = gameState.visitedNodes + failNode,
                    gameLog = gameState.gameLog + log
                )
            }
            log.add("✓ Erfolgreich!")
        }

        // Give rewards
        if (choice.giveXp > 0) {
            character = character.copy(xp = character.xp + choice.giveXp)
            log.add("+${choice.giveXp} XP erhalten!")
        }

        if (choice.giveGold > 0) {
            character = character.copy(gold = character.gold + choice.giveGold)
            log.add("+${choice.giveGold} Gold erhalten!")
        }

        if (choice.giveItem != null) {
            character = character.copy(
                inventory = character.inventory + choice.giveItem
            )
            log.add("${choice.giveItem.icon} ${choice.giveItem.name} erhalten!")

            // Auto-equip if better weapon
            if (choice.giveItem.type == ItemType.WEAPON) {
                val currentDamage = character.equippedWeapon?.damage ?: 0
                if (choice.giveItem.damage > currentDamage) {
                    character = character.copy(equippedWeapon = choice.giveItem)
                    log.add("${choice.giveItem.name} ausgerüstet!")
                }
            }
            if (choice.giveItem.type == ItemType.ARMOR) {
                val currentArmor = character.equippedArmor?.armorBonus ?: 0
                if (choice.giveItem.armorBonus > currentArmor) {
                    character = character.copy(equippedArmor = choice.giveItem)
                    log.add("${choice.giveItem.name} angelegt!")
                }
            }
        }

        if (choice.healAmount > 0) {
            val healedHp = if (choice.healAmount >= 999) {
                character.maxHp
            } else {
                minOf(character.maxHp, character.currentHp + choice.healAmount)
            }
            character = character.copy(currentHp = healedHp)
            if (choice.healAmount >= 999) {
                log.add("💚 Vollständig geheilt!")
            } else {
                log.add("💚 ${choice.healAmount} HP geheilt!")
            }
        }

        // Check for level up
        character = checkLevelUp(character, log)

        return gameState.copy(
            character = character,
            currentNodeId = choice.nextNodeId,
            visitedNodes = gameState.visitedNodes + choice.nextNodeId,
            gameLog = gameState.gameLog + log
        )
    }

    fun startCombat(gameState: GameState, enemy: Enemy): GameState {
        val combat = combatEngine.initCombat(enemy)
        return gameState.copy(combatState = combat)
    }

    fun processCombatAttack(gameState: GameState): GameState {
        val combat = gameState.combatState ?: return gameState
        val (newCombat, newChar) = combatEngine.playerAttack(combat, gameState.character)
        return gameState.copy(combatState = newCombat, character = newChar)
    }

    fun processCombatDefend(gameState: GameState): GameState {
        val combat = gameState.combatState ?: return gameState
        val newCombat = combatEngine.playerDefend(combat)
        return gameState.copy(combatState = newCombat)
    }

    fun processCombatItem(gameState: GameState, item: Item): GameState {
        val combat = gameState.combatState ?: return gameState
        val (newCombat, newChar) = combatEngine.playerUseItem(combat, gameState.character, item)
        return gameState.copy(combatState = newCombat, character = newChar)
    }

    fun processCombatFlee(gameState: GameState): GameState {
        val combat = gameState.combatState ?: return gameState
        val (newCombat, fled) = combatEngine.playerFlee(combat, gameState.character)
        return gameState.copy(combatState = newCombat)
    }

    fun processEnemyTurn(gameState: GameState): GameState {
        val combat = gameState.combatState ?: return gameState
        val (newCombat, newChar) = combatEngine.enemyTurn(combat, gameState.character)
        return gameState.copy(combatState = newCombat, character = newChar)
    }

    fun endCombat(gameState: GameState): GameState {
        val combat = gameState.combatState ?: return gameState
        val node = getStoryNode(gameState.currentNodeId)
        val afterNodeId = node?.afterCombatNodeId

        var character = checkLevelUp(gameState.character, mutableListOf())

        return if (combat.playerWon && afterNodeId != null) {
            gameState.copy(
                character = character,
                currentNodeId = afterNodeId,
                visitedNodes = gameState.visitedNodes + afterNodeId,
                combatState = null
            )
        } else {
            gameState.copy(
                character = character,
                combatState = null
            )
        }
    }

    fun equipItem(character: PlayerCharacter, item: Item): PlayerCharacter {
        return when (item.type) {
            ItemType.WEAPON -> character.copy(equippedWeapon = item)
            ItemType.ARMOR -> character.copy(equippedArmor = item)
            else -> character
        }
    }

    fun useItem(character: PlayerCharacter, item: Item): PlayerCharacter {
        if (!item.isConsumable) return character

        var updated = character
        if (item.type == ItemType.POTION && item.healAmount > 0) {
            val heal = if (item.healAmount >= 20) {
                Dice.rollMultipleDamage(4, 4, 4)
            } else {
                Dice.rollMultipleDamage(2, 4, 2)
            }
            updated = updated.copy(
                currentHp = minOf(updated.maxHp, updated.currentHp + heal)
            )
        }

        updated = updated.copy(
            inventory = updated.inventory.toMutableList().also {
                val idx = it.indexOf(item)
                if (idx >= 0) it.removeAt(idx)
            }
        )
        return updated
    }

    private fun checkLevelUp(character: PlayerCharacter, log: MutableList<String>): PlayerCharacter {
        var c = character
        while (c.xp >= c.xpToNextLevel && c.level < 10) {
            val newLevel = c.level + 1
            val hpGain = Dice.roll(c.charClass.hitDie) + c.stats.conMod
            c = c.copy(
                level = newLevel,
                xp = c.xp - c.xpToNextLevel,
                maxHp = c.maxHp + maxOf(1, hpGain),
                currentHp = c.currentHp + maxOf(1, hpGain)
            )
            log.add("⬆️ LEVEL UP! Du bist jetzt Level $newLevel! (+$hpGain HP)")
        }
        return c
    }
}
