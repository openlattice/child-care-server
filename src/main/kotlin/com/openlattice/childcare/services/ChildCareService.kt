package com.openlattice.childcare.services

import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import com.google.common.util.concurrent.RateLimiter
import com.openlattice.auth0.Auth0Delegate;
import com.openlattice.authentication.Auth0Configuration;
import com.openlattice.childcare.configuration.ChildCareConfiguration
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

class ChildCareService(
        val config: ChildCareConfiguration,
        auth0Configuration: Auth0Configuration
) {
    private val auth0Client: Auth0Delegate = Auth0Delegate.fromConfig(auth0Configuration)

    private val tokenSupplier: Supplier<String> = Suppliers.memoizeWithExpiration({
        try {
            logger.info("getting id token from auth0...")
            auth0Client.getIdToken(config.username, config.password)
        } catch (ex: Exception) {
            logger.error("Failed to retrieve a token from auth0", ex)
            throw ex
        }
    }, 10, TimeUnit.HOURS)

    companion object {
        private val logger = LoggerFactory.getLogger(ChildCareService::class.java)
    }

    private val rateLimiter = RateLimiter.create(1_000.0)

    fun getJWTToken(): String {
        rateLimiter.acquire()
        return tokenSupplier.get()
    }
}
