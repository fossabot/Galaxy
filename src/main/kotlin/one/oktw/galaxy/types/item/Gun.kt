package one.oktw.galaxy.types.item

import one.oktw.galaxy.enums.GunType
import one.oktw.galaxy.types.Upgrade
import java.util.*

data class Gun(
        val uuid: UUID = UUID.randomUUID(),
        var type: GunType = GunType.ORIGIN,
        var coolDown: Double = 5.0,
        var range: Double = 10.0,
        var damage: Double = 3.0,
        var through: Int = 1,
        var slot: Int = 1,
        var upgrade: List<Upgrade> = ArrayList()
)