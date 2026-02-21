package com.dungeonquest.app.model

// --- Character Classes ---
enum class CharacterClass(
    val displayName: String,
    val description: String,
    val hitDie: Int,
    val baseStats: Stats,
    val icon: String
) {
    KRIEGER(
        displayName = "Krieger",
        description = "Ein mächtiger Kämpfer, stark in Nahkampf und Rüstung.",
        hitDie = 10,
        baseStats = Stats(str = 16, dex = 12, con = 14, int = 8, wis = 10, cha = 10),
        icon = "⚔️"
    ),
    MAGIER(
        displayName = "Magier",
        description = "Ein Meister der arkanen Künste mit verheerender Magie.",
        hitDie = 6,
        baseStats = Stats(str = 8, dex = 12, con = 10, int = 16, wis = 14, cha = 10),
        icon = "🔮"
    ),
    SCHURKE(
        displayName = "Schurke",
        description = "Ein flinker Meister der Schatten und des Betrugs.",
        hitDie = 8,
        baseStats = Stats(str = 10, dex = 16, con = 12, int = 12, wis = 10, cha = 14),
        icon = "🗡️"
    ),
    WALDLAEUFER(
        displayName = "Waldläufer",
        description = "Ein Jäger der Wildnis, bewandert in Natur und Bogen.",
        hitDie = 10,
        baseStats = Stats(str = 12, dex = 14, con = 12, int = 10, wis = 16, cha = 8),
        icon = "🏹"
    )
}

// --- Races ---
enum class Race(
    val displayName: String,
    val description: String,
    val statBonus: Stats
) {
    MENSCH(
        displayName = "Mensch",
        description = "Vielseitig und anpassungsfähig. +1 auf alle Werte.",
        statBonus = Stats(str = 1, dex = 1, con = 1, int = 1, wis = 1, cha = 1)
    ),
    ELF(
        displayName = "Elf",
        description = "Anmutig und weise. +2 GES, +1 WEI.",
        statBonus = Stats(str = 0, dex = 2, con = 0, int = 0, wis = 1, cha = 0)
    ),
    ZWERG(
        displayName = "Zwerg",
        description = "Zäh und widerstandsfähig. +2 KON, +1 STR.",
        statBonus = Stats(str = 1, dex = 0, con = 2, int = 0, wis = 0, cha = 0)
    ),
    HALBLING(
        displayName = "Halbling",
        description = "Klein, flink und glücklich. +2 GES, +1 CHA.",
        statBonus = Stats(str = 0, dex = 2, con = 0, int = 0, wis = 0, cha = 1)
    )
}

// --- Stats ---
data class Stats(
    val str: Int = 10,
    val dex: Int = 10,
    val con: Int = 10,
    val int: Int = 10,
    val wis: Int = 10,
    val cha: Int = 10
) {
    operator fun plus(other: Stats) = Stats(
        str = str + other.str,
        dex = dex + other.dex,
        con = con + other.con,
        int = int + other.int,
        wis = wis + other.wis,
        cha = cha + other.cha
    )

    fun modifier(stat: Int): Int = (stat - 10) / 2

    val strMod get() = modifier(str)
    val dexMod get() = modifier(dex)
    val conMod get() = modifier(con)
    val intMod get() = modifier(int)
    val wisMod get() = modifier(wis)
    val chaMod get() = modifier(cha)
}

// --- Items ---
enum class ItemType { WEAPON, ARMOR, POTION, RING, SCROLL, QUEST_ITEM }

data class Item(
    val id: String,
    val name: String,
    val description: String,
    val type: ItemType,
    val icon: String,
    val damage: Int = 0,
    val armorBonus: Int = 0,
    val healAmount: Int = 0,
    val statBonus: Stats = Stats(0, 0, 0, 0, 0, 0),
    val value: Int = 0,
    val isConsumable: Boolean = false
)

