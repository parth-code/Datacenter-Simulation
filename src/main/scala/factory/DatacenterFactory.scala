package factory

import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicy
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.datacenters.{Datacenter, DatacenterSimple}
import org.cloudbus.cloudsim.datacenters.network.NetworkDatacenter
import org.cloudbus.cloudsim.hosts.network.NetworkHost
import org.cloudbus.cloudsim.hosts.{Host, HostSimple}
import org.cloudbus.cloudsim.power.models.PowerModelLinear
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple
import org.cloudbus.cloudsim.resources.{Pe, PeSimple}

import scala.collection.mutable.ListBuffer
import collection.JavaConverters._

/**
  * A util object with methods to create standardized datacenters
  */
object DatacenterFactory {
	
	private val config = ConfigFactory.load()

	/**
		* Given config path, it creates a datacenter.
		* NOTE the configuration must have the following fields:
		* -hostCount
		* -peCount
		* -MIPS
		* -RAM
		* -storage
		* -bandwidth
		*
		* @param configPath
		* @param simulation
		* @param vmAllocationPolicy custom allocation policy in use
		*/
	def createNormalDatacenter(configPath: String, simulation: CloudSim, vmAllocationPolicy: VmAllocationPolicy): Datacenter  ={
		//load values from the configuration file
		val peCount = config.getInt(configPath + ".host.peCount")
		val mips = config.getInt(configPath + ".host.MIPS")
		val hostRam = config.getInt(configPath + ".host.RAM")
		val hostBw = config.getInt(configPath + ".host.bandwidth")
		val hostStorage = config.getInt(configPath + ".host.storage")
		val numHosts = config.getInt(configPath + ".hostCount")
		val costMem = config.getDouble(configPath + ".cost.perMem")
		val costBw = config.getDouble(configPath + ".cost.perBW")
		val costSec = config.getDouble(configPath + ".cost.perSec")
		val costStorage = config.getDouble(configPath + ".cost.perStorage")
		
		
		def createHost(): Host = {
			//create the processing units
			val peList = new ListBuffer[Pe]
			(1 to peCount).foreach(_ => peList.append(
				new PeSimple(
					mips, new PeProvisionerSimple())))
			new HostSimple(
				hostRam, hostBw, hostStorage, peList.asJava)
		}
		
		val hosts = new ListBuffer[Host]
		(1 to numHosts).foreach(_ => hosts.append(createHost()))
		
		val dc =new DatacenterSimple(simulation, hosts.asJava, vmAllocationPolicy)
		val characteristics = dc.getCharacteristics
		characteristics.setCostPerBw(costBw)
		characteristics.setCostPerMem(costMem)
		characteristics.setCostPerSecond(costSec)
		characteristics.setCostPerStorage(costStorage)
		dc
	}

	/**
		* Given config path, it creates a datacenter for a power simulation.
		* NOTE the configuration must have the following fields:
		* -hostCount
		* -peCount
		* -MIPS
		* -RAM
		* -storage
		* -bandwidth
		* -maxPower ie. Maximum power a host can use in a second
		* -staticPowerPercent power consumed at idle by host
		*
		* @param configPath
		* @param simulation
		* @param vmAllocationPolicy custom allocation policy in use
		*/
	def createPowerDatacenter(configPath: String, simulation: CloudSim, vmAllocationPolicy: VmAllocationPolicy): List[Host]={
		//load values from the configuration file
		val peCount = config.getInt(configPath + ".host.peCount")
		val mips = config.getInt(configPath + ".host.MIPS")
		val hostRam = config.getInt(configPath + ".host.RAM")
		val hostBw = config.getInt(configPath + ".host.bandwidth")
		val hostStorage = config.getInt(configPath + ".host.storage")
		val numHosts = config.getInt(configPath + ".hostCount")
		val maxPower = config.getInt(configPath + ".power.maxPower")
		val powerPercent = config.getInt(configPath + ".power.powerPercent")
		val costMem = config.getDouble(configPath + ".cost.perMem")
		val costBw = config.getDouble(configPath + ".cost.perBW")
		val costSec = config.getDouble(configPath + ".cost.perSec")
		val costStorage = config.getDouble(configPath + ".cost.perStorage")
		
		def createHost(): Host = {
			//create the processing units
			val peList = new ListBuffer[Pe]
			(0 to peCount).foreach(_ => peList.append(
				new PeSimple(
					mips, new PeProvisionerSimple())))
				new HostSimple(
					hostRam, hostBw, hostStorage, peList.asJava)
					.setPowerModel(
						new PowerModelLinear(
							maxPower,
							powerPercent
						))
		}

		val hosts = new ListBuffer[Host]
		(0 to numHosts).foreach(_ => hosts.append(createHost()))

		val dc = new DatacenterSimple(simulation, hosts.asJava, vmAllocationPolicy)
		val characteristics = dc.getCharacteristics
		characteristics.setCostPerBw(costBw)
		characteristics.setCostPerMem(costMem)
		characteristics.setCostPerSecond(costSec)
		characteristics.setCostPerStorage(costStorage)
		hosts.toList
	}


	def createNetworkDatacenter(configPath: String, simulation: CloudSim, vmAllocationPolicy: VmAllocationPolicy): List[NetworkHost] ={
		//load values from the configuration file
		val peCount = config.getInt(configPath + ".host.peCount")
		val mips = config.getInt(configPath + ".host.MIPS")
		val hostRam = config.getInt(configPath + ".host.RAM")
		val hostBw = config.getInt(configPath + ".host.bandwidth")
		val hostStorage = config.getInt(configPath + ".host.storage")
		val numHosts = config.getInt(configPath + ".hostCount")
		val costMem = config.getDouble(configPath + ".cost.perMem")
		val costBw = config.getDouble(configPath + ".cost.perBW")
		val costSec = config.getDouble(configPath + ".cost.perSec")
		val costStorage = config.getDouble(configPath + ".cost.perStorage")

		def createHost(): NetworkHost = {
			//create the processing units
			val peList = new ListBuffer[Pe]
			(0 to peCount).foreach(_ => peList.append(
				new PeSimple(
					mips, new PeProvisionerSimple())))
			new NetworkHost(
				hostRam, hostBw, hostStorage, peList.asJava)
		}

		val hosts = new ListBuffer[NetworkHost]
		(0 to numHosts).foreach(_ => hosts.append(createHost()))

		val dc = new NetworkDatacenter(simulation, hosts.asJava, vmAllocationPolicy)
		val characteristics = dc.getCharacteristics
		characteristics.setCostPerBw(costBw)
		characteristics.setCostPerMem(costMem)
		characteristics.setCostPerSecond(costSec)
		characteristics.setCostPerStorage(costStorage)
		hosts.toList
	}

	
}
