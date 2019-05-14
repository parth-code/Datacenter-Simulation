package Predicates

import java.util.function.Predicate

import org.cloudbus.cloudsim.vms.Vm

//predicate to determine if a vm need to allocate another vm
class AutoVmPredicate extends Predicate[Vm]{

    override def test(t: Vm): Boolean = t.getCpuPercentUsage > 0.7

}
