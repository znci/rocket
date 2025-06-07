package dev.znci.rocket.scripting.globals.interfaces.entities

import dev.znci.rocket.scripting.api.RocketError
import dev.znci.rocket.scripting.globals.tables.LuaLocation
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import dev.znci.twine.annotations.TwineOverload
import org.bukkit.entity.LivingEntity

@Suppress("unused")
interface LivingEntity<T> : Frictional<T>, Attributable<T>, Damageable<T>, Entity<T> where T : LivingEntity {
    override val entity: T

    @TwineNativeProperty
    val eyeHeight: Double
        get() = entity.eyeHeight

    @TwineNativeProperty
    val eyeLocation: LuaLocation
        get() = LuaLocation.fromBukkit(entity.eyeLocation)

    // TODO: Implement Block

    // TODO: Implement BlockFace

    // TODO: Implement TargetBlockInfo

    @TwineNativeFunction
    fun targetEntity(maxDistance: Int): Entity<T>? {
        val targetBukkitEntity = entity.getTargetEntity(maxDistance) ?: return null

        // TODO: fix unchecked cast??
        return targetBukkitEntity as Entity<T>?
    }

    // TODO: Implement TargetEntityInfo

    // TODO: Implement RayTraceResult

    @TwineNativeProperty
    val remainingAir: Int
        get() = entity.remainingAir

    @TwineNativeFunction
    fun setRemainingAir(remainingAir: Int) {
        entity.remainingAir = remainingAir
    }

    @TwineNativeProperty
    val maximumAir: Int
        get() = entity.maximumAir

    @TwineNativeFunction
    fun setMaximumAir(maximumAir: Int) {
        entity.maximumAir = maximumAir
    }

    // TODO: Implement ItemStack

    @TwineNativeProperty
    val arrowCooldown: Int
        get() = entity.arrowCooldown

    @TwineNativeFunction
    fun setArrowCooldown(ticks: Int) {
        entity.arrowCooldown = ticks
    }

    @TwineNativeProperty
    val arrowsInBody: Int
        get() = entity.arrowsInBody

    @TwineNativeFunction
    fun setArrowsInBody(arrows: Int) {
        entity.arrowsInBody = arrows
    }

    @TwineNativeFunction
    fun setNextArrowRemoval(removal: Int) {
        if (removal < 0) {
            throw RocketError("setNextArrowRemoval removal parameter must be between 0-2147483647L")
        }

        entity.nextArrowRemoval = removal
    }

    @TwineNativeProperty
    val nextArrowRemoval: Int
        get() = entity.nextArrowRemoval

    @TwineNativeProperty
    val beeStingerCooldown: Int
        get() = entity.beeStingerCooldown

    @TwineNativeFunction
    fun setBeeStingerCooldown(ticks: Int) {
        entity.beeStingerCooldown = ticks
    }

    @TwineNativeProperty
    val beeStingersInBody: Int
        get() = entity.beeStingersInBody

    @TwineNativeFunction
    fun setBeeStingersInBody(stingers: Int) {
        entity.beeStingersInBody = stingers
    }

    @TwineNativeFunction
    fun setNextBeeStingerRemoval(removal: Int) {
        if (removal < 0) {
            throw RocketError("setNextBeeStingerRemoval removal parameter must be between 0-2147483647L")
        }

        entity.nextBeeStingerRemoval = removal
    }

    @TwineNativeProperty
    val nextBeeStingerRemoval: Int
        get() = entity.nextBeeStingerRemoval

    @TwineNativeProperty
    val maximumNoDamageTicks: Int
        get() = entity.maximumNoDamageTicks

    @TwineNativeFunction
    fun setMaximumNoDamageTicks(ticks: Int) {
        entity.maximumNoDamageTicks = ticks
    }

    @TwineNativeProperty
    val lastDamage: Double
        get() = entity.lastDamage

    @TwineNativeProperty
    val noDamageTicks: Int
        get() = entity.noDamageTicks

    @TwineNativeFunction
    fun setNoDamageTicks(ticks: Int) {
        entity.noDamageTicks = ticks
    }

    @TwineNativeProperty
    val noActionTicks: Int
        get() = entity.noActionTicks

    @TwineNativeFunction
    fun setNoActionTicks(ticks: Int) {
        entity.noActionTicks = ticks
    }

    // TODO: Implement PotionEffect & PotionEffectType

    @TwineNativeFunction
    fun clearEffects() {
        entity.clearActivePotionEffects()
    }

    @TwineOverload
    @TwineNativeFunction
    fun hasLineOfSight(targetEntity: Entity<T>): Boolean {
        return entity.hasLineOfSight(targetEntity.entity)
    }

