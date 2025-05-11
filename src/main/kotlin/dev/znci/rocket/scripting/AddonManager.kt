package dev.znci.rocket.scripting

import dev.znci.rocket.scripting.api.RocketAddon

object AddonManager {
    private val addons: MutableList<RocketAddon> = mutableListOf()

    fun registerAddon(addon: RocketAddon) {
        addons.add(addon)
    }

    fun getAddons(): List<RocketAddon> = addons
}