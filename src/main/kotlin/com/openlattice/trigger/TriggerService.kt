package com.openlattice.trigger

import com.amazonaws.util.EC2MetadataUtils
import com.hazelcast.core.HazelcastInstance
import com.hazelcast.core.Message
import com.hazelcast.core.MessageListener
import com.openlattice.controllers.exceptions.ResourceNotFoundException
import com.openlattice.hazelcast.HazelcastMap
import com.openlattice.hazelcast.HazelcastTopic
import com.openlattice.triggers.TriggerEvent
import java.io.File
import java.io.InputStream
import java.net.InetAddress
import java.util.concurrent.TimeUnit

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
class TriggerService(hazelcastInstance: HazelcastInstance,

                     private val configuration: TriggerConfiguration) : MessageListener<TriggerEvent> {
    private val triggers = hazelcastInstance.getMap<String, Trigger>(HazelcastMap.TRIGGERS.name )
    private val triggerTopic = hazelcastInstance.getReliableTopic<TriggerEvent>(HazelcastTopic.TRIGGERS.name)
    private val node: String = EC2MetadataUtils.getPrivateIpAddress()
    private val id = EC2MetadataUtils.getInstanceId()

    init {
        triggerTopic.addMessageListener(this)
    }

    override fun onMessage(message: Message<TriggerEvent>?) {

    }
    /**
     * @param trigger The trigger to create
     * @return Returns true if the trigger was created or false if it already exists.
     */
    fun createTrigger(trigger: Trigger) : Boolean {
        return triggers.putIfAbsent(trigger.name, trigger) == null
    }

    fun getTrigger( name: String ) : Trigger {
        return triggers[name] ?: throw ResourceNotFoundException("Trigger with name $name was not found.")
    }

    fun fireTrigger( name: String ): InputStream {
        val trigger = triggers.getValue( name )

        val proc = ProcessBuilder(*trigger.args.toTypedArray())
                .directory(File(configuration.workingDirectory) )
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

        proc.waitFor(15, TimeUnit.MINUTES)
        return proc.inputStream
    }

    fun deleteTrigger(name: String) {
        triggers.delete( name )
    }
}