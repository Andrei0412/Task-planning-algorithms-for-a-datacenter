/* Implement this class. */

import java.util.Comparator;
import java.util.List;

public class MyDispatcher extends Dispatcher {
    // ROUND_ROBIN.
    private int lastAssignedHost;
    public MyDispatcher(SchedulingAlgorithm algorithm, List<Host> hosts) {
        super(algorithm, hosts);
        lastAssignedHost = -1;
    }

    @Override
    public synchronized void addTask(Task task) {
        if (this.algorithm.equals(SchedulingAlgorithm.ROUND_ROBIN)) {
            //Assign based on the main rule.
            hosts.get((lastAssignedHost + 1) % hosts.size()).addTask(task);
            lastAssignedHost++;
        } else if (this.algorithm.equals(SchedulingAlgorithm.SHORTEST_QUEUE)) {
            //Find the suitable host using stream and comparing using getQueueSize method.
            Host host = hosts.stream()
                            .min(Comparator.comparingLong(Host::getQueueSize)
                                    .thenComparing(Host::getId))
                                    .orElse(null);

            //Don t need null check since we will always find a suitable host.
            if (host == null) {
                System.out.println("Can't find a suitable host!\n");
            } else {
                host.addTask(task);
            }
        } else if (this.algorithm.equals(SchedulingAlgorithm.SIZE_INTERVAL_TASK_ASSIGNMENT)) {
            //We only need to check task's type in order to know where to send.
            if (task.getType().equals(TaskType.SHORT)) {
                hosts.get(0).addTask(task);
            } else if (task.getType().equals(TaskType.MEDIUM)) {
                hosts.get(1).addTask(task);
            } else if (task.getType().equals(TaskType.LONG)) {
                hosts.get(2).addTask(task);
            }
        } else if (this.algorithm.equals(SchedulingAlgorithm.LEAST_WORK_LEFT)) {
            //Find the suitable host using stream and comparing using getWorkLeft method.
            Host host = hosts.stream()
                    .min(Comparator.comparingLong(Host::getWorkLeft)
                            .thenComparing(Host::getId))
                    .orElse(null);

            //Don t need null check since we will always find a suitable host.
            if (host == null) {
                System.out.println("Can't find a suitable host!\n");
            } else {
                host.addTask(task);
            }
        }
    }
}
