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
package com.openlattice.childcare

import com.kryptnostic.rhizome.configuration.websockets.BaseRhizomeServer
import com.kryptnostic.rhizome.core.RhizomeApplicationServer
import com.kryptnostic.rhizome.hazelcast.serializers.RhizomeUtils.Pods
import com.openlattice.auth0.Auth0Pod
import com.openlattice.childcare.pods.ChildCareServerSecurityPod
import com.openlattice.childcare.pods.ChildCareServerServicesPod
import com.openlattice.childcare.pods.ChildCareServerServletsPod

class ChildCareServer(vararg pods: Class<*>?) : BaseRhizomeServer(*Pods.concatenate(
        pods,
        webPods,
        rhizomePods,
        childCarePods,
        RhizomeApplicationServer.DEFAULT_PODS)) {
    companion object {

        private val webPods = arrayOf<Class<*>>(
                ChildCareServerServletsPod::class.java,
                ChildCareServerSecurityPod::class.java)
        val rhizomePods = arrayOf<Class<*>>(
                Auth0Pod::class.java)

        val childCarePods = arrayOf<Class<*>>(
                ChildCareServerServicesPod::class.java
        )

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val childCareServer = ChildCareServer()
            childCareServer.start(*args)
        }
    }

    @Throws(Exception::class)
    override fun start(vararg profiles: String) {
        super.start(*profiles)
    }
}