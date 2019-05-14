import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuite
import factory.CloudletFactory

class CloudletFactoryTest extends FunSuite {

	val config = ConfigFactory.load()
	
	test("createCloudlets test"){
		val num = config.getInt("test.cloudletFactoryTest.numCloudlets")
		val length = config.getInt("test.cloudletFactoryTest.cloudlet.length")
		val peNum = config.getInt("test.cloudletFactoryTest.cloudlet.peNum")
		val size = config.getInt("test.cloudletFactoryTest.cloudlet.size")
		val cloudlets = CloudletFactory.createCloudlets("test.cloudletFactoryTest.cloudlet", num)
		
		assert(cloudlets.size() == num)
		cloudlets.forEach(cloudlet => {
			assert(cloudlet.getLength == length)
			assert(cloudlet.getNumberOfPes == peNum)
			assert(cloudlet.getFileSize == size)
		})
		
	}
}

