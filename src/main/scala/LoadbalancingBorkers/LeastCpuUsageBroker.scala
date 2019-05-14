package LoadbalancingBorkers

import java.util

import org.cloudbus.cloudsim.brokers.DatacenterBrokerSimple
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.vms.Vm
import org.slf4j.LoggerFactory

import scala.collection.mutable.Map
import collection.JavaConverters._

/**
	* Allocates cloudlets to vm based on their length(execution time)
	* @param simulation
	*/
class LeastCpuUsageBroker(val simulation: CloudSim) extends DatacenterBrokerSimple(simulation) {
	val logger = LoggerFactory.getLogger("LeastCpuUsageBroker")
	
	val vmMap = Map[Long, Long]()

	override def defaultVmMapper(cloudlet: Cloudlet): Vm = {
		if (cloudlet.isBindToVm) {
			val vm = cloudlet.getVm
			if (this == vm.getBroker && vm.isCreated)
				return vm
			logger.debug("Returning Vm.Null")
			return Vm.NULL
		}
		
		val vmList: java.util.List[Vm] = getVmCreatedList()
		
		//filter the list to vms that can support the
		val filteredList = vmList.asScala
		if (filteredList.isEmpty) {
			logger.debug("Returning Vm.NULL")
			return Vm.NULL
		}
		
		val sortedList = filteredList.sortWith((vm1, vm2) => {
			vmMap(vm1.getId) < vmMap(vm2.getId)
		})

		val selectedVm = sortedList.head
		//update the map
		vmMap(selectedVm.getId) += cloudlet.getLength
		selectedVm
	}

	override def submitVm(vm: Vm): Unit = {
		super.submitVm(vm)
		//add vm entry to the map
		vmMap += (vm.getId -> 0)
	}

	override def submitVmList(list: util.List[_ <: Vm]): Unit = {
		super.submitVmList(list)
		list.asScala.foreach(vm => vmMap += (vm.getId -> 0))
	}
}
