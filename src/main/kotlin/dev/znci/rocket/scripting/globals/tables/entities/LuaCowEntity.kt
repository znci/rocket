package dev.znci.rocket.scripting.globals.tables.entities

import dev.znci.rocket.scripting.annotations.Entity
import dev.znci.rocket.scripting.globals.interfaces.entity.BaseAnimalEntity
import org.bukkit.entity.Cow
import org.bukkit.entity.EntityType

@Entity(EntityType.COW)
class LuaCowEntity(entity: Cow):
    BaseAnimalEntity<Cow>(entity)