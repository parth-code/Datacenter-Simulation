package factory

import java.util.DoubleSummaryStatistics

import org.cloudbus.cloudsim.hosts.Host
import org.cloudbus.cloudsim.vms.{UtilizationHistory, Vm}

import scala.collection.SortedMap
import scala.collection.JavaConverters._
import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.core.Simulation
import org.cloudbus.cloudsim.hosts.network.NetworkHost
import org.cloudbus.cloudsim.power.models.PowerAware
import org.cloudbus.cloudsim.hosts.network.NetworkHost

/**
  *
  */
object PrintPowerFactory {
	val config = ConfigFactory.load()
	
	def printNetworkHostByteUtilization(hostlist: scala.List[NetworkHost]): Unit = {
		
		hostlist.foreach(nh => {
			println("Host" + nh.getId + " Data Transfered: " + nh.getTotalDataTransferBytes)
		})
		
	}
	
	/**
	  *
	  * @param vmlist
	  */
	def printPowerUtilization(vmlist: scala.List[Vm]):Unit = {
		var total:Double = 0
		vmlist.foreach(vm =>{
			total += printEachVmPowerUtilization(vm)
		})
		println("Total Power Consumption: " + total)
		println("Average Power Consumption: " + (total/vmlist.size))
	}
	
	/**
	  *
	  * @param vm
	  */
	private def printEachVmPowerUtilization(vm: Vm):Double = {
		var previoustime: Double = 0
		val utilizationHistory: UtilizationHistory = vm.getUtilizationHistory
		var vmPowerTotal = 0.0
		utilizationHistory.getHistory.keySet().forEach(
			time => { vmPowerTotal += utilizationHistory.powerConsumption(time)
		})
		vmPowerTotal
	}
}
