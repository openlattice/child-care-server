package com.openlattice.childcare.controllers

import com.openlattice.childcare.apis.CONTROLLER
import com.openlattice.childcare.apis.ChildCareApi
import com.openlattice.childcare.apis.TOKEN
import com.openlattice.childcare.services.ChildCareService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
@RequestMapping(CONTROLLER)
class ChildCareController : ChildCareApi {

    @Inject
    private lateinit var childCareService: ChildCareService

    @GetMapping(value = [TOKEN])
    override fun getReadToken(): String {
        return childCareService.getJWTToken()
    }

}