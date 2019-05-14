import com.typesafe.config.ConfigFactory
import org.cloudbus.cloudsim.allocationpolicies.VmAllocationPolicySimple
import org.cloudbus.cloudsim.core.CloudSim
import org.scalatest.FunSuite
import factory.DatacenterFactory
import org.cloudbus.cloudsim.hosts.Host

import collection.JavaConverters._


class DatacenterFactoryTest extends FunSuite {
	
	val config = ConfigFactory.load()
	
	test("createNormalDatacenter") {
		val cloudsim = new CloudSim()
		val vmAllocationPolicy = new VmAllocationPolicySimple()
		
		val hostCount = config.getInt("test.datacenterFactoryTest.hostCount")
		val peCount = config.getInt("test.datacenterFactoryTest.host.peCount")
		val mips = config.getInt("test.datacenterFactoryTest.host.MIPS")
		val ram = config.getInt("test.datacenterFactoryTest.host.RAM")
		val storage = config.getInt("test.datacenterFactoryTest.host.storage")
		val bw = config.getInt("test.datacenterFactoryTest.host.bandwidth")
		
		val datacenter = DatacenterFactory.createNormalDatacenter("test.datacenterFactoryTest", cloudsim, vmAllocationPolicy)
		assert(datacenter.getHostList.size() == hostCount)
		
		val hostList: java.util.List[Host] = datacenter.getHostList()
		hostList.forEach(h => {
			assert(h.getBw.getAvailableResource == bw)
			assert(h.getRam.getAvailableResource == ram)
			assert(h.getStorage.getAvailableResource == storage)
			assert(h.getMips == mips)
			assert(h.getPeList.size() == peCount)
		})
	}
}
