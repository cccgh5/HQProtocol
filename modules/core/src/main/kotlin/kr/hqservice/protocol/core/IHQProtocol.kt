package kr.hqservice.protocol.core

import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.protocol.api.HQProtocol

interface IHQProtocol : HQProtocol {
    fun injectHandler(plugin: HQBukkitPlugin)
}