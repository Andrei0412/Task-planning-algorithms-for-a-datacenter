/* Implement this class. */

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyHost extends Host {
    private final BlockingQueue<Task> queue;
    private volatile boolean shutdownRequested;
    private volatile Task currentTask;
    private long leftTime;
    private volatile boolean higherPriority;

    public MyHost() {
        queue = new PriorityBlockingQueue<>
                (10, Comparator
                        .comparingInt(Task::getPriority)
                        .reversed()
                        .thenComparing(Task::getStart));
        shutdownRequested = false;
        currentTask = null;
        leftTime = 0;
        higherPriority = false;
    }

    @Override
    public void run() {
        try {
            while (!shutdownRequested) {
                currentTask = queue.take();
                long startTime = System.currentTimeMillis();
                leftTime = currentTask.getLeft();

                while (leftTime > 0) {
                    //If a task with higher priority arrived, check if the current one is preemptive
                    //If true, save the progress for this one, add it back to the queue then proceed with the new one.
                    if (currentTask.isPreemptible() && higherPriority) {
                        currentTask.setLeft(leftTime);
                        queue.put(currentTask);
                        currentTask = null;
                        higherPriority = false;
                        break;
                    }

                    TimeUnit.MILLISECONDS.sleep(1);
                    long passedTime = (System.currentTimeMillis() - startTime);
                    leftTime = currentTask.getLeft() - passedTime;
                }

                if (currentTask != null) {
                    currentTask.finish();
                    currentTask = null;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void addTask(Task task) {
        try {
            queue.put(task);
            if (currentTask != null &&
                    task.getPriority() > currentTask.getPriority()) {
                higherPriority = true;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getQueueSize() {
        if (currentTask != null) {
            return queue.size() + 1;
        } else {
            return queue.size();
        }
    }

    @Override
    public long getWorkLeft() {
        long totalWorkLeft = 0;
        totalWorkLeft = leftTime;

        for (Task task : queue) {
            totalWorkLeft += task.getLeft();
        }

        return totalWorkLeft;
    }

    @Override
    public void shutdown() {
        shutdownRequested = true;
        interrupt();
    }
}
