package one.oktw.galaxy.command.admin

import one.oktw.galaxy.command.CommandBase
import one.oktw.galaxy.command.admin.galaxyManage.*
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.spec.CommandSpec

class GalaxyManage : CommandBase {
    override val spec: CommandSpec
        get() = CommandSpec.builder()
            .permission("oktw.command.admin.galaxyManage")
            .child(CreateGalaxy().spec, "createGalaxy")
            .child(CreatePlanet().spec, "createPlanet")
            .child(AddMember().spec, "addMember")
            .child(SetGroup().spec, "setGroup")
            .child(RemoveMember().spec, "removeMember")
            .child(TransferOwner().spec, "transferOwner")
            .child(Rename().spec, "rename")
            .child(Info().spec, "info")
            .child(Notice().spec, "notice")
            .child(SetSize().spec, "setSize")
            .child(SetVisit().spec, "setVisit")
            .child(RemoveGalaxy().spec, "removeGalaxy")
            .child(RemovePlanet().spec, "removePlanet")
            .child(Dividends().spec, "dividends")
            .child(GiveStarDust().spec, "giveStarDust")
            .child(TakeStarDust().spec, "takeStarDust")
            .build()

    override fun execute(src: CommandSource, args: CommandContext): CommandResult {
        src.sendMessage(spec.getUsage(src))

        return CommandResult.success()
    }
}
