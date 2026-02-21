package com.dungeonquest.app.viewmodel

import androidx.lifecycle.ViewModel
import com.dungeonquest.app.game.Dice
import com.dungeonquest.app.game.GameEngine
import com.dungeonquest.app.game.RollResult
import com.dungeonquest.app.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DiceRollerState(
    val selectedDie: Int = 20,
    val modifier: Int = 0,
    val rollHistory: List<RollResult> = emptyList(),
    val lastRoll: RollResult? = null,
    val isRolling: Boolean = false
)

class GameViewModel : ViewModel() {

    private val engine = GameEngine()

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _diceState = MutableStateFlow(DiceRollerState())
    val diceState: StateFlow<DiceRollerState> = _diceState.asStateFlow()

    private val _showInventory = MutableStateFlow(false)
    val showInventory: StateFlow<Boolean> = _showInventory.asStateFlow()

    private val _lastActionLog = MutableStateFlow<List<String>>(emptyList())
    val lastActionLog: StateFlow<List<String>> = _lastActionLog.asStateFlow()

    // --- Character Creation ---
    fun startNewGame(name: String, race: Race, charClass: CharacterClass) {
        val character = engine.createCharacter(name, race, charClass)
        _gameState.value = GameState(
            character = character,
            currentNodeId = "start",
            gameStarted = true,
            gameLog = listOf("⚔️ ${character.name} der ${race.displayName}-${charClass.displayName} betritt die Welt!")
        )
    }

    // --- Story ---
    fun getCurrentNode(): StoryNode? {
        return engine.getStoryNode(_gameState.value.currentNodeId)
    }

    fun makeChoice(choice: StoryChoice) {
        val oldLog = _gameState.value.gameLog
        val newState = engine.processChoice(_gameState.value, choice)
        _gameState.value = newState
        _lastActionLog.value = newState.gameLog.drop(oldLog.size)

        // Check if new node has combat
        val node = engine.getStoryNode(newState.currentNodeId)
        if (node?.combat != null && newState.combatState == null) {
            _gameState.value = engine.startCombat(_gameState.value, node.combat)
        }
    }

    fun canMakeChoice(choice: StoryChoice): Boolean {
        if (choice.requiredItem != null) {
            return _gameState.value.character.inventory.any { it.id == choice.requiredItem }
        }
        return true
    }

    // --- Combat ---
    fun isInCombat(): Boolean = _gameState.value.combatState != null

    fun combatAttack() {
        _gameState.value = engine.processCombatAttack(_gameState.value)
        processEnemyTurnIfNeeded()
    }

    fun combatDefend() {
        _gameState.value = engine.processCombatDefend(_gameState.value)
        processEnemyTurnIfNeeded()
    }

    fun combatUseItem(item: Item) {
        _gameState.value = engine.processCombatItem(_gameState.value, item)
        processEnemyTurnIfNeeded()
    }

    fun combatFlee() {
        _gameState.value = engine.processCombatFlee(_gameState.value)
        if (!(_gameState.value.combatState?.isOver ?: true)) {
            processEnemyTurnIfNeeded()
        }
    }

    private fun processEnemyTurnIfNeeded() {
        val combat = _gameState.value.combatState ?: return
        if (!combat.isOver && !combat.isPlayerTurn) {
            _gameState.value = engine.processEnemyTurn(_gameState.value)
        }
    }

    fun endCombat() {
        _gameState.value = engine.endCombat(_gameState.value)
    }

    // --- Inventory ---
    fun toggleInventory() {
        _showInventory.value = !_showInventory.value
    }

    fun equipItem(item: Item) {
        val newChar = engine.equipItem(_gameState.value.character, item)
        _gameState.value = _gameState.value.copy(character = newChar)
    }

    fun useItem(item: Item) {
        val oldChar = _gameState.value.character
        val newChar = engine.useItem(oldChar, item)
        _gameState.value = _gameState.value.copy(character = newChar)

        if (newChar.currentHp > oldChar.currentHp) {
            val healed = newChar.currentHp - oldChar.currentHp
            _lastActionLog.value = listOf("💚 $healed HP geheilt! (${newChar.currentHp}/${newChar.maxHp})")
        }
    }

    // --- Dice Roller ---
    fun selectDie(sides: Int) {
        _diceState.value = _diceState.value.copy(selectedDie = sides)
    }

    fun setModifier(mod: Int) {
        _diceState.value = _diceState.value.copy(modifier = mod)
    }

    fun rollDice() {
        val state = _diceState.value
        val result = Dice.rollWithModifier(state.selectedDie, state.modifier)
        _diceState.value = state.copy(
            lastRoll = result,
            rollHistory = (listOf(result) + state.rollHistory).take(20),
            isRolling = true
        )
    }

    fun finishRollAnimation() {
        _diceState.value = _diceState.value.copy(isRolling = false)
    }

    fun clearRollHistory() {
        _diceState.value = _diceState.value.copy(rollHistory = emptyList())
    }

    // --- Game Management ---
    fun resetGame() {
        _gameState.value = GameState()
        _showInventory.value = false
        _lastActionLog.value = emptyList()
    }

    fun isGameOver(): Boolean {
        val node = getCurrentNode()
        return node?.isEnding == true || _gameState.value.character.currentHp <= 0
    }
}
