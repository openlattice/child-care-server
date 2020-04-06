package com.openlattice.childcare.apis

import retrofit2.http.GET


// @formatter:off
const val SERVICE = "/child-care"
const val CONTROLLER = "/explore"
const val BASE = SERVICE + CONTROLLER
// @formatter:on

const val TOKEN = "/token"


interface ChildCareApi {
    /**
     * Returns a read-only jwt token.
     */
    @GET(BASE + TOKEN)
    fun getReadToken(): String


}