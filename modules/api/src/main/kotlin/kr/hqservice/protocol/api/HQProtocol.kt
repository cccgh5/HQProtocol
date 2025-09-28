package kr.hqservice.protocol.api

import kotlin.reflect.KClass

interface HQProtocol {
    fun <C : Any> wrapNMS(craftInstance: C): NMSClass<C>?

    fun <T : NMSClass<C>, C : Any> wrapNMS(craftInstance: C, nmsClass: KClass<T>): T?
}