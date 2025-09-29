package kr.hqservice.protocol.core

import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup
import kr.hqservice.framework.global.core.util.AnsiColor
import java.util.logging.Logger

@Module
class HQProtocolBootstrap(
    private val plugin: HQBukkitPlugin,
    private val logger: Logger,
    private val protocol: IHQProtocol
) {
    @Setup
    fun setup() {
        protocol.injectHandler(plugin)
        logger.info("${AnsiColor.CYAN}Protocol Handler Injected.${AnsiColor.RESET}")
    }
}