package simulations

import LoadbalancingBorkers.RandomSimBroker
import VmAllocationPolicy.VmAllocationRoundRobin
import com.typesafe.config.ConfigFactory
import factory.{CloudletFactory, DatacenterFactory, PrintFactory, PrintPowerFactory, VmSpaceFactory}
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.hosts.Host
import org.cloudbus.cloudsim.vms.Vm
import org.slf4j.LoggerFactory

import scala.collection.JavaConverters._

/**
  * Simulation for Round Robin with Random with power profile
  */
object RoundRobinRandomPowerSimulation {
	private val logger = LoggerFactory.getLogger("PowerSim:")
	private val config = ConfigFactory.load()
	
	def main(args: Array[String]): Unit = {
		logger.info("Random Simulation starting")
		val numVms = config.getInt("simulationConf.numVm")
		val numCloudlets = config.getInt("simulationConf.numCloudlets")
		
		val cloudsim = new CloudSim()
		
		val broker = new RandomSimBroker(cloudsim)
		
		val allocationPolicy1 = new VmAllocationRoundRobin()
		val allocationPolicy2 = new VmAllocationRoundRobin()
		
		val hostlist1: List[Host] = DatacenterFactory.createPowerDatacenter("powerDatacenter", cloudsim, allocationPolicy1)
		val hostlist2: List[Host] = DatacenterFactory.createPowerDatacenter("powerDatacenter", cloudsim, allocationPolicy2)
		
		
		broker.submitVmList(VmSpaceFactory.createVmList("vm.standard", numVms / 3))
		broker.submitCloudletList(CloudletFactory.createCloudlets("cloudlet.standard", numCloudlets / 3))
		
		broker.submitVmList(VmSpaceFactory.createVmList("vm.memory_intensive", numVms / 3))
		broker.submitCloudletList(CloudletFactory.createCloudlets("cloudlet.memory_intensive", numCloudlets / 3))
		
		broker.submitVmList(VmSpaceFactory.createVmList("vm.cpu_intensive", numVms / 3))
		broker.submitCloudletList(CloudletFactory.createCloudlets("cloudlet.cpu_intensive", numCloudlets / 3))
		
		cloudsim.start()
		new PrintFactory(broker.getCloudletFinishedList(), "powerDatacenter").build()
		//print out average and total
		PrintFactory.printAverages(broker.getCloudletFinishedList(), cloudsim)
		
		val vmList: java.util.List[Vm] = broker.getVmCreatedList()
		PrintPowerFactory.printPowerUtilization(vmList.asScala.toList)
	}
}
