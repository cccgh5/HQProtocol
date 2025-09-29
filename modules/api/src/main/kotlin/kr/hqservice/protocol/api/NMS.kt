package kr.hqservice.protocol.api

interface NMS<C : Any> {
    fun handle(): C
}