package simulations

import LoadbalancingBorkers.{LeastCpuUsageBroker, RandomSimBroker}
import VmAllocationPolicy.VmAllocationLeastConnection
import com.typesafe.config.ConfigFactory
import factory._
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.hosts.Host
import org.cloudbus.cloudsim.vms.Vm
import org.slf4j.LoggerFactory
import scala.collection.JavaConverters._


//Simulation with Least Connection vm allocation and Least CPU broker, with a power profile
object LeastConnectionLeastCpuPowerSimulation {
	private val logger = LoggerFactory.getLogger("LeastConnectionLeastCpuPowerSimulation")
	private val config = ConfigFactory.load()
	
	def main(args: Array[String]): Unit = {
		logger.info("Random Simulation starting")
		val numVms = config.getInt("simulationConf.numVm")
		val numCloudlets = config.getInt("simulationConf.numCloudlets")
		
		val cloudsim = new CloudSim()
		
		val broker = new LeastCpuUsageBroker(cloudsim)
		
		val allocationPolicy1 = new VmAllocationLeastConnection()
		val allocationPolicy2 = new VmAllocationLeastConnection()
		
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
