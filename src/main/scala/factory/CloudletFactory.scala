package factory

import com.typesafe.config.ConfigFactory
import java.util

import factory.VmFactory.config
import org.cloudbus.cloudsim.cloudlets.network.{CloudletExecutionTask, NetworkCloudlet}
import org.cloudbus.cloudsim.cloudlets.{Cloudlet, CloudletSimple}
import org.cloudbus.cloudsim.utilizationmodels.{UtilizationModelDynamic, UtilizationModelFull}
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}

import scala.collection.mutable.ListBuffer
import collection.JavaConverters._


object CloudletFactory {
	private val config = ConfigFactory.load()
	
	/**
	  * Given a config path, it creates a list of clouds
	  * NOTE the configuration must have the following fields:
	  * <ul>
	  * <li>length (in million of instructions)
	  * <li>peNum
	  * <li>size (in bytes)
	  * </ul>
		* @param configPath a path to a cloudlet configuration in the config file
	  * @param num of cloudlets
	  * @return A java.util.list of cloudlets of specified length
	  */
	def createCloudlets(configPath: String, num: Int): util.List[Cloudlet] = {
		val length = config.getInt(configPath + ".length ")
		val peNum = config.getInt(configPath + ".peNum")
		val size = config.getInt(configPath + ".size")
		
		val utilizationModel = new UtilizationModelDynamic(0.25)
		val cloudlets = new ListBuffer[Cloudlet]
		(1 to num).foreach(_ => {

			cloudlets.append(new CloudletSimple(length, peNum, utilizationModel).setSizes(size))
		})
		
		cloudlets.asJava
	}

	/**
		* Does the same as above, but for multiple types of cloudlets.
		* NOTE the configuration must have the following fields:
		* <ul>
		* <li>length (in million of instructions)
		* <li>peNum
		* <li>size (in bytes)
		* </ul>
		* @param configPath a path to multiple cloudlet configurations in the config file
		* @return A java.util.list of cloudlets of specified length
		*/
	def createMultipleCloudlets(configPath: String): util.List[Cloudlet] = {
		val cloudletConfigList = config.getObjectList(configPath)
		val cloudletlist = new ListBuffer[Cloudlet]
		val utilizationModel = new UtilizationModelDynamic(0.25)
		cloudletConfigList.forEach(
			cloudlet => {
				val length = cloudlet.get("length").unwrapped().asInstanceOf[Int]
				val peNum = cloudlet.get("peNum").unwrapped().asInstanceOf[Int]
				val size = cloudlet.get("size").unwrapped().asInstanceOf[Int]
				val numCloudlets = cloudlet.get("numCloudlets").unwrapped().asInstanceOf[Int]
				cloudletlist ++=
					List.fill(numCloudlets)(new CloudletSimple(length, peNum, utilizationModel).setSizes(size))
			})
		cloudletlist.asJava
	}



	def createNetworkCloudlets(configPath: String, num: Int): util.List[NetworkCloudlet] = {
		val length = config.getInt(configPath + ".length ")
		val peNum = config.getInt(configPath + ".peNum")
		val size = config.getInt(configPath + ".size")

		val utilizationModel = new UtilizationModelDynamic(0.25)
		val cloudlets = new ListBuffer[NetworkCloudlet]
		(1 to num).foreach(_ => {

			val ncl = new NetworkCloudlet(length, peNum)
			ncl.setSizes(size)
			val nctask = new CloudletExecutionTask(1,(length*0.7).asInstanceOf[Long])
			ncl.addTask(nctask)
			cloudlets.append(ncl)
		})

		cloudlets.asJava
	}
}