// --- Predefined Items ---
object Items {
    val rostigesSchwert = Item(
        id = "rusty_sword", name = "Rostiges Schwert",
        description = "Ein altes, aber noch brauchbares Schwert.",
        type = ItemType.WEAPON, icon = "⚔️", damage = 4, value = 5
    )
    val langSchwert = Item(
        id = "longsword", name = "Langschwert",
        description = "Ein gut geschmiedetes Langschwert. 1W8 Schaden.",
        type = ItemType.WEAPON, icon = "⚔️", damage = 8, value = 15
    )
    val magierstab = Item(
        id = "staff", name = "Magierstab",
        description = "Ein knorriger Stab, der vor Magie pulsiert. 1W6 Schaden.",
        type = ItemType.WEAPON, icon = "🪄", damage = 6, value = 10
    )
    val dolch = Item(
        id = "dagger", name = "Dolch",
        description = "Ein scharfer Dolch. Schnell und tödlich. 1W4 Schaden.",
        type = ItemType.WEAPON, icon = "🗡️", damage = 4, value = 2
    )
    val langbogen = Item(
        id = "longbow", name = "Langbogen",
        description = "Ein eleganter Langbogen. 1W8 Schaden.",
        type = ItemType.WEAPON, icon = "🏹", damage = 8, value = 15
    )
    val feuerSchwert = Item(
        id = "fire_sword", name = "Flammenschwert",
        description = "Eine legendäre Klinge, die in Flammen gehüllt ist. 1W10+2 Schaden.",
        type = ItemType.WEAPON, icon = "🔥", damage = 12, value = 100
    )

    val lederRuestung = Item(
        id = "leather_armor", name = "Lederrüstung",
        description = "Leichte Rüstung aus gehärtetem Leder. +2 RK.",
        type = ItemType.ARMOR, icon = "🛡️", armorBonus = 2, value = 10
    )
    val kettenHemd = Item(
        id = "chainmail", name = "Kettenhemd",
        description = "Schwere Rüstung aus Stahlringen. +4 RK.",
        type = ItemType.ARMOR, icon = "🛡️", armorBonus = 4, value = 30
    )
    val magierRobe = Item(
        id = "mage_robe", name = "Magierrobe",
        description = "Eine verzauberte Robe. +1 RK, +1 INT.",
        type = ItemType.ARMOR, icon = "👘", armorBonus = 1, statBonus = Stats(int = 1), value = 20
    )

    val heiltrank = Item(
        id = "health_potion", name = "Heiltrank",
        description = "Stellt 2W4+2 Lebenspunkte wieder her.",
        type = ItemType.POTION, icon = "🧪", healAmount = 10, isConsumable = true, value = 5
    )
    val grosserHeiltrank = Item(
        id = "greater_health_potion", name = "Großer Heiltrank",
        description = "Stellt 4W4+4 Lebenspunkte wieder her.",
        type = ItemType.POTION, icon = "🧪", healAmount = 20, isConsumable = true, value = 15
    )

    val ringDerStaerke = Item(
        id = "ring_strength", name = "Ring der Stärke",
        description = "Ein goldener Ring, der mit roter Energie pulsiert. +2 STR.",
        type = ItemType.RING, icon = "💍", statBonus = Stats(str = 2), value = 50
    )
    val amulett = Item(
        id = "amulet_protection", name = "Amulett des Schutzes",
        description = "Ein silbernes Amulett mit einem leuchtenden Edelstein. +1 RK.",
        type = ItemType.RING, icon = "📿", armorBonus = 1, value = 40
    )

    val schriftrolleFeuerball = Item(
        id = "scroll_fireball", name = "Schriftrolle: Feuerball",
        description = "Entfesselt einen Feuerball, der 3W6 Schaden verursacht.",
        type = ItemType.SCROLL, icon = "📜", damage = 18, isConsumable = true, value = 25
    )

    val mysterioeserSchluessel = Item(
        id = "mysterious_key", name = "Mysteriöser Schlüssel",
        description = "Ein schwarzer Schlüssel mit seltsamen Runen. Öffnet etwas...",
        type = ItemType.QUEST_ITEM, icon = "🗝️", value = 0
    )

    fun starterItems(charClass: CharacterClass): List<Item> = when (charClass) {
        CharacterClass.KRIEGER -> listOf(langSchwert, kettenHemd, heiltrank)
        CharacterClass.MAGIER -> listOf(magierstab, magierRobe, heiltrank, heiltrank)
        CharacterClass.SCHURKE -> listOf(dolch, lederRuestung, heiltrank, heiltrank)
        CharacterClass.WALDLAEUFER -> listOf(langbogen, lederRuestung, heiltrank)
    }
}

// --- Enemies ---
data class Enemy(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val maxHp: Int,
    val armorClass: Int,
    val attackBonus: Int,
    val damage: Int,
    val xpReward: Int,
    val goldReward: Int,
    val loot: List<Item> = emptyList(),
    val specialAbility: String? = null
)

