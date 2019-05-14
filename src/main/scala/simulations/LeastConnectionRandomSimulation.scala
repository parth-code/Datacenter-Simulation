package simulations

import LoadbalancingBorkers.{LeastCpuUsageBroker, RandomSimBroker}
import VmAllocationPolicy.VmAllocationLeastConnection
import com.typesafe.config.ConfigFactory
import factory.{CloudletFactory, DatacenterFactory, PrintFactory, VmSpaceFactory}
import org.cloudbus.cloudsim.core.CloudSim
import org.slf4j.LoggerFactory

//Simulation with Least Connection allocation policy and Random broker
object LeastConnectionRandomSimulation {
	private val config = ConfigFactory.load()
	private val logger = LoggerFactory.getLogger("LeastConnectionRandomSimulation")
	
	def main(args: Array[String]): Unit = {
		val numVms = config.getInt("simulationConf.numVm")
		val numCloudlets = config.getInt("simulationConf.numCloudlets")
		val cloudsim = new CloudSim()
		
		val broker = new RandomSimBroker(cloudsim)
		
		val allocationPolicy1 = new VmAllocationLeastConnection()
		val allocationPolicy2 = new VmAllocationLeastConnection()
		
		//Multiple datacenters
		DatacenterFactory.createNormalDatacenter("smallDatacenter", cloudsim, allocationPolicy1)
		DatacenterFactory.createNormalDatacenter("smallDatacenter", cloudsim, allocationPolicy2)
		
		broker.submitVmList(VmSpaceFactory.createVmList("vm.standard", numVms / 3))
		broker.submitCloudletList(CloudletFactory.createCloudlets("cloudlet.standard", numCloudlets / 3))
		
		broker.submitVmList(VmSpaceFactory.createVmList("vm.memory_intensive", numVms / 3))
		broker.submitCloudletList(CloudletFactory.createCloudlets("cloudlet.memory_intensive", numCloudlets / 3))
		
		broker.submitVmList(VmSpaceFactory.createVmList("vm.cpu_intensive", numVms / 3))
		broker.submitCloudletList(CloudletFactory.createCloudlets("cloudlet.cpu_intensive", numCloudlets / 3))
		
		cloudsim.start()
		
		new PrintFactory(broker.getCloudletFinishedList() ,"smallDatacenter").build()
		
		//print out average and total
		PrintFactory.printAverages(broker.getCloudletFinishedList(), cloudsim)
	}
}
