package com.dungeonquest.app.game

import com.dungeonquest.app.model.*

object StoryData {

    fun getAllNodes(): Map<String, StoryNode> = listOf(
        // ===== KAPITEL 1: DIE TAVERNE =====
        StoryNode(
            id = "start",
            title = "Die Taverne zum Goldenen Drachen",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                Der Regen peitscht gegen deinen Umhang, als du die schwere Eichentür aufstößt.
                Wärme und der Geruch von Braten und Bier schlagen dir entgegen.

                Die Taverne „Zum Goldenen Drachen" ist gut besucht an diesem Abend.
                Ein Barde spielt eine melancholische Melodie in der Ecke, Händler und Bauern
                sitzen an langen Tischen, und hinter dem Tresen poliert ein massiger Zwerg Bierkrüge.

                Doch etwas stimmt nicht. Die Stimmung ist gedrückt, Blicke huschen nervös
                durch den Raum. Du bemerkst einen verhüllten Fremden, der allein in einer
                dunklen Ecke sitzt und dich mustert.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Zum Tresen gehen und den Zwerg ansprechen",
                    nextNodeId = "tavern_bar"
                ),
                StoryChoice(
                    text = "Den verhüllten Fremden in der Ecke ansprechen",
                    nextNodeId = "tavern_stranger"
                ),
                StoryChoice(
                    text = "Sich unauffällig umschauen und lauschen",
                    nextNodeId = "tavern_observe_success",
                    skillCheck = SkillCheck(SkillType.PERCEPTION, 12),
                    failNodeId = "tavern_observe_fail"
                )
            )
        ),

        StoryNode(
            id = "tavern_bar",
            title = "Am Tresen",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                Du setzt dich an den Tresen. Der Zwerg – sein Name ist Grimm, wie ein
                Holzschild verrät – stellt dir wortlos einen Krug Bier hin.

                „Siehst aus wie jemand, der Ärger sucht... oder vor ihm wegläuft," brummt er.
                Dann senkt er die Stimme: „Wenn du Arbeit suchst – ich hätte da was.
                Seit drei Wochen verschwinden Leute aus dem Dorf. Nachts. Einfach weg.
                Letzte Woche war es die Tochter des Müllers."

                Er beugt sich vor. „Man sagt, im alten Verlies unter dem Mondberg
                treibe ein Nekromant sein Unwesen. Der Bürgermeister zahlt 100 Gold
                für jeden, der dem ein Ende setzt."
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "„Ich bin dabei. Erzählt mir mehr über das Verlies."",
                    nextNodeId = "tavern_quest_accepted",
                    giveXp = 10
                ),
                StoryChoice(
                    text = "„100 Gold? Das ist mir zu wenig für einen Nekromanten."",
                    nextNodeId = "tavern_haggle",
                    skillCheck = SkillCheck(SkillType.PERSUASION, 13),
                    failNodeId = "tavern_haggle_fail"
                ),
                StoryChoice(
                    text = "„Klingt gefährlich. Kann ich erstmal mein Bier trinken?"",
                    nextNodeId = "tavern_drink",
                    healAmount = 3
                )
            )
        ),

        StoryNode(
            id = "tavern_stranger",
            title = "Der Verhüllte Fremde",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                Du trittst an den Tisch des Fremden. Zwei Augen glimmen unter der
                Kapuze – violett, wie Amethyste im Kerzenlicht.

                „Setz dich," flüstert eine raue Stimme. Eine knochige Hand deutet
                auf den Stuhl gegenüber.

                „Ich habe auf jemanden wie dich gewartet. Jemand mit... Potenzial."
                Der Fremde schiebt eine vergilbte Karte über den Tisch. Sie zeigt den
                Weg zum Mondberg und ein Labyrinth darunter.

                „In den Tiefen dieses Verlieses ruht etwas Altes. Etwas Mächtiges.
                Der Nekromant, der dort haust, sucht es ebenfalls. Du musst ihn
                aufhalten – oder es zuerst finden."
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "„Wer seid Ihr? Und was ist dieses 'etwas Mächtiges'?"",
                    nextNodeId = "tavern_stranger_reveal"
                ),
                StoryChoice(
                    text = "Die Karte nehmen und den Auftrag annehmen",
                    nextNodeId = "tavern_stranger_quest",
                    giveXp = 15
                ),
                StoryChoice(
                    text = "[Einsicht] Seine wahren Absichten lesen",
                    nextNodeId = "tavern_stranger_insight",
                    skillCheck = SkillCheck(SkillType.WISDOM, 14),
                    failNodeId = "tavern_stranger_insight_fail"
                )
            )
        ),

        StoryNode(
            id = "tavern_observe_success",
            title = "Scharfe Sinne",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                ✓ Wahrnehmung erfolgreich!

                Du lehnst dich unauffällig an eine Säule und lässt deinen Blick
                wandern. Deine geschärften Sinne nehmen einiges wahr:

                Zwei Händler am Nebentisch flüstern über „Verschwundene" und
                „unheimliche Lichter am Mondberg". Der Zwerg am Tresen wirkt
                besorgt und mustert jeden Neuankömmling. Und der verhüllte
                Fremde in der Ecke – seine Finger zeichnen unsichtbare Runen
                auf den Tisch. Ein Magier.

                Du entdeckst auch eine Hintertür, die zum Stall führt, und
                bemerkst, dass ein Bauer am Tresen einen SCHLÜSSEL um den
                Hals trägt – mit denselben Runen wie auf der Verliestür
                laut den alten Geschichten.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Den Magier in der Ecke konfrontieren",
                    nextNodeId = "tavern_stranger",
                    giveXp = 10
                ),
                StoryChoice(
                    text = "Mit Grimm am Tresen sprechen",
                    nextNodeId = "tavern_bar",
                    giveXp = 10
                ),
                StoryChoice(
                    text = "Den Bauern wegen des Schlüssels ansprechen",
                    nextNodeId = "tavern_key_farmer",
                    giveXp = 15
                )
            )
        ),

        StoryNode(
            id = "tavern_observe_fail",
            title = "Nichts Besonderes",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                ✗ Wahrnehmung fehlgeschlagen.

                Du schaust dich um, aber der Lärm der Taverne und der
                Rauch machen es schwer, etwas Ungewöhnliches zu bemerken.
                Es scheint eine ganz normale Taverne zu sein –
                obwohl die Leute etwas nervöser wirken als üblich.

                Vielleicht solltest du direkt mit jemandem reden.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Zum Tresen gehen",
                    nextNodeId = "tavern_bar"
                ),
                StoryChoice(
                    text = "Den verhüllten Fremden ansprechen",
                    nextNodeId = "tavern_stranger"
                )
            )
        ),

        StoryNode(
            id = "tavern_haggle",
            title = "Geschickt Verhandelt",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                ✓ Überzeugung erfolgreich!

                Grimm mustert dich, dann grinst er widerwillig.

                „Ha! Du hast Mumm, das muss ich dir lassen. Also gut –
                150 Gold, und ich geb dir noch Proviant und einen
                Heiltrank für den Weg mit. Der Bürgermeister kann sich
                das leisten, glaub mir."

                Er reicht dir einen schimmernden roten Trank über den Tresen.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Den Handel annehmen und nach dem Verlies fragen",
                    nextNodeId = "tavern_quest_accepted",
                    giveItem = Items.heiltrank,
                    giveGold = 50,
                    giveXp = 20
                )
            )
        ),

        StoryNode(
            id = "tavern_haggle_fail",
            title = "Kein Verhandlungsgeschick",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                ✗ Überzeugung fehlgeschlagen.

                Grimm runzelt die Stirn. „100 Gold für's Monsterjagen –
                das ist ein fairer Preis, Fremder. Nimm es oder lass es."

                Er verschränkt die Arme. An seiner Miene erkennst du,
                dass Verhandeln zwecklos ist.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "„Gut, 100 Gold. Ich bin dabei."",
                    nextNodeId = "tavern_quest_accepted",
                    giveXp = 5
                ),
                StoryChoice(
                    text = "Lieber mit dem Fremden in der Ecke reden",
                    nextNodeId = "tavern_stranger"
                )
            )
        ),

        StoryNode(
            id = "tavern_drink",
            title = "Ein Moment der Ruhe",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                Du nimmst einen tiefen Schluck des kühlen Biers. Es ist
                überraschend gut – würzig und kräftig, wie man es von
                einem Zwerg erwarten würde.

                Grimm nickt anerkennend. „Eigene Brauerei. Das beste
                Bier westlich der Drachenspitze."

                Das Bier wärmt dich von innen und du fühlst dich
                etwas erfrischt. Grimm lehnt sich wieder vor.

                „Also, was sagst du? Hilfst du uns mit dem Problem
                am Mondberg?"
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "„Erzählt mir alles über das Verlies."",
                    nextNodeId = "tavern_quest_accepted",
                    giveXp = 5
                ),
                StoryChoice(
                    text = "Erstmal den Fremden in der Ecke beobachten",
                    nextNodeId = "tavern_stranger"
                )
            )
        ),

        StoryNode(
            id = "tavern_stranger_reveal",
            title = "Enthüllung",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                Der Fremde zieht langsam seine Kapuze zurück.
                Darunter kommt das Gesicht einer alten Elfin zum
                Vorschein – tiefe Falten, aber lebendige, violette Augen.

                „Mein Name ist Elara. Ich bin eine Hüterin des
                Wissens – eine der Letzten. Unter dem Mondberg ruht
                das Seelensiegel, ein Artefakt aus der Zeit der
                Drachen. Es kann die Grenze zwischen Leben und Tod
                verwischen."

                Ihre Stimme wird ernst. „Der Nekromant Vardok will
                es nutzen, um eine Armee Untoter zu erschaffen.
                Wenn er Erfolg hat..." Sie lässt den Satz offen.

                „Ich bin zu alt für diese Reise. Aber du –
                ich spüre Stärke in dir."
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "„Ich werde Vardok aufhalten. Gebt mir die Karte."",
                    nextNodeId = "tavern_stranger_quest",
                    giveXp = 15
                ),
                StoryChoice(
                    text = "„Was erwartet mich dort unten?"",
                    nextNodeId = "tavern_quest_accepted",
                    giveXp = 10
                )
            )
        ),

        StoryNode(
            id = "tavern_stranger_quest",
            title = "Elaras Auftrag",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                Elara schiebt dir die Karte zu und legt einen
                schimmernden Gegenstand daneben – ein silbernes
                Amulett mit einem blauen Edelstein.

                „Nimm dies. Es wird dir Schutz gewähren gegen
                die dunkelste Magie. Und hier—" Sie legt einen
                Heiltrank auf den Tisch. „Du wirst ihn brauchen."

                „Folge dem Pfad durch den Dunkelwald zum Mondberg.
                Sei vorsichtig – Vardoks Kreaturen streifen bereits
                durch den Wald. Und wenn du das Seelensiegel findest...
                zerstöre es. Es darf nicht in seine Hände fallen."

                Sie ergreift deine Hand. „Mögen die alten Götter
                dich beschützen."
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Aufbrechen in den Dunkelwald",
                    nextNodeId = "forest_entrance",
                    giveItem = Items.amulett,
                    giveXp = 20
                )
            )
        ),

        StoryNode(
            id = "tavern_stranger_insight",
            title = "Verborgene Wahrheit",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                ✓ Einsicht erfolgreich!

                Du studierst den Fremden genau. Hinter der mysteriösen
                Fassade erkennst du echte Sorge – und Angst. Diese
                Person lügt nicht. Was immer sie sucht, es ist
                ihr todernst.

                Du bemerkst auch, dass ihre Hände zittern – nicht
                vor Kälte, sondern vor Schwäche. Sie ist krank
                oder verflucht. Die Zeit drängt für sie.

                „Du bist scharfsinnig," murmelt die Gestalt, als
                hätte sie deine Gedanken gelesen. „Ja, mir bleibt
                nicht viel Zeit. Deshalb brauche ich DEINE Hilfe."
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "„Ich helfe Euch. Erzählt mir alles."",
                    nextNodeId = "tavern_stranger_reveal",
                    giveXp = 20
                )
            )
        ),

        StoryNode(
            id = "tavern_stranger_insight_fail",
            title = "Undurchsichtig",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                ✗ Einsicht fehlgeschlagen.

                Du versuchst, die Absichten des Fremden zu lesen,
                aber sein Gesicht bleibt im Schatten verborgen.
                Du kannst nicht sagen, ob er die Wahrheit spricht
                oder dich manipuliert.

                „Nun?" fragt die Stimme ungeduldig. „Die Zeit drängt."
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Trotzdem vertrauen und zuhören",
                    nextNodeId = "tavern_stranger_reveal"
                ),
                StoryChoice(
                    text = "Lieber mit Grimm am Tresen reden",
                    nextNodeId = "tavern_bar"
                )
            )
        ),

        StoryNode(
            id = "tavern_key_farmer",
            title = "Der Schlüssel des Bauern",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                Du setzt dich neben den Bauern. Er ist nervös,
                trinkt hastig und hält den Schlüssel umklammert.

                „Was? Nein, den geb ich nicht her!" stammelt er,
                als du fragst. „Das ist... das ist ein Familien-
                erbstück. Von meinem Großvater."

                Er senkt die Stimme. „Er war der letzte Wächter
                des Mondberg-Verlieses. Bevor... bevor dieser
                Nekromant kam. Dieser Schlüssel öffnet eine
                geheime Kammer dort unten. Eine, die der
                Nekromant nicht kennt."

                Seine Augen werden feucht. „Meine Tochter...
                sie ist eine der Verschwundenen."
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "„Ich werde sie retten. Gebt mir den Schlüssel."",
                    nextNodeId = "tavern_key_obtained",
                    skillCheck = SkillCheck(SkillType.PERSUASION, 11),
                    failNodeId = "tavern_key_refused"
                ),
                StoryChoice(
                    text = "„Ich verspreche, den Nekromanten aufzuhalten."",
                    nextNodeId = "tavern_quest_accepted",
                    giveXp = 10
                )
            )
        ),

        StoryNode(
            id = "tavern_key_obtained",
            title = "Der mysteriöse Schlüssel",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                ✓ Überzeugung erfolgreich!

                Der Bauer starrt dich lange an. Dann, mit
                zitternden Händen, nimmt er den Schlüssel ab
                und legt ihn in deine Hand.

                „Bring meine Tochter zurück. Bitte."
                Eine Träne rinnt über seine Wange.

                Der Schlüssel ist kalt und schwer. Die Runen
                darauf glimmen schwach bläulich.

                Du hast den Mysteriösen Schlüssel erhalten!
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Noch mit Grimm sprechen, dann aufbrechen",
                    nextNodeId = "tavern_quest_accepted",
                    giveItem = Items.mysterioeserSchluessel,
                    giveXp = 25
                )
            )
        ),

        StoryNode(
            id = "tavern_key_refused",
            title = "Verweigert",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                ✗ Überzeugung fehlgeschlagen.

                Der Bauer schüttelt heftig den Kopf und
                versteckt den Schlüssel unter seinem Hemd.

                „Nein! Ich kenne dich nicht. Woher soll ich
                wissen, dass du ihn nicht für dich selbst willst?"

                Er steht auf und geht. Vielleicht findest du
                einen anderen Weg in die geheime Kammer.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Sich mit Grimm über das Verlies unterhalten",
                    nextNodeId = "tavern_quest_accepted"
                )
            )
        ),

        StoryNode(
            id = "tavern_quest_accepted",
            title = "Die Quest beginnt",
            chapter = 1,
            imageId = "scene_tavern",
            text = """
                Du hast nun ein klares Bild der Lage: Der Nekromant
                Vardok hat sich im alten Verlies unter dem Mondberg
                eingenistet. Dorfbewohner verschwinden, und dunkle
                Magie breitet sich aus.

                Grimm gibt dir letzte Hinweise:
                „Der Weg führt durch den Dunkelwald – Vorsicht,
                dort treiben sich Goblins und Schlimmeres herum.
                Am Fuß des Mondbergs findest du den Eingang zum
                Verlies. Geh bei Tagesanbruch."

                Du legst dich in einem Zimmer oberhalb der Taverne
                schlafen. Als die Morgendämmerung durch die Fenster
                bricht, bist du bereit.

                Das Abenteuer beginnt.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "In den Dunkelwald aufbrechen →",
                    nextNodeId = "forest_entrance",
                    giveXp = 10,
                    healAmount = 999 // Full heal at rest
                )
            )
        ),

        // ===== KAPITEL 2: DER DUNKELWALD =====
        StoryNode(
            id = "forest_entrance",
            title = "Der Dunkelwald",
            chapter = 2,
            imageId = "scene_forest",
            text = """
                Der Pfad führt dich aus dem Dorf in den Dunkelwald.
                Die Bäume werden dichter, das Licht schwächer. Nebel
                kriecht über den Boden, und seltsame Geräusche
                dringen aus dem Unterholz.

                Du bist schon eine Stunde unterwegs, als sich der
                Weg gabelt. Ein verwitterter Wegweiser zeigt:

                ← MONDBERG (Hauptweg)
                → ALTE RUINEN (Umweg)

                Der Hauptweg ist breiter, aber du siehst frische
                Fußspuren – viele, kleine Füße. Goblins.

                Der Weg zu den Ruinen ist zugewachsen, aber
                vielleicht findest du dort etwas Nützliches.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Den Hauptweg zum Mondberg nehmen",
                    nextNodeId = "forest_main_path"
                ),
                StoryChoice(
                    text = "Den Umweg über die alten Ruinen nehmen",
                    nextNodeId = "forest_ruins"
                ),
                StoryChoice(
                    text = "[Wahrnehmung] Die Goblin-Spuren untersuchen",
                    nextNodeId = "forest_tracks_success",
                    skillCheck = SkillCheck(SkillType.PERCEPTION, 13),
                    failNodeId = "forest_main_path"
                )
            )
        ),

        StoryNode(
            id = "forest_tracks_success",
            title = "Fährtenleser",
            chapter = 2,
            imageId = "scene_forest",
            text = """
                ✓ Wahrnehmung erfolgreich!

                Du untersuchst die Spuren genau. Es sind mindestens
                vier Goblins, die hier vor Kurzem entlanggelaufen
                sind. Aber du bemerkst noch etwas – sie haben eine
                FALLE aufgestellt! Dünne Drähte zwischen den Bäumen,
                die einen Netzmechanismus auslösen.

                Du kannst die Falle umgehen oder sie sogar gegen
                die Goblins selbst verwenden.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Die Falle umgehen und leise weiterschleichen",
                    nextNodeId = "forest_ambush_avoided",
                    giveXp = 20
                ),
                StoryChoice(
                    text = "Die Falle auslösen, um die Goblins anzulocken",
                    nextNodeId = "forest_goblin_fight",
                    giveXp = 10
                )
            )
        ),

        StoryNode(
            id = "forest_main_path",
            title = "Goblin-Hinterhalt!",
            chapter = 2,
            imageId = "scene_forest",
            combat = Enemies.goblin,
            afterCombatNodeId = "forest_after_goblin",
            text = """
                Du folgst dem Hauptweg, als plötzlich ein schrilles
                Kreischen ertönt! Aus dem Gebüsch springen Goblins –
                kleine, grünhäutige Kreaturen mit gelben Augen
                und rostigen Waffen.

                „Fleisch! Fleisch! Gold!" kreischt ihr Anführer.

                Ein Kampf ist unvermeidlich!
            """.trimIndent()
        ),

        StoryNode(
            id = "forest_goblin_fight",
            title = "Goblin-Kampf",
            chapter = 2,
            imageId = "scene_forest",
            combat = Enemies.goblin,
            afterCombatNodeId = "forest_after_goblin_trap",
            text = """
                Du löst die Falle aus – das Netz schnellt hoch
                und verfängt... nichts. Aber der Lärm lockt einen
                Goblin-Späher an!

                „Eindringling! EINDRINGLING!" kreischt er und
                zieht seinen Dolch.

                Zum Kampf!
            """.trimIndent()
        ),

        StoryNode(
            id = "forest_ambush_avoided",
            title = "Unbemerkt",
            chapter = 2,
            imageId = "scene_forest",
            text = """
                Geschickt umgehst du die Fallen und schleichst
                an den Goblins vorbei, die auf einer Lichtung
                hocken und um ein Feuer streiten.

                Du bemerkst, dass sie einen Gefangenen haben –
                einen verwundeten Waldläufer, an einen Baum
                gefesselt.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Den Gefangenen befreien (Kampf mit Goblins)",
                    nextNodeId = "forest_rescue_fight"
                ),
                StoryChoice(
                    text = "[Heimlichkeit] Leise befreien ohne Kampf",
                    nextNodeId = "forest_rescue_stealth",
                    skillCheck = SkillCheck(SkillType.STEALTH, 14),
                    failNodeId = "forest_rescue_fight"
                ),
                StoryChoice(
                    text = "Weitergehen und den Gefangenen zurücklassen",
                    nextNodeId = "forest_crossroads"
                )
            )
        ),

        StoryNode(
            id = "forest_rescue_fight",
            title = "Befreiungskampf",
            chapter = 2,
            imageId = "scene_forest",
            combat = Enemies.goblinSchamann,
            afterCombatNodeId = "forest_rescue_success",
            text = """
                Die Goblins bemerken dich! Ihr Schamane, ein
                größerer Goblin mit einem Knochenstab, zeigt auf
                dich und kreischt einen Befehl.

                Der Goblin-Schamane greift an!
            """.trimIndent()
        ),

        StoryNode(
            id = "forest_rescue_stealth",
            title = "Leise Befreiung",
            chapter = 2,
            imageId = "scene_forest",
            text = """
                ✓ Heimlichkeit erfolgreich!

                Wie ein Schatten gleitest du durch das Unterholz.
                Während die Goblins streiten, durchschneidest du
                die Fesseln des Gefangenen.

                „Danke," flüstert er. „Ich bin Aldric, ein
                Waldläufer. Diese Bestien haben mich überfallen."

                Er reicht dir einen Heiltrank. „Nimm das – du
                wirst ihn brauchen. Der Wald wird gefährlicher,
                je näher man dem Mondberg kommt."
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Aldric nach dem Weg fragen und weiterziehen",
                    nextNodeId = "forest_crossroads",
                    giveItem = Items.heiltrank,
                    giveXp = 40
                )
            )
        ),

        StoryNode(
            id = "forest_rescue_success",
            title = "Befreit!",
            chapter = 2,
            imageId = "scene_forest",
            text = """
                Die Goblins sind besiegt! Du befreist den
                Gefangenen.

                „Ich danke dir, Fremder! Mein Name ist Aldric.
                Ohne dich wäre ich Goblin-Futter geworden."

                Er humpelt, ist aber dankbar. „Für deine Hilfe
                – nimm diesen Trank. Und ein Rat: Am Fuß des
                Mondbergs gibt es eine Höhle. Der Eingang zum
                Verlies. Aber Vorsicht – der Weg wird von einer
                Riesenspinne bewacht."
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Weiter zum Mondberg",
                    nextNodeId = "forest_crossroads",
                    giveItem = Items.heiltrank,
                    giveXp = 30
                )
            )
        ),

        StoryNode(
            id = "forest_after_goblin",
            title = "Nach dem Kampf",
            chapter = 2,
            imageId = "scene_forest",
            text = """
                Der Goblin liegt besiegt am Boden. Du durchsuchst
                seine Taschen und findest ein paar Münzen.

                Der Wald wird dichter. Vor dir siehst du einen
                Wegweiser, halb verrottet: „MONDBERG →"
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Weiter dem Pfad folgen",
                    nextNodeId = "forest_crossroads",
                    giveGold = 8
                )
            )
        ),

        StoryNode(
            id = "forest_after_goblin_trap",
            title = "Goblin besiegt",
            chapter = 2,
            imageId = "scene_forest",
            text = """
                Der Goblin-Späher ist erledigt. Du hast die
                anderen mit dem Netztrick verwirrt und konntest
                unbemerkt weitergehen.

                Der Pfad zum Mondberg liegt vor dir.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Weiter zum Mondberg",
                    nextNodeId = "forest_crossroads",
                    giveGold = 5,
                    giveXp = 10
                )
            )
        ),

        StoryNode(
            id = "forest_ruins",
            title = "Die Alten Ruinen",
            chapter = 2,
            imageId = "scene_dungeon",
            text = """
                Der Pfad zu den Ruinen ist überwuchert, aber
                passierbar. Nach einer halben Stunde erreichst
                du die Überreste eines alten Tempels.

                Moosbewachsene Säulen ragen in den Himmel.
                In der Mitte steht ein zerbrochener Altar mit
                einer Inschrift in einer alten Sprache.

                Hinter dem Altar siehst du eine steinerne Truhe,
                verschlossen und mit Ranken überwachsen.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "[Arkane Kunde] Die Inschrift entziffern",
                    nextNodeId = "forest_ruins_inscription",
                    skillCheck = SkillCheck(SkillType.ARCANA, 13),
                    failNodeId = "forest_ruins_fail"
                ),
                StoryChoice(
                    text = "[Stärke] Die Truhe mit Gewalt öffnen",
                    nextNodeId = "forest_ruins_chest",
                    skillCheck = SkillCheck(SkillType.STRENGTH, 14),
                    failNodeId = "forest_ruins_fail"
                ),
                StoryChoice(
                    text = "Die Ruinen verlassen und zum Hauptweg zurückkehren",
                    nextNodeId = "forest_crossroads"
                )
            )
        ),

        StoryNode(
            id = "forest_ruins_inscription",
            title = "Uralte Worte",
            chapter = 2,
            imageId = "scene_dungeon",
            text = """
                ✓ Arkane Kunde erfolgreich!

                Die Inschrift ist in Alt-Elfisch geschrieben:

                „Hier ruht der Segen des Waldhüters.
                Wer rein im Herzen wandelt,
                dem öffne sich die Gabe."

                Als du die Worte laut vorliest, beginnt die
                Truhe zu leuchten und öffnet sich von selbst!

                Darin liegt ein Ring, der golden schimmert,
                und ein großer Heiltrank.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Die Schätze nehmen und weiterziehen",
                    nextNodeId = "forest_crossroads",
                    giveItem = Items.ringDerStaerke,
                    giveXp = 30
                )
            )
        ),

        StoryNode(
            id = "forest_ruins_chest",
            title = "Rohe Gewalt",
            chapter = 2,
            imageId = "scene_dungeon",
            text = """
                ✓ Stärke erfolgreich!

                Mit einem gewaltigen Ruck reißt du den Deckel
                der Truhe auf! Die Ranken zerreißen und
                steinerne Splitter fliegen.

                In der Truhe findest du einen großen Heiltrank,
                einige Goldmünzen und ein altes Schwert –
                noch erstaunlich scharf!
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Die Beute einpacken und weiter",
                    nextNodeId = "forest_crossroads",
                    giveItem = Items.grosserHeiltrank,
                    giveGold = 20,
                    giveXp = 20
                )
            )
        ),

        StoryNode(
            id = "forest_ruins_fail",
            title = "Versperrt",
            chapter = 2,
            imageId = "scene_dungeon",
            text = """
                ✗ Fehlgeschlagen.

                Du versuchst dein Bestes, aber die Truhe
                bleibt verschlossen. Welche Magie auch immer
                sie versiegelt – sie ist stärker als du.

                Ein unheimliches Rascheln in den Ruinen
                erinnert dich daran, dass du weiterziehen
                solltest.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Zurück zum Hauptweg",
                    nextNodeId = "forest_crossroads",
                    giveXp = 5
                )
            )
        ),

        StoryNode(
            id = "forest_crossroads",
            title = "Am Fuß des Mondbergs",
            chapter = 2,
            imageId = "scene_forest",
            text = """
                Der Wald lichtet sich und vor dir erhebt sich
                der Mondberg – ein düsterer Felsen, dessen
                Gipfel in Wolken gehüllt ist.

                Am Fuß des Berges klafft ein dunkler Eingang,
                halb verborgen hinter Gestrüpp. Ein kalter
                Luftzug weht heraus und du riechst Verwesung.

                Spinnweben bedecken den Eingang. Große
                Spinnweben. SEHR große Spinnweben.

                Du hörst ein leises Klicken aus der Dunkelheit...
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Mutig in den Eingang treten",
                    nextNodeId = "dungeon_spider"
                ),
                StoryChoice(
                    text = "[Wahrnehmung] Erst die Umgebung untersuchen",
                    nextNodeId = "forest_investigate_entrance",
                    skillCheck = SkillCheck(SkillType.PERCEPTION, 12),
                    failNodeId = "dungeon_spider"
                ),
                StoryChoice(
                    text = "Sich vorbereiten (Inventar öffnen, Heiltrank trinken)",
                    nextNodeId = "forest_crossroads",
                    healAmount = 0
                )
            )
        ),

        StoryNode(
            id = "forest_investigate_entrance",
            title = "Vorsicht ist besser",
            chapter = 2,
            imageId = "scene_forest",
            text = """
                ✓ Wahrnehmung erfolgreich!

                Du entdeckst, dass die Spinnweben ein Muster
                bilden – ein Warnsystem! Die Spinne würde
                sofort bemerken, wenn du sie berührst.

                Du findest einen Seiteneingang, teilweise
                eingestürzt, aber groß genug zum Durchkriechen.
                So kannst du der Spinne ausweichen – oder sie
                von hinten überraschen.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Durch den Seiteneingang schleichen (Spinne umgehen)",
                    nextNodeId = "dungeon_entrance",
                    giveXp = 25
                ),
                StoryChoice(
                    text = "Die Spinne von hinten angreifen (Überraschungsangriff)",
                    nextNodeId = "dungeon_spider_surprise",
                    giveXp = 10
                )
            )
        ),

        // ===== KAPITEL 3: DAS VERLIES =====
        StoryNode(
            id = "dungeon_spider",
            title = "Die Wächterin des Eingangs",
            chapter = 3,
            imageId = "scene_dungeon",
            combat = Enemies.riesenSpinne,
            afterCombatNodeId = "dungeon_entrance",
            text = """
                Du trittst in die Höhle – und sofort zuckt
                etwas über dir! Eine RIESIGE Spinne, groß
                wie ein Pferd, lässt sich von der Decke fallen!

                Ihre Giftzähne tropfen, acht Augen funkeln
                dich an. KAMPF!
            """.trimIndent()
        ),

        StoryNode(
            id = "dungeon_spider_surprise",
            title = "Überraschungsangriff!",
            chapter = 3,
            imageId = "scene_dungeon",
            combat = Enemies.riesenSpinne,
            afterCombatNodeId = "dungeon_entrance",
            text = """
                Du schleichst dich durch den Seiteneingang
                und siehst die Riesenspinne von hinten!
                Sie lauert an der Decke über dem Haupteingang.

                Du nutzt den Überraschungsmoment und greifst
                an! Du triffst sie, bevor sie reagieren kann!

                (Die Spinne startet mit weniger HP!)
            """.trimIndent()
        ),

        StoryNode(
            id = "dungeon_entrance",
            title = "Das Verlies des Mondbergs",
            chapter = 3,
            imageId = "scene_dungeon",
            text = """
                Du stehst im Eingang des alten Verlieses.
                Fackeln an den Wänden brennen mit unnatürlich
                blauem Licht. Die Luft ist kalt und riecht
                nach Erde und altem Tod.

                Vor dir erstreckt sich ein langer Gang. An
                den Wänden sind Reliefs eingemeißelt – sie
                zeigen Szenen einer alten Schlacht zwischen
                Drachen und Magiern.

                Der Gang teilt sich: Links hörst du das
                Klappern von Knochen. Rechts siehst du
                schwaches Licht und riechst etwas Süßliches.

                Geradeaus führt eine massive Tür, verschlossen
                mit seltsamen Runen.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Links – dem Klappern folgen",
                    nextNodeId = "dungeon_skeleton_room"
                ),
                StoryChoice(
                    text = "Rechts – dem Licht folgen",
                    nextNodeId = "dungeon_alchemy_room"
                ),
                StoryChoice(
                    text = "Die Runentür untersuchen",
                    nextNodeId = "dungeon_rune_door"
                )
            )
        ),

        StoryNode(
            id = "dungeon_skeleton_room",
            title = "Halle der Gebeine",
            chapter = 3,
            imageId = "scene_dungeon",
            combat = Enemies.skelett,
            afterCombatNodeId = "dungeon_skeleton_loot",
            text = """
                Du betrittst eine große Halle. Der Boden ist
                übersät mit Knochen und zerbrochener Rüstung –
                die Überreste vergangener Abenteurer.

                Plötzlich bewegen sich die Knochen! Ein
                Skelett erhebt sich, greift ein rostiges
                Schwert und starrt dich mit leeren Augenhöhlen an.

                Dunkle Magie hält es am Un-Leben. KAMPF!
            """.trimIndent()
        ),

        StoryNode(
            id = "dungeon_skeleton_loot",
            title = "Beute der Gefallenen",
            chapter = 3,
            imageId = "scene_dungeon",
            text = """
                Das Skelett zerfällt zu Staub. Die dunkle
                Energie, die es belebte, löst sich auf.

                Unter den Knochen findest du die Ausrüstung
                eines früheren Abenteurers: Ein Beutel Gold
                und ein brauchbarer Heiltrank.

                An der Wand bemerkst du eine Inschrift:
                „Vardoks Macht wächst mit jedem Opfer.
                Beeilt euch."
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Zurück zur Kreuzung und rechts gehen",
                    nextNodeId = "dungeon_alchemy_room",
                    giveGold = 15,
                    giveItem = Items.heiltrank,
                    giveXp = 10
                ),
                StoryChoice(
                    text = "Zurück und die Runentür untersuchen",
                    nextNodeId = "dungeon_rune_door",
                    giveGold = 15,
                    giveItem = Items.heiltrank,
                    giveXp = 10
                )
            )
        ),

        StoryNode(
            id = "dungeon_alchemy_room",
            title = "Das Alchemie-Labor",
            chapter = 3,
            imageId = "scene_dungeon",
            text = """
                Du betrittst ein altes Alchemie-Labor. Regale
                voller Phiolen und Bücher stehen an den Wänden.
                Ein Kessel brodelt über einem magischen Feuer.

                Auf einem Tisch liegen Notizen – Vardoks
                Forschungen über das Seelensiegel. Er scheint
                nah daran zu sein, es zu aktivieren.

                Du siehst mehrere Tränke auf dem Regal:
                Ein roter (Heilung), ein blauer (Magie) und
                ein schwarzer (unbekannt).
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Den roten Heiltrank nehmen",
                    nextNodeId = "dungeon_alchemy_done",
                    giveItem = Items.grosserHeiltrank,
                    giveXp = 10
                ),
                StoryChoice(
                    text = "[Arkane Kunde] Die Notizen studieren",
                    nextNodeId = "dungeon_alchemy_notes",
                    skillCheck = SkillCheck(SkillType.ARCANA, 14),
                    failNodeId = "dungeon_alchemy_done"
                ),
                StoryChoice(
                    text = "Den schwarzen Trank trinken (mutig!)",
                    nextNodeId = "dungeon_alchemy_black",
                    giveXp = 5
                )
            )
        ),

        StoryNode(
            id = "dungeon_alchemy_notes",
            title = "Vardoks Geheimnisse",
            chapter = 3,
            imageId = "scene_dungeon",
            text = """
                ✓ Arkane Kunde erfolgreich!

                Du entzifferst die Notizen. Vardok hat eine
                Schwäche – das Seelensiegel muss mit einem
                Ritual aktiviert werden, das LICHT erfordert.
                Vardoks Nekromantie basiert auf Dunkelheit.

                Wenn du Lichtmagie oder heilige Symbole
                gegen ihn einsetzt, wird er geschwächt!

                Du findest auch die Formel für einen
                Schriftrolle des Feuerballs unter den Notizen.

                (Vardok wird im finalen Kampf geschwächt sein!)
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Die Feuerball-Schriftrolle und den Heiltrank nehmen",
                    nextNodeId = "dungeon_alchemy_done",
                    giveItem = Items.schriftrolleFeuerball,
                    giveXp = 35
                )
            )
        ),

        StoryNode(
            id = "dungeon_alchemy_black",
            title = "Der Schwarze Trank",
            chapter = 3,
            imageId = "scene_dungeon",
            text = """
                Du trinkst den schwarzen Trank. Er schmeckt
                nach Lakritz und... Asche?

                Dein Körper durchzuckt Schmerz – dann ein
                Gefühl von Macht! Dunkle Energie durchströmt
                dich. Deine Sinne schärfen sich.

                Du verlierst etwas Lebensenergie, aber
                gewinnst dunkle Stärke.

                (-5 HP, aber +2 auf den nächsten Angriff)
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Weiter erkunden",
                    nextNodeId = "dungeon_alchemy_done",
                    giveXp = 15
                )
            )
        ),

        StoryNode(
            id = "dungeon_alchemy_done",
            title = "Weiter ins Verlies",
            chapter = 3,
            imageId = "scene_dungeon",
            text = """
                Du verlässt das Labor. Die Luft wird kälter,
                je tiefer du in das Verlies vordringst.

                Vor dir liegt die Runentür – der Weg zu
                Vardoks Kammer.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Zur Runentür gehen",
                    nextNodeId = "dungeon_rune_door"
                )
            )
        ),

        StoryNode(
            id = "dungeon_rune_door",
            title = "Die Runentür",
            chapter = 3,
            imageId = "scene_dungeon",
            text = """
                Die massive Steintür ist mit glühenden Runen
                bedeckt. Sie pulsieren in einem langsamen
                Rhythmus – wie ein Herzschlag.

                Die Tür ist magisch versiegelt. Du brauchst
                entweder den richtigen Schlüssel, magisches
                Wissen oder rohe Gewalt.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Den Mysteriösen Schlüssel verwenden",
                    nextNodeId = "dungeon_secret_room",
                    requiredItem = "mysterious_key"
                ),
                StoryChoice(
                    text = "[Arkane Kunde] Das Runenpuzzle lösen",
                    nextNodeId = "dungeon_boss_approach",
                    skillCheck = SkillCheck(SkillType.ARCANA, 15),
                    failNodeId = "dungeon_rune_fail"
                ),
                StoryChoice(
                    text = "[Stärke] Die Tür eintreten",
                    nextNodeId = "dungeon_boss_approach",
                    skillCheck = SkillCheck(SkillType.STRENGTH, 16),
                    failNodeId = "dungeon_rune_fail"
                )
            )
        ),

        StoryNode(
            id = "dungeon_rune_fail",
            title = "Magische Abwehr",
            chapter = 3,
            imageId = "scene_dungeon",
            text = """
                ✗ Fehlgeschlagen!

                Die Runen blitzen auf und eine Schockwelle
                wirft dich zurück! Du nimmst Schaden.

                Aber der Schlag hat auch die Tür geschwächt.
                Mit einem letzten Versuch könntest du sie
                aufbrechen.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Nochmal versuchen – mit aller Kraft!",
                    nextNodeId = "dungeon_boss_approach",
                    giveXp = 5
                )
            )
        ),

        StoryNode(
            id = "dungeon_secret_room",
            title = "Die Geheimkammer",
            chapter = 3,
            imageId = "scene_dungeon",
            text = """
                Der Mysteriöse Schlüssel passt perfekt! Die
                Runen leuchten blau auf und eine verborgene
                Tür in der Wand gleitet auf.

                Dahinter liegt eine geheime Kammer! Auf einem
                Podest in der Mitte liegt ein flammendes
                Schwert – das legendäre Flammenschwert der
                alten Wächter!

                An der Wand sind Gefangene angekettet – die
                vermissten Dorfbewohner! Sie leben noch!

                „Bitte, befreit uns!" fleht eine junge Frau.
                Die Tochter des Müllers.
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Die Gefangenen befreien und das Schwert nehmen",
                    nextNodeId = "dungeon_boss_approach",
                    giveItem = Items.feuerSchwert,
                    giveXp = 50
                )
            )
        ),

        // ===== KAPITEL 4: DER ENDKAMPF =====
        StoryNode(
            id = "dungeon_boss_approach",
            title = "Vardoks Kammer",
            chapter = 4,
            imageId = "scene_dungeon",
            text = """
                Du stehst vor der letzten Tür. Dahinter hörst
                du eine tiefe, hallende Stimme, die eine
                Beschwörung murmelt. Grünes Licht flackert
                durch die Ritzen.

                Dies ist es. Der Nekromant Vardok wartet
                dahinter. Das Seelensiegel, das Schicksal
                des Dorfes – alles hängt von dir ab.

                Du atmest tief durch. Bist du bereit?
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Die Tür aufstoßen und Vardok stellen!",
                    nextNodeId = "dungeon_boss_fight"
                ),
                StoryChoice(
                    text = "Sich vorbereiten (Inventar prüfen, heilen)",
                    nextNodeId = "dungeon_boss_approach",
                    healAmount = 0
                )
            )
        ),

        StoryNode(
            id = "dungeon_boss_fight",
            title = "Der Nekromant Vardok",
            chapter = 4,
            imageId = "scene_boss",
            combat = Enemies.nekromant,
            afterCombatNodeId = "dungeon_boss_defeated",
            text = """
                Du stürzt in die Kammer!

                Ein riesiger Raum, erleuchtet von grünem
                Feuer. In der Mitte schwebt ein pulsierendes,
                schwarzes Artefakt – das Seelensiegel!

                Davor steht VARDOK – ein hochgewachsener
                Mann in schwarzer Robe, das Gesicht halb
                Skelett, halb Mensch. Seine Augen brennen
                mit unheiligem Feuer.

                „Ah, der kleine Held. Zu spät! Das Ritual
                ist fast vollendet. Aber du wirst ein
                ausgezeichnetes letztes Opfer sein!"

                Er hebt seine Hände und dunkle Energie
                sammelt sich um sie!

                BOSSKAMPF: NEKROMANT VARDOK!
            """.trimIndent()
        ),

        StoryNode(
            id = "dungeon_boss_defeated",
            title = "Sieg über die Dunkelheit",
            chapter = 4,
            imageId = "scene_boss",
            text = """
                Vardok fällt auf die Knie. Seine dunkle
                Energie löst sich auf, die Schatten weichen.

                „Unmöglich..." keucht er. „Das Siegel...
                es wäre MEIN gewesen..."

                Sein Körper zerfällt zu Asche. Nur seine
                leere Robe bleibt zurück.

                Das Seelensiegel schwebt noch immer in
                der Mitte des Raumes. Seine dunkle Energie
                pulsiert schwächer jetzt, aber es ist
                noch aktiv.

                Was tust du?
            """.trimIndent(),
            choices = listOf(
                StoryChoice(
                    text = "Das Seelensiegel zerstören!",
                    nextNodeId = "ending_destroy",
                    giveXp = 100
                ),
                StoryChoice(
                    text = "Das Seelensiegel an dich nehmen...",
                    nextNodeId = "ending_take",
                    giveXp = 100
                ),
                StoryChoice(
                    text = "Das Seelensiegel versiegeln und verbergen",
                    nextNodeId = "ending_seal",
                    giveXp = 100
                )
            )
        ),

        // ===== ENDEN =====
        StoryNode(
            id = "ending_destroy",
            title = "Das Gute Ende",
            chapter = 4,
            imageId = "scene_boss",
            isEnding = true,
            text = """
                🏆 DAS SEELENSIEGEL ZERSTÖRT!

                Du schlägst mit aller Kraft auf das
                Seelensiegel ein. Es splittert, schreit
                auf – und explodiert in einem Blitz aus
                weißem Licht!

                Die dunkle Energie im Verlies löst sich
                auf. Die Untoten zerfallen. Die Gefangenen
                sind frei.

                Als du aus dem Verlies trittst, scheint
                die Sonne. Das Dorf empfängt dich als Held.

                Grimm drückt dir einen prallen Beutel Gold
                in die Hand. Elara lächelt aus der Ferne
                und nickt dir zu, bevor sie im Wald verschwindet.

                Die Tochter des Müllers umarmt ihren Vater.
                Der Barde beginnt ein Lied über deine Taten.

                Du bist ein wahrer Held.

                ═══════════════════════════════
                    ABENTEUER ABGESCHLOSSEN!
                    Du hast die Dunkelheit besiegt!
                ═══════════════════════════════
            """.trimIndent()
        ),

        StoryNode(
            id = "ending_take",
            title = "Das Dunkle Ende",
            chapter = 4,
            imageId = "scene_boss",
            isEnding = true,
            text = """
                ☠️ DAS SEELENSIEGEL GENOMMEN!

                Du streckst deine Hand aus und berührst
                das Seelensiegel. Dunkle Energie durchströmt
                dich – Macht, wie du sie nie gekannt hast!

                Du siehst die Welt mit neuen Augen. Leben
                und Tod sind nur noch Werkzeuge für dich.

                Als du aus dem Verlies trittst, weichen
                die Dorfbewohner vor dir zurück. Sie sehen
                es in deinen Augen – die gleiche Dunkelheit,
                die einst Vardok verschlang.

                Elara senkt traurig den Blick. „Ich hatte
                gehofft... aber die Versuchung war zu groß."

                Du verlässt das Dorf. Die Macht ruft dich.
                Wohin sie dich führt? Das ist eine andere
                Geschichte...

                ═══════════════════════════════
                    ABENTEUER ABGESCHLOSSEN!
                    Die Dunkelheit hat einen
                    neuen Meister...
                ═══════════════════════════════
            """.trimIndent()
        ),

        StoryNode(
            id = "ending_seal",
            title = "Das Weise Ende",
            chapter = 4,
            imageId = "scene_boss",
            isEnding = true,
            text = """
                ⚖️ DAS SEELENSIEGEL VERSIEGELT!

                Du erinnerst dich an die Inschrift im
                Tempel und sprichst alte Worte der
                Versiegelung. Das Siegel verstummt und
                wird zu kaltem, grauem Stein.

                Du vergräbst es tief in der Kammer und
                lässt den Eingang einstürzen. Niemand
                wird es je wieder finden.

                Im Dorf wirst du als Held gefeiert.
                Elara nickt anerkennend. „Weise Entscheidung.
                Manche Macht sollte weder genutzt noch
                zerstört werden – nur vergessen."

                Du erhältst deine Belohnung und der Barde
                singt noch Jahre später von deinen Taten.

                Aber manchmal, in stillen Nächten, glaubst
                du, ein leises Pulsieren aus der Richtung
                des Mondbergs zu hören...

                ═══════════════════════════════
                    ABENTEUER ABGESCHLOSSEN!
                    Das Siegel schläft. Vorerst.
                ═══════════════════════════════
            """.trimIndent()
        )
    ).associateBy { it.id }
}
