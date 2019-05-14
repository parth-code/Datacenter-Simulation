package LoadbalancingBorkers
import org.cloudbus.cloudsim.brokers.{DatacenterBroker, DatacenterBrokerSimple}
import org.cloudbus.cloudsim.cloudlets.Cloudlet
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.vms.Vm
import org.cloudsimplus._
import java.util.Random
import org.slf4j.LoggerFactory
class RandomSimBroker(val simulation: CloudSim) extends DatacenterBrokerSimple(simulation)
{
    val mylogger = LoggerFactory.getLogger("Random Broker:")
    //Override the default cloudlet to vm mapper to random which acts as a load balance
    override def defaultVmMapper(cloudlet: Cloudlet): Vm = {

        if (cloudlet.isBindToVm) {
            val vm = cloudlet.getVm
            if (this == vm.getBroker && vm.isCreated) return vm
            return Vm.NULL
        }

        // using java.util.random  for some reason scala random lib is behaving strangely
        val rand = new Random()
        val index = rand.nextInt(getVmCreatedList.size())


        getVmFromCreatedList(index)

    }



}