    @TwineOverload
    @TwineNativeFunction
    fun hasLineOfSight(location: LuaLocation): Boolean {
        return entity.hasLineOfSight(location.toBukkit())
    }

    @TwineNativeProperty
    val removeWhenFarAway: Boolean
        get() = entity.removeWhenFarAway

    @TwineNativeFunction
    fun setRemoveWhenFarAway(remove: Boolean) {
        entity.removeWhenFarAway = remove
    }

    // TODO: Implement EntityEquipment

    @TwineNativeFunction
    fun setCanPickupItems(canPickupItems: Boolean) {
        entity.canPickupItems = canPickupItems
    }

    @TwineNativeProperty
    val canPickupItems: Boolean
        get() = entity.canPickupItems

    @TwineNativeProperty
    val leashed: Boolean
        get() = entity.isLeashed

    @TwineNativeProperty
    val leashHolder: Entity<T>?
        get() = entity.leashHolder as Entity<T>?

    @TwineNativeFunction
    fun setLeashHolder(holderEntity: Entity<T>) {
        entity.setLeashHolder(holderEntity.entity)
    }

    @TwineNativeProperty
    val gliding: Boolean
        get() = entity.isGliding

    @TwineNativeFunction
    fun setGliding(gliding: Boolean) {
        entity.isGliding = true
    }

    @TwineNativeProperty
    val swimming: Boolean
        get() = entity.isSwimming

    @TwineNativeProperty
    val riptiding: Boolean
        get() = entity.isRiptiding

    @TwineNativeFunction
    fun setRiptiding(riptiding: Boolean) {
        entity.isRiptiding = riptiding
    }

    @TwineNativeProperty
    val isSleeping: Boolean
        get() = entity.isSleeping

    @TwineNativeProperty
    val isClimbing: Boolean
        get() = entity.isClimbing

    @TwineNativeFunction
    fun setAI(hasAI: Boolean) {
        entity.setAI(hasAI)
    }

    @TwineNativeProperty
    val AI: Boolean
        get() = entity.hasAI()

    @TwineNativeFunction
    fun attack(targetEntity: Entity<T>) {
        entity.attack(targetEntity.entity)
    }

    @TwineNativeFunction
    fun swingMainHand() {
        entity.swingMainHand()
    }

    @TwineNativeFunction
    fun swingOffHand() {
        entity.swingOffHand()
    }

    @TwineNativeFunction
    fun playHurtAnimation(yaw: Float) {
        entity.playHurtAnimation(yaw)
    }

    @TwineNativeFunction
    fun setCollidable(collidable: Boolean) {
        entity.isCollidable = collidable
    }

    @TwineNativeProperty
    val collidable: Boolean
        get() = entity.isCollidable

    @TwineNativeProperty
    val collidableExemptions: Set<String>
        get() = entity.collidableExemptions.map { it.toString() }.toSet()

    // TODO: Implement MemoryKey

    // TODO: Implement Sound

    @TwineNativeProperty
    val canBreatheUnderwater: Boolean
        get() = entity.canBreatheUnderwater()

    @TwineNativeProperty
    val shieldBlockingDelay: Int
        get() = entity.shieldBlockingDelay

    @TwineNativeFunction
    fun setShieldBlockingDelay(ticks: Int) {
        entity.shieldBlockingDelay = ticks
    }

    @TwineNativeProperty
    val sidewaysMovement: Float
        get() = entity.sidewaysMovement

    @TwineNativeProperty
    val upwardsMovement: Float
        get() = entity.upwardsMovement

    @TwineNativeProperty
    val forwardsMovement: Float
        get() = entity.forwardsMovement

    @TwineNativeFunction
    fun clearActiveItem() {
        entity.clearActiveItem()
    }

    @TwineNativeProperty
    val activeItemRemainingTime: Int
        get() = entity.activeItemRemainingTime

    @TwineNativeFunction
    fun setActiveItemRemainingTime(time: Int) {
        if (time < 0) {
            throw RocketError("setActiveItemRemainingTime time parameter must be between 0-2147483647L")
        }

        entity.activeItemRemainingTime = time
    }

    @TwineNativeProperty
    val activeItemUsedTime: Int
        get() = entity.activeItemUsedTime

    @TwineNativeProperty
    val jumping: Boolean
        get() = entity.isJumping

    @TwineNativeFunction
    fun setJumping(jumping: Boolean) {
        entity.isJumping = jumping
    }

    @TwineNativeProperty
    val hurtDirection: Float
        get() = entity.hurtDirection

    @TwineNativeProperty
    val bodyYaw: Float
        get() = entity.bodyYaw

    @TwineNativeFunction
    fun setBodyYaw(yaw: Float) {
        entity.bodyYaw = yaw
    }

    @TwineNativeFunction
    fun kill() {
        entity.health = 0.0
    }
}