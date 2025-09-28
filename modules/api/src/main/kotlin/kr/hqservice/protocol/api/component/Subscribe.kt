package kr.hqservice.protocol.api.component

import kr.hqservice.framework.global.core.component.Scannable

@Scannable
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class Subscribe(
    val priority: HandlerPriority = HandlerPriority.NORMAL
)
