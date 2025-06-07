package dev.znci.rocket.scripting.globals.interfaces

import dev.znci.twine.TwineEnum
import net.kyori.adventure.util.TriState as AdventureTriState

//@Global
@Suppress("unused")
class TriState(
    private val triState: AdventureTriState
) : TwineEnum(AdventureTriState::class)