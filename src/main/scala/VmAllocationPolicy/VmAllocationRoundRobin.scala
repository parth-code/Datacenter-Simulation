package VmAllocationPolicy

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple
import org.cloudbus.cloudsim.hosts.Host
import org.cloudbus.cloudsim.vms.Vm
import org.slf4j.LoggerFactory


class VmAllocationRoundRobin extends VmAllocationPolicySimple {
	
	val log = LoggerFactory.getLogger("VmAllocationPolicyRoundRobin")
	var previous = 0
	
	override def allocateHostForVm(vm: Vm): Boolean = {
		val hostList = getHostList
		if (previous >= hostList.size()) {
			previous = 0
		}
		
		val host = hostList.get(previous).asInstanceOf[Host]
		previous += 1
		//first check if host has enough resource for the vm
		if (host.createVm(vm)) {
			log.debug("Successfully allocated vm " + vm.getUid + " to host " + host.getId)
			true
		} else {
			allocate(vm, hostList.size() - 1)
		}
	}
	
	//tries to allocate host to vm, if not, recursively calls itself to try another host
	//has time to live counter to prevent infinite calls
	def allocate(vm: Vm, ttl: Int): Boolean = {
		if (ttl == 0)
			return false
		val hostList = getHostList
		if (previous >= hostList.size()) {
			previous = 0
		}
		
		val host = hostList.get(previous).asInstanceOf[Host]
		previous += 1
		//first check if host has enough resource for the vm
		if (host.createVm(vm)) {
			log.debug("Successfully allocated vm " + vm.getUid + " to host " + host.getId)
			true
		} else {
			allocate(vm, ttl - 1)
		}
	}
}