object Enemies {
    val goblin = Enemy(
        id = "goblin", name = "Goblin", icon = "👺",
        description = "Ein kleiner, hinterhältiger Goblin mit einem rostigen Dolch.",
        maxHp = 12, armorClass = 12, attackBonus = 3, damage = 4,
        xpReward = 25, goldReward = 5
    )
    val goblinSchamann = Enemy(
        id = "goblin_shaman", name = "Goblin-Schamane", icon = "🧙",
        description = "Ein Goblin mit dunkler Magie. Seine Augen glühen grün.",
        maxHp = 15, armorClass = 11, attackBonus = 4, damage = 6,
        xpReward = 50, goldReward = 10,
        specialAbility = "Kann einmal pro Kampf einen Giftpfeil verschießen (2W4 Schaden)."
    )
    val skelett = Enemy(
        id = "skeleton", name = "Skelettkrieger", icon = "💀",
        description = "Ein klappriges Skelett in verrosteter Rüstung, das ein Schwert schwingt.",
        maxHp = 18, armorClass = 13, attackBonus = 4, damage = 6,
        xpReward = 50, goldReward = 8
    )
    val riesenSpinne = Enemy(
        id = "giant_spider", name = "Riesenspinne", icon = "🕷️",
        description = "Eine menschengroße Spinne mit tropfendem Gift an den Reißzähnen.",
        maxHp = 22, armorClass = 12, attackBonus = 5, damage = 7,
        xpReward = 75, goldReward = 0,
        specialAbility = "Giftbiss: Bei Treffer WEI-Rettungswurf DC 11 oder 2W4 Giftschaden."
    )
    val ork = Enemy(
        id = "orc", name = "Ork-Berserker", icon = "👹",
        description = "Ein massiver Ork mit wutverzerrtem Gesicht und einer Doppelaxt.",
        maxHp = 30, armorClass = 13, attackBonus = 5, damage = 9,
        xpReward = 100, goldReward = 15,
        loot = listOf(Items.heiltrank)
    )
    val dunkleMagierin = Enemy(
        id = "dark_mage", name = "Dunkle Magierin", icon = "🧙‍♀️",
        description = "Eine in Schatten gehüllte Zauberin. Violette Energie knistert um ihre Hände.",
        maxHp = 35, armorClass = 14, attackBonus = 6, damage = 10,
        xpReward = 150, goldReward = 25,
        loot = listOf(Items.schriftrolleFeuerball),
        specialAbility = "Schattenblitz: Einmal pro Kampf 3W6 Schaden."
    )
    val nekromant = Enemy(
        id = "necromancer", name = "Nekromant Vardok", icon = "☠️",
        description = "Der gefürchtete Nekromant Vardok. Seine hohle Stimme hallt durch die Kammer. Untote gehorchen seinem Willen.",
        maxHp = 55, armorClass = 15, attackBonus = 7, damage = 12,
        xpReward = 300, goldReward = 100,
        loot = listOf(Items.feuerSchwert, Items.grosserHeiltrank),
        specialAbility = "Beschwört einmal Skelette (heilt sich um 10 HP). Lebensraub bei jedem Treffer."
    )
    val drache = Enemy(
        id = "dragon", name = "Feuerdrache Smaulgorth", icon = "🐉",
        description = "Ein gewaltiger roter Drache. Sein Atem allein lässt die Luft erzittern. Berge von Gold liegen unter seinen Klauen.",
        maxHp = 80, armorClass = 17, attackBonus = 9, damage = 15,
        xpReward = 500, goldReward = 500,
        loot = listOf(Items.feuerSchwert, Items.ringDerStaerke, Items.grosserHeiltrank),
        specialAbility = "Feueratem: Alle 3 Runden 4W6 Feuerschaden. Klauenangriff trifft zweimal."
    )
}

// --- Skill Checks ---
enum class SkillType(val displayName: String, val statName: String) {
    STRENGTH("Stärke", "STR"),
    DEXTERITY("Geschicklichkeit", "GES"),
    CONSTITUTION("Konstitution", "KON"),
    INTELLIGENCE("Intelligenz", "INT"),
    WISDOM("Weisheit", "WEI"),
    CHARISMA("Charisma", "CHA"),
    PERCEPTION("Wahrnehmung", "WEI"),
    STEALTH("Heimlichkeit", "GES"),
    PERSUASION("Überzeugung", "CHA"),
    ARCANA("Arkane Kunde", "INT")
}

