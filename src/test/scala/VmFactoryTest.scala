import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuite

import factory.VmFactory

class VmFactoryTest extends FunSuite {
	
	val config = ConfigFactory.load()
	
	test("createVmList test") {
		val num = config.getInt("test.vmFactoryTest.numVm")
		val ram = config.getInt("test.vmFactoryTest.vm.ram")
		val mips = config.getInt("test.vmFactoryTest.vm.mips")
		val numPe = config.getInt("test.vmFactoryTest.vm.numPe")
		val bw = config.getInt("test.vmFactoryTest.vm.bw")
		val storage = config.getInt("test.vmFactoryTest.vm.storage")
		val vms = VmFactory.createVmList("test.vmFactoryTest.vm", num)
		
		assert(vms.size == num)
		vms.forEach(v => {
			assert(v.getBw.getAvailableResource == bw)
			assert(v.getRam.getAvailableResource == ram)
			assert(v.getMips == mips)
			assert(v.getNumberOfPes == numPe)
			assert(v.getStorage.getAvailableResource == storage)
		})
	}
}

