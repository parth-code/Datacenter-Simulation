package factory

import java.util

import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.vms.network.NetworkVm
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}

import scala.collection.mutable.ListBuffer
import collection.JavaConverters._

object VmFactory {
	
	private val config = ConfigFactory.load()
	
	/**
	  * Creates a list of VMs per the configuration provided by the path.
	  * NOTE: the configuration must have the following properties:
	  * -ram
	  * -mips
	  * -numPe
	  * -bw
	  * -storage
	  *
	  * @param configPath a path to a VM configuration in the config file
	  * @return A java.util.list of vms of specified length
	  */
	def createVmList(configPath: String, num: Int): util.List[Vm] = {
		val ram = config.getInt(configPath + ".ram")
		val bw = config.getInt(configPath + ".bw")
		val mips = config.getInt(configPath + ".mips")
		val storage = config.getInt(configPath + ".storage")
		val numPe = config.getInt(configPath + ".numPe")
		
		val vms = new ListBuffer[Vm]
		(1 to num).foreach(_ => {
			val vm = new VmSimple(mips, numPe).setRam(ram).setBw(bw).setSize(storage)
			vm.getUtilizationHistory.enable()
			vms.append(vm)
		})
		vms.foreach(vm =>vm.getUtilizationHistory.enable())
		vms.asJava
	}

	/**
		* Returns a list of Vms of different types.
		* NOTE: the configuration must have the following properties:
		* -ram
		* -mips
		* -numPe
		* -bw
		* -storage
		*	-numVms
		* @param configPath a path to multiple VM configurations in the config file
		* @return A java.util.list of vms of specified length
		*/
	def createComplexVmList(configPath: String): util.List[Vm] = {
		val vmConfigList = config.getObjectList(configPath)
		val vmlist = new ListBuffer[Vm]
		vmConfigList.forEach(
			vm => {
				val ram = vm.get("ram").unwrapped().asInstanceOf[Int]
				val bw = vm.get("bw").unwrapped().asInstanceOf[Int]
				val mips = vm.get("mips").unwrapped().asInstanceOf[Int]
				val storage = vm.get("storage").unwrapped().asInstanceOf[Int]
				val numPe = vm.get("numPe").unwrapped().asInstanceOf[Int]
				val numVm = vm.get("numVm").unwrapped().asInstanceOf[Int]
				(1 to numVm).foreach(_ => {
					val vm = new VmSimple(mips, numPe).setRam(ram).setBw(bw).setSize(storage)
					vm.getUtilizationHistory.enable()
					vmlist.append(vm)
				})
			})
			vmlist.asJava
	}



	
}
