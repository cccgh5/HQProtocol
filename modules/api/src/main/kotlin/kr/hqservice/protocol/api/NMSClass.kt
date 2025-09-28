package kr.hqservice.protocol.api

interface NMSClass<C : Any> {
    fun handle(): C
}