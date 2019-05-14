import org.scalatest.FunSuite

import scala.collection.mutable

class RoundRobinTest extends FunSuite{
  var previous = 0
  val vmlist = List[String]("vm1", "vm2")
  val hostlist = List[String]("host1", "host2")
  var hostvm = mutable.Map[String, String]()
  vmlist.foreach(vm => {
    val index = (previous) % hostlist.size
    val key:String = hostlist(index)
    hostvm += (key -> vm)
    previous += 1
  })
  assert(hostvm("host1") == "vm1")
  assert(hostvm("host2") == "vm2")
}
