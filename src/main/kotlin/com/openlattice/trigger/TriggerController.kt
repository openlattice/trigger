package com.openlattice.trigger

import com.openlattice.authorization.AuthorizationManager
import com.openlattice.authorization.AuthorizingComponent
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.NotImplementedException
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject
import javax.servlet.http.HttpServletResponse

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
@RestController
@RequestMapping("/control")
class TriggerController : AuthorizingComponent, TriggerApi {
    @Inject
    private lateinit var triggerService: TriggerService
    @Inject
    private lateinit var triggerConfiguration: TriggerConfiguration

    @Inject
    private lateinit var authzManager: AuthorizationManager

    override fun getAuthorizationManager(): AuthorizationManager {
        return authzManager
    }


    override  fun createTrigger(trigger: Trigger, apiKey: String): Boolean {
        ensureAdminAccess()
        ensureAllowed(trigger.name, apiKey)
        return triggerService.createTrigger(trigger)
    }

    override fun deleteTrigger(name: String, apiKey: String): Void? {
        ensureAdminAccess()
        ensureAllowed(name, apiKey)
        triggerService.deleteTrigger(name)
        return null
    }

    override fun fireTrigger(name: String, apiKey: String, Set<String>): Void {
        throw NotImplementedException("Purposefully not implemented and shouldn't be invoked.")
    }

    fun fireTrigger(name: String, apiKey: String, response: HttpServletResponse) {
        ensureAdminAccess()
        ensureAllowed(name, apiKey)
        IOUtils.copy(triggerService.fireTrigger(name), response.outputStream)
        response.flushBuffer()
    }

    fun ensureAllowed(name: String, apiKey: String) {
        require(!triggerConfiguration.protectedTriggers.contains(name) || triggerConfiguration.apiKey == apiKey) {
            "You are not authorized to do this."
        }
    }
}