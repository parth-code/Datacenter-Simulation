package factory

import java.util.{List, function}

import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudsimplus.builders.tables.TableBuilderAbstract
import org.slf4j.LoggerFactory
import simulations.RoundRobinRandomSimulation.logger

import collection.JavaConverters._


class PrintFactory(list: List[Cloudlet], datacentertype: String) extends TableBuilderAbstract[Cloudlet](list) {
	val config = ConfigFactory.load()
	val costMem = config.getDouble(datacentertype + "." + "cost.perMem")
	val costBw = config.getDouble(datacentertype + "." + "cost.perBW")
	val costSec = config.getDouble(datacentertype + "." + "cost.perSec")
	val costStorage = config.getDouble(datacentertype + "." + "cost.perStorage")
	//  print(costMem, costBw, costSec, costStorage)
	
	
	override def createTableColumns = {
		val ID = "ID"
		addColumnDataFunction(getTable.addColumn("Cloudlet", ID), get_id)
		addColumnDataFunction(getTable.addColumn("Status "), get_status)
		addColumnDataFunction(getTable.addColumn("DC", ID), get_datacenter)
		addColumnDataFunction(getTable.addColumn("Host", ID), get_host)
		addColumnDataFunction(getTable.addColumn("Host PEs ", "CPU cores"), get_hostpe)
		addColumnDataFunction(getTable.addColumn("VM", ID), get_vm)
		addColumnDataFunction(getTable.addColumn("VM PEs   ", "CPU cores"), get_vmpe)
		addColumnDataFunction(getTable.addColumn("CloudletLen", "MI"), get_cloudletlength)
		addColumnDataFunction(getTable.addColumn("CloudletPEs", "CPU cores"), get_cloudletpe)
		
		val start = getTable.addColumn("StartTime", "Seconds").setFormat("%.0f")
		addColumnDataFunction(start, get_start)
		
		val finish = getTable.addColumn("FinishTime", "Seconds").setFormat("%.0f")
		addColumnDataFunction(finish, get_finish)
		
		val total = getTable.addColumn("ExecTime", "Seconds").setFormat("%.0f")
		addColumnDataFunction(total, get_total)
		
		val cost = getTable.addColumn("Cost", "Dollars").setFormat("%.7f")
		addColumnDataFunction(cost, get_cost)
	}
	
	implicit def get_id: function.Function[Cloudlet, Object] = cloudlet => cloudlet.getId.asInstanceOf[Object]
	
	implicit def get_status: function.Function[Cloudlet, Object] =
		cloudlet =>
			cloudlet.getStatus.name
				.asInstanceOf[Object]
	
	implicit def get_datacenter: function.Function[Cloudlet, Object] =
		cloudlet =>
			cloudlet.getVm.getHost.getDatacenter.getId
				.asInstanceOf[Object]
	
	implicit def get_host: function.Function[Cloudlet, Object] =
		cloudlet =>
			cloudlet.getVm.getHost.getId
				.asInstanceOf[Object]
	
	//  def costCalculator(cloudlet:Object = {}
	implicit def get_hostpe: function.Function[Cloudlet, Object] =
		cloudlet =>
			cloudlet.getVm.getHost.getWorkingPesNumber
				.asInstanceOf[Object]
	
	implicit def get_vm: function.Function[Cloudlet, Object] =
		cloudlet =>
			cloudlet.getVm.getId
				.asInstanceOf[Object]
	
	implicit def get_vmpe: function.Function[Cloudlet, Object] =
		cloudlet =>
			cloudlet.getVm.getNumberOfPes
				.asInstanceOf[Object]
	
	implicit def get_cloudletlength: function.Function[Cloudlet, Object] =
		cloudlet =>
			cloudlet.getLength
				.asInstanceOf[Object]
	
	implicit def get_cloudletpe: function.Function[Cloudlet, Object] =
		cloudlet =>
			cloudlet.getNumberOfPes
				.asInstanceOf[Object]
	
	implicit def get_start: function.Function[Cloudlet, Object] =
		cloudlet =>
			cloudlet.getExecStartTime
				.asInstanceOf[Object]
	
	implicit def get_finish: function.Function[Cloudlet, Object] =
		cloudlet =>
			cloudlet.getFinishTime
				.asInstanceOf[Object]
	
	implicit def get_total: function.Function[Cloudlet, Object] =
		cloudlet =>
			Math.ceil(cloudlet.getActualCpuTime)
				.asInstanceOf[Object]
	
	implicit def get_cost: function.Function[Cloudlet, Object] =
		cloudlet =>
			costCalculator(cloudlet)
				.asInstanceOf[Object]
	
	def costCalculator(cloudlet: Cloudlet): Double = {
		cloudlet.getTotalCost
	}
}

object PrintFactory {
	private val logger = LoggerFactory.getLogger("PrintFactory")
	
	def printAverages(finishedList: java.util.List[Cloudlet], cloudsim: CloudSim): Unit = {
		val costs = finishedList.asScala.map(c => c.getTotalCost)
		val times = finishedList.asScala.map(c => c.getActualCpuTime)
		val sum = costs.sum
		
		println("Total Costs: " + sum)
		println("Average costs: " + (sum / costs.size))
		println("Total time of simulation: " + cloudsim.clock())
		println("Average time: " + (times.sum / times.size))
	}
}