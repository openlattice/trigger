package com.openlattice.shuttle.pods

import com.kryptnostic.rhizome.configuration.servlets.DispatcherServletConfiguration
import com.openlattice.trigger.pods.TriggerMvcPod
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
@Configuration
class TriggerServletsPod {
    @Bean
    fun triggerServlet(): DispatcherServletConfiguration {

        return DispatcherServletConfiguration(
                "shuttle",
                arrayOf("/shuttle/*"),
                1,
                listOf(TriggerMvcPod::class.java)
        )
    }


}