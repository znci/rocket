package dev.znci.rocket.scripting.globals.tables.entities

import dev.znci.rocket.scripting.annotations.Entity
import dev.znci.rocket.scripting.globals.interfaces.entity.BaseDisplayEntity
import dev.znci.rocket.util.MessageFormatter
import dev.znci.twine.annotations.TwineNativeFunction
import org.bukkit.entity.EntityType
import org.bukkit.entity.TextDisplay

@Entity(EntityType.TEXT_DISPLAY)
class LuaTextDisplayEntity(entity: TextDisplay):
    BaseDisplayEntity<TextDisplay>(entity) {
        @TwineNativeFunction
        fun setText(text: Any) {
            val messageComponent = MessageFormatter.formatMessage(text.toString())
            entity.text(messageComponent)
        }
    }