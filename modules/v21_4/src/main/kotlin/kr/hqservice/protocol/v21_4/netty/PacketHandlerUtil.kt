package kr.hqservice.protocol.v21_4.netty

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise

inline fun ChannelHandlerContext.ensureOnEventLoop(crossinline command: () -> Unit) {
    if (executor().inEventLoop()) {
        command.invoke()
    } else {
        executor().execute { command.invoke() }
    }
}

fun ChannelPromise.trySuccessOnEventLoop(context: ChannelHandlerContext) {
    val executor = context.executor()
    if (executor.inEventLoop()) {
        trySuccess()
    } else {
        executor.execute { trySuccess() }
    }
}

fun ChannelPromise.tryFailureOnEventLoop(context: ChannelHandlerContext, throwable: Throwable) {
    val executor = context.executor()
    if (executor.inEventLoop()) {
        tryFailure(throwable)
    } else {
        executor.execute { tryFailure(throwable) }
    }
}