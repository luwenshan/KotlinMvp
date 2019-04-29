package com.lws.kotlinmvp.common.exception

/**
 * 订阅处理异常
 * 用于统一订阅处理的逻辑异常 (包括服务端返回的数据引起的异常)
 */
class SubscribeException(throwable: Throwable) : Exception(throwable)