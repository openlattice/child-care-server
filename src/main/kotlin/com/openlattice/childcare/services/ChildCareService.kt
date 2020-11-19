package com.openlattice.childcare.services

import com.auth0.exception.Auth0Exception
import com.google.common.util.concurrent.RateLimiter
import com.openlattice.auth0.Auth0Delegate;
import com.openlattice.authentication.Auth0Configuration;
import com.openlattice.childcare.configuration.ChildCareConfiguration
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled


class ChildCareService(
        val config: ChildCareConfiguration,
        val auth0Configuration: Auth0Configuration
) {

    lateinit var token: String

    private val auth0Client: Auth0Delegate

    private val rateLimiter = RateLimiter.create(1_000.0)


    companion object {
        protected val logger = LoggerFactory.getLogger(ChildCareService::class.java)
    }

    init {
        this.auth0Client = Auth0Delegate.fromConfig(auth0Configuration)
        refresh()
    }

    @Scheduled(fixedRate = 60000 * 60 * 10) // 10 hours
    private fun refresh() {
        try {
            token = auth0Client.getIdToken(config.username, config.password)
        } catch (e: Auth0Exception) {
            logger.error("Unable to reload jwt token: ", e)
        }
    }

    fun getJWTToken(): String {
        rateLimiter.acquire()
        return token
    }

}