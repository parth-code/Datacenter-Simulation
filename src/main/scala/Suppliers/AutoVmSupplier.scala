package Suppliers

import java.util.function.Supplier

import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.slf4j.LoggerFactory

// Vm supplier for dynamic vm allocation
class AutoVmSupplier extends Supplier[Vm]{

    val logger = LoggerFactory.getLogger("AutoVmSupplier:")

    override def get(): Vm = {
        logger.debug("New VM Created!!!!!!!!!!!!!!!!!")
        val config = ConfigFactory.load()
        val configPath = "simulationConf.vm."

        val ram = config.getInt(configPath+"ram")
        val bw = config.getInt(configPath+"bw")
        val mips = config.getInt(configPath+"mips")
        val storage = config.getInt(configPath+"storage")
        val numPe = config.getInt(configPath+"numPe")
        val numVm = config.getInt(configPath+"numVm")
        val newVm = new VmSimple(mips,numPe).setRam(ram).setBw(bw).setSize(storage)
        newVm.setCloudletScheduler( new CloudletSchedulerSpaceShared())
        newVm
    }



}
