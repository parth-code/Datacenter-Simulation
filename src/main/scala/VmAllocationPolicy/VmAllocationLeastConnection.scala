package VmAllocationPolicy

import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple
import org.cloudbus.cloudsim.hosts.Host
import org.cloudbus.cloudsim.vms.Vm
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable.ListBuffer

class VmAllocationLeastConnection extends VmAllocationPolicySimple {
	val log: Logger = LoggerFactory.getLogger("test")
	
	override def allocateHostForVm(vm: Vm): Boolean = {
		val hostlist: List[Host] = toSList(getHostList)
		
		//    Getting host with least number of connections
		def leastbusy(host1: Host, host2: Host): Host = {
			if (host1.getVmList.size() <= host2.getVmList.size()) host1 else host2
		}
		
		val leastbusyhost: Host = hostlist.minBy(_.getVmList.size())
		
		
		//    Check if hosts are created
		if (leastbusyhost.createVm(vm)) {
//			log.debug("Successfully allocated vm " + vm.getUid + " to host " + leastbusyhost.getId)
			true
		} else {
			false
		}
	}
	
	def toSList[T](l: java.util.List[T]): List[T] = {
		var a = ListBuffer[T]()
		for (r <- 0 until l.size) a += l.get(r)
		a.toList
	}
	
}
