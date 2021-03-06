package com.openlattice.childcare.pods


import com.fasterxml.jackson.databind.ObjectMapper
import com.openlattice.childcare.controllers.ChildCareController
import com.openlattice.childcare.util.ChildCareServerExceptionHandler
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import javax.inject.Inject


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
*/
@Configuration
@ComponentScan(basePackageClasses = [ChildCareController::class, ChildCareServerExceptionHandler::class], includeFilters = [ComponentScan.Filter(value = [Controller::class, RestControllerAdvice::class], type = org.springframework.context.annotation.FilterType.ANNOTATION)])
@EnableAsync
@EnableMetrics(proxyTargetClass = true)
open class ChildCareServerMvcPod : WebMvcConfigurationSupport() {

    @Inject
    private lateinit var defaultObjectMapper: ObjectMapper

    @Inject
    private lateinit var childCareServerSecurityPod: ChildCareServerSecurityPod

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        super.addDefaultHttpMessageConverters(converters)
        for (converter in converters) {
            if (converter is MappingJackson2HttpMessageConverter) {
                val jacksonConverter: MappingJackson2HttpMessageConverter = converter as MappingJackson2HttpMessageConverter
                jacksonConverter.setObjectMapper(defaultObjectMapper)
            }
        }
    }

    // TODO: We need to lock this down. Since all endpoints are stateless + authenticated this is more a
// defense-in-depth measure.
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
                .addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedOrigins("*")
        super.addCorsMappings(registry)
    }

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        configurer.parameterName("fileType")
                .favorParameter(true)
                .mediaType("json", org.springframework.http.MediaType.APPLICATION_JSON)
                .defaultContentType(org.springframework.http.MediaType.APPLICATION_JSON)
    }

    @Bean
    @Throws(Exception::class)
    open fun authenticationManagerBean(): AuthenticationManager {
        return childCareServerSecurityPod.authenticationManagerBean()
    }
}