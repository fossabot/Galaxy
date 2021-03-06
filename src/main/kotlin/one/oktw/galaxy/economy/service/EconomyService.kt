package one.oktw.galaxy.economy.service

import kotlinx.coroutines.reactive.consumeEach
import kotlinx.coroutines.runBlocking
import one.oktw.galaxy.Main.Companion.galaxyManager
import one.oktw.galaxy.Main.Companion.main
import org.spongepowered.api.scheduler.Task
import java.util.concurrent.TimeUnit

class EconomyService {
    companion object {
        init {
            Task.builder()
                .name("EconomyService")
                .async()
                .delay(20, TimeUnit.MINUTES)
                .interval(20, TimeUnit.MINUTES)
                .execute(::dailyTask)
                .submit(main)
        }

        private fun dailyTask() = runBlocking {
            galaxyManager.listGalaxy().consumeEach {
                it.giveInterest()
                galaxyManager.saveGalaxy(it)
            }
        }
    }
}
