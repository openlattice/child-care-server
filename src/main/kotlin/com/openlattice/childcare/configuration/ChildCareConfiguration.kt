package com.openlattice.childcare.configuration

import com.fasterxml.jackson.annotation.JsonProperty
import com.kryptnostic.rhizome.configuration.annotation.ReloadableConfiguration

/**
 * Configuration class for auditing.
 */
@ReloadableConfiguration(uri = "childcare.yaml")
data class ChildCareConfiguration(
        @JsonProperty("username") val username: String,
        @JsonProperty("password") val password: String
)
