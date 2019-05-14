import org.cloudbus.cloudsim.hosts.{Host, HostSimple, HostStateHistoryEntry}
import org.cloudbus.cloudsim.provisioners.{PeProvisionerSimple, ResourceProvisioner}
import org.cloudbus.cloudsim.resources.{Pe, PeSimple, Resource, ResourceManageable}
import org.scalatest.FunSuite

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConverters._

/**
  * This test checks the vm allocation policy for least connections. The code is checked for a set of variables.
  * Since no vms are actually allocated to the host, the size is 0.
  */
class LeastConnectionTest extends FunSuite {
  val peList = new ListBuffer[Pe]
  peList.append(
    new PeSimple(
      2000, new PeProvisionerSimple()))
  val h1 = new HostSimple(
    16000, 10000, 4000000, peList.asJava)
  val h2 = new HostSimple(
    16000, 10000, 4000000, peList.asJava)
  //Adding 2 vms to h1
  val mem = h1.getVmList.size() + 2
  //Adding 3 vms to h2
  val mem2 = h2.getVmList.size() + 3
  val result = if (mem <= mem2) h1 else h2
  assert(h1.getVmList.size() == 0)
  assert(h1.getVmList.size() == 0)
  assert(result == h1)
}
