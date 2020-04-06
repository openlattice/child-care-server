package com.openlattice.childcare.controllers

import com.openlattice.admin.CONTROLLER
import com.openlattice.childcare.apis.ChildCareApi
import com.openlattice.childcare.apis.TOKEN
import com.openlattice.childcare.services.ChildCareService
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@SuppressFBWarnings(
        value = ["RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE", "BC_BAD_CAST_TO_ABSTRACT_COLLECTION"],
        justification = "Allowing redundant kotlin null check on lateinit variables, " +
                "Allowing kotlin collection mapping cast to List")
@RestController
@RequestMapping(CONTROLLER)
class ChildCareController : ChildCareApi {

    companion object {
        private val logger = LoggerFactory.getLogger(ChildCareController::class.java)!!
    }

    @Inject
    private lateinit var childCareService: ChildCareService

    @GetMapping(value = [TOKEN])
    override fun getReadToken(): String {
        return childCareService.getJWTToken()
    }

}