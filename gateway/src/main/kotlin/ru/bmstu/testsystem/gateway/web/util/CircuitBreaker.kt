package ru.bmstu.testsystem.gateway.web.util

import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.ResourceAccessException
import javax.servlet.http.HttpServletRequest

internal enum class CircuitBreakerState {
    Closed, Open, HalfOpen
}

@Service
class CircuitBreaker {
    private val errorsLimit = 5
    private val _openToHalfOpenWaitTime: Long = 10000
    private var _errorsCount = 0
    private var _state = CircuitBreakerState.Closed
    private var _lastException: Exception? = null
    private var _lastStateChangedDateUtc: Long = 0
    private fun reset() {
        _errorsCount = 0
        _lastException = null
        _state = CircuitBreakerState.Closed
    }

    private fun isClosed(): Boolean {
        return _state == CircuitBreakerState.Closed
    }

    fun action(
        proxyService: ProxyService,
        body: String?,
        method: HttpMethod,
        request: HttpServletRequest,
        host: String,
        port: Int,
        path: String
    ): ResponseEntity<String> {
        if (isClosed()) {
            try {
                return proxyService.proxy(body, method, request, host, port, path)
            } catch (ex: ResourceAccessException) {
                trackException(ex)
                throw ex
            }
        } else {
            if (_state == CircuitBreakerState.HalfOpen || isTimerExpired()) {
                _state = CircuitBreakerState.HalfOpen
                try {
                    val res = proxyService.proxy(body, method, request, host, port, path)
                    reset()
                    return res
                } catch (ex: ResourceAccessException) {
                    reopen(ex)
                    throw ex
                }
            }
            throw _lastException!!
        }
    }

    private fun reopen(exception: ResourceAccessException) {
        _state = CircuitBreakerState.Open
        _lastStateChangedDateUtc = System.currentTimeMillis()
        _errorsCount = 0
        _lastException = exception
    }

    private fun isTimerExpired(): Boolean {
        return _lastStateChangedDateUtc + _openToHalfOpenWaitTime < System.currentTimeMillis()
    }

    private fun trackException(exception: Exception) {
        _errorsCount++
        if (_errorsCount >= errorsLimit) {
            _lastException = exception
            _state = CircuitBreakerState.Open
            _lastStateChangedDateUtc = System.currentTimeMillis()
        }
    }
}