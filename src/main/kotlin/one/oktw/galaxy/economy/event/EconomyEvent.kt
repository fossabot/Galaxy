package one.oktw.galaxy.economy.event

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import one.oktw.galaxy.Main
import one.oktw.galaxy.Main.Companion.galaxyManager
import one.oktw.galaxy.economy.service.EconomyService
import one.oktw.galaxy.galaxy.data.extensions.getMember
import one.oktw.galaxy.galaxy.data.extensions.saveMember
import one.oktw.galaxy.player.data.ActionBarData
import one.oktw.galaxy.player.service.ActionBar
import one.oktw.galaxy.translation.extensions.toLegacyText
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.text.Text
import org.spongepowered.api.text.format.TextColors.AQUA

class EconomyEvent {
    init {
        EconomyService
    }

    @SubscribeEvent
    fun onPickupExp(event: PlayerPickupXpEvent) {
        val player = event.entityPlayer as Player
        val lang = Main.translationService

        if (event.orb.xpValue == 0) return

        GlobalScope.launch {
            galaxyManager.get(player.world)?.run {
                getMember(player.uniqueId)
                    ?.apply {
                        giveStarDust(event.orb.xpValue)

                        Text.of(AQUA, lang.of("traveler.event.get_dust", event.orb.xpValue, starDust)).toLegacyText(player)
                            .let { ActionBar.setActionBar(player, ActionBarData(it, 2, 10)) }
                    }
                    ?.let { saveMember(it) }
            }
        }

    }
}
