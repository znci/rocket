package dev.znci.rocket.scripting.api

/**
 * Base class for Rocket API values, extending `RocketLuaValue`.
 * This class provides a common structure for all Rocket values, including a `valueName`
 * that identifies the value within the Rocket API.
 * It serves as a superclass for other classes that represent values in the Rocket API.
 *
 * @param valueName The name of the value, used for specifying the name of the value if it becomes a global.
 */
open class RocketValueBase(
    open var valueName: String
): RocketLuaValue()