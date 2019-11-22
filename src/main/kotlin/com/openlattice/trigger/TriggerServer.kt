package com.openlattice.trigger

import com.kryptnostic.rhizome.configuration.websockets.BaseRhizomeServer
import com.openlattice.auditing.pods.AuditingConfigurationPod
import com.openlattice.aws.AwsS3Pod
import com.openlattice.datastore.pods.ByteBlobServicePod
import com.openlattice.hazelcast.pods.MapstoresPod
import com.openlattice.hazelcast.pods.SharedStreamSerializersPod
import com.openlattice.jdbc.JdbcPod
import com.openlattice.postgres.PostgresPod
import com.openlattice.shuttle.pods.TriggerServicesPod
import com.openlattice.shuttle.pods.TriggerServletsPod
import com.openlattice.tasks.pods.TaskSchedulerPod
import com.openlattice.trigger.pods.TriggerMvcPod

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
private val triggerPods = arrayOf(
        AuditingConfigurationPod::class.java,
        AwsS3Pod::class.java,
        ByteBlobServicePod::class.java,
        JdbcPod::class.java,
        MapstoresPod::class.java,
        PostgresPod::class.java,
        SharedStreamSerializersPod::class.java,
        TaskSchedulerPod::class.java,
        TriggerServicesPod::class.java,
        TriggerServletsPod::class.java,
        TriggerMvcPod::class.java
)

/**
 *
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
class TriggerServer : BaseRhizomeServer(*triggerPods)