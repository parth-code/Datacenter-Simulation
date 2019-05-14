package simulations

import LoadbalancingBorkers.RandomSimBroker
import VmAllocationPolicy.VmAllocationRoundRobin
import com.typesafe.config.ConfigFactory
import factory._
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.hosts.Host
import org.cloudbus.cloudsim.hosts.network.NetworkHost
import org.slf4j.LoggerFactory

//Network profile simulation
object NetworkSimulation extends App{

    private val logger = LoggerFactory.getLogger("NewtWorkedSim:")
    private val config = ConfigFactory.load()

    override def main(args: Array[String]): Unit = {
        super.main(args)

        logger.info("Network Simulation starting")
        val numVms = config.getInt("simulationConf.numVm")
        val numCloudlets = config.getInt("simulationConf.numCloudlets")
        val cloudsim = new CloudSim()

        val broker = new RandomSimBroker(cloudsim)

        val allocationPolicy = new VmAllocationRoundRobin()
        val hostlist: List[NetworkHost] = DatacenterFactory.createNetworkDatacenter("smallDatacenter", cloudsim, allocationPolicy)

        logger.debug("Number of Vms: " + numVms)
        val vms = VmSpaceFactory.createNetWorkVmList("simulationConf.vm", 10)
        val cloudlets = CloudletFactory.createNetworkCloudlets("simulationConf.cloudlet", numCloudlets)


        broker.submitVmList(vms)
        broker.submitCloudletList(cloudlets)

        cloudsim.start()

        new PrintFactory(broker.getCloudletFinishedList() ,"smallDatacenter").build()
        PrintPowerFactory.printNetworkHostByteUtilization(hostlist)

    }

}