// --- Story Choices ---
data class StoryChoice(
    val text: String,
    val nextNodeId: String,
    val skillCheck: SkillCheck? = null,
    val failNodeId: String? = null,
    val requiredItem: String? = null,
    val giveItem: Item? = null,
    val giveGold: Int = 0,
    val giveXp: Int = 0,
    val healAmount: Int = 0
)

data class SkillCheck(
    val skill: SkillType,
    val dc: Int
)

// --- Story Nodes ---
data class StoryNode(
    val id: String,
    val title: String,
    val text: String,
    val imageId: String = "scene_default",
    val choices: List<StoryChoice> = emptyList(),
    val combat: Enemy? = null,
    val afterCombatNodeId: String? = null,
    val isEnding: Boolean = false,
    val chapter: Int = 1
)

// --- Character ---
data class PlayerCharacter(
    val name: String = "",
    val race: Race = Race.MENSCH,
    val charClass: CharacterClass = CharacterClass.KRIEGER,
    val level: Int = 1,
    val xp: Int = 0,
    val maxHp: Int = 0,
    val currentHp: Int = 0,
    val stats: Stats = Stats(),
    val baseArmorClass: Int = 10,
    val inventory: List<Item> = emptyList(),
    val equippedWeapon: Item? = null,
    val equippedArmor: Item? = null,
    val gold: Int = 0
) {
    val armorClass: Int
        get() = baseArmorClass + stats.dexMod +
                (equippedArmor?.armorBonus ?: 0) +
                inventory.filter { it.type == ItemType.RING }.sumOf { it.armorBonus }

    val attackBonus: Int
        get() = when (charClass) {
            CharacterClass.KRIEGER -> stats.strMod
            CharacterClass.MAGIER -> stats.intMod
            CharacterClass.SCHURKE -> stats.dexMod
            CharacterClass.WALDLAEUFER -> stats.dexMod
        } + (level / 4) + 2

    val damageBonus: Int
        get() = when (charClass) {
            CharacterClass.KRIEGER -> stats.strMod
            CharacterClass.MAGIER -> stats.intMod
            CharacterClass.SCHURKE -> stats.dexMod
            CharacterClass.WALDLAEUFER -> stats.dexMod
        }

    val weaponDamage: Int
        get() = equippedWeapon?.damage ?: 2

    val xpToNextLevel: Int
        get() = level * 100

    fun getSkillModifier(skill: SkillType): Int {
        val proficiencyBonus = (level / 4) + 2
        val baseMod = when (skill) {
            SkillType.STRENGTH -> stats.strMod
            SkillType.DEXTERITY, SkillType.STEALTH -> stats.dexMod
            SkillType.CONSTITUTION -> stats.conMod
            SkillType.INTELLIGENCE, SkillType.ARCANA -> stats.intMod
            SkillType.WISDOM, SkillType.PERCEPTION -> stats.wisMod
            SkillType.CHARISMA, SkillType.PERSUASION -> stats.chaMod
        }
        val isProficient = when (charClass) {
            CharacterClass.KRIEGER -> skill in listOf(SkillType.STRENGTH, SkillType.CONSTITUTION)
            CharacterClass.MAGIER -> skill in listOf(SkillType.INTELLIGENCE, SkillType.ARCANA)
            CharacterClass.SCHURKE -> skill in listOf(SkillType.DEXTERITY, SkillType.STEALTH, SkillType.PERSUASION)
            CharacterClass.WALDLAEUFER -> skill in listOf(SkillType.WISDOM, SkillType.PERCEPTION, SkillType.DEXTERITY)
        }
        return baseMod + if (isProficient) proficiencyBonus else 0
    }
}

// --- Combat State ---
data class CombatState(
    val enemy: Enemy,
    val enemyCurrentHp: Int,
    val isPlayerTurn: Boolean = true,
    val combatLog: List<String> = emptyList(),
    val isOver: Boolean = false,
    val playerWon: Boolean = false,
    val round: Int = 1,
    val playerDefending: Boolean = false,
    val enemySpecialUsed: Boolean = false
)

// --- Game State ---
data class GameState(
    val character: PlayerCharacter = PlayerCharacter(),
    val currentNodeId: String = "start",
    val visitedNodes: Set<String> = emptySet(),
    val combatState: CombatState? = null,
    val gameStarted: Boolean = false,
    val gameLog: List<String> = emptyList(),
    val flags: Map<String, Boolean> = emptyMap()
)
