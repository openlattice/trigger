package com.openlattice.trigger

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
data class TriggerConfiguration(
        val workingDirectory: String,
        val apiKey : String,
        val protectedTriggers: Set<String>
)