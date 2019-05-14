package factory

import java.util

import Predicates.AutoVmPredicate
import Suppliers.AutoVmSupplier
import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.schedulers.cloudlet.CloudletSchedulerSpaceShared
import org.cloudbus.cloudsim.vms.network.NetworkVm
import org.cloudbus.cloudsim.vms.{Vm, VmSimple}
import org.cloudsimplus.autoscaling._

import collection.JavaConverters._
import scala.collection.mutable.ListBuffer

// This Vm factory allocates new vm with space shared policy instead of default timeshared.
// Strongly prefer this one over the timeshared policy!!!
object VmSpaceFactory {
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
        (0 to num).foreach(_ => {
            val vm = new VmSimple(mips, numPe).setRam(ram).setBw(bw).setSize(storage).setCloudletScheduler(new CloudletSchedulerSpaceShared())
            vm.getUtilizationHistory.enable()
            vms.append(vm)
        })
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
    def createComplexVmList(configPath: String) = {
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
                val newVm = new VmSimple(mips, numPe).setRam(ram).setBw(bw).setSize(storage)
                newVm.setCloudletScheduler( new CloudletSchedulerSpaceShared())
                newVm.getUtilizationHistory.enable()
                vmlist ++=
                        List.fill(numVm)(newVm)
            })
        vmlist.asJava
    }

    // create NetworkVm for networksimulation
    def createNetWorkVmList(configPath: String, num: Int): util.List[NetworkVm] = {
        val ram = config.getInt(configPath + ".ram")
        val bw = config.getInt(configPath + ".bw")
        val mips = config.getInt(configPath + ".mips")
        val storage = config.getInt(configPath + ".storage")
        val numPe = config.getInt(configPath + ".numPe")

        val vms: ListBuffer[NetworkVm] = new ListBuffer[NetworkVm]
        (0 to num).foreach(_ => {

            val newVm:NetworkVm = new NetworkVm(mips, numPe)
            newVm.setRam(ram).setBw(bw).setSize(storage)
            newVm.setCloudletScheduler( new CloudletSchedulerSpaceShared())
            vms.append( newVm )
        })
        vms.asJava
    }


    def createScalableVmList(configPath: String, num: Int): util.List[Vm] = {
        val ram = config.getInt(configPath + ".ram")
        val bw = config.getInt(configPath + ".bw")
        val mips = config.getInt(configPath + ".mips")
        val storage = config.getInt(configPath + ".storage")
        val numPe = config.getInt(configPath + ".numPe")

        val vms = new ListBuffer[Vm]
        (0 to num).foreach(_ => {
            val newVm = new VmSimple(mips, numPe).setRam(ram).setBw(bw).setSize(storage).setCloudletScheduler(new CloudletSchedulerSpaceShared())
            addHorizontalScaling(newVm)
            vms.append(newVm)
        })
        vms.asJava
    }




    def addHorizontalScaling(vm:Vm):Unit = {


        val horizontalScaling = new HorizontalVmScalingSimple()

        val autoVmSupplier = new AutoVmSupplier
        val autoVmPredicate = new AutoVmPredicate

        horizontalScaling.setVmSupplier(autoVmSupplier).setOverloadPredicate(autoVmPredicate)
        vm.setHorizontalScaling(horizontalScaling)
    }


}
