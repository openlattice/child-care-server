/*
 * Copyright (C) 2018. OpenLattice, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can contact the owner of the copyright at support@openlattice.com
 *
 */
package com.openlattice.childcare.pods


import com.dataloom.mappers.ObjectMappers
import com.fasterxml.jackson.databind.ObjectMapper
import com.kryptnostic.rhizome.configuration.service.ConfigurationService
import com.openlattice.auth0.Auth0Pod
import com.openlattice.auth0.Auth0TokenProvider
import com.openlattice.auth0.AwsAuth0TokenProvider
import com.openlattice.authentication.Auth0Configuration
import com.openlattice.childcare.configuration.ChildCareConfiguration
import com.openlattice.childcare.services.ChildCareService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.io.IOException
import javax.inject.Inject


@Configuration
@Import(Auth0Pod::class)
open class ChildCareServerServicesPod {

    @Inject
    private lateinit var auth0Configuration: Auth0Configuration

    @Inject
    private lateinit var configurationService: ConfigurationService

    @Bean
    open fun defaultObjectMapper(): ObjectMapper {
        return ObjectMappers.getJsonMapper()
    }

    @Bean(name = ["childCareConfiguration"])
    @Throws(IOException::class)
    open fun getChildCareConfiguration(): ChildCareConfiguration {
        return configurationService.getConfiguration(ChildCareConfiguration::class.java)!!
    }

    @Bean
    open fun auth0TokenProvider(): Auth0TokenProvider? {
        return AwsAuth0TokenProvider(auth0Configuration)
    }

    @Bean
    open fun childCareService(): ChildCareService {
        return ChildCareService(getChildCareConfiguration(), auth0Configuration)
    }
}