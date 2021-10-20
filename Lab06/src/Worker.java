import java.util.Queue;

public abstract class Worker {
    private static int numWorkers = 0;

    Queue<Work> workQueue;
    private final int id;

    protected int sinceLastWork = 0;

    public abstract void run();

    Worker(Queue<Work> workQueue) {
        this.workQueue = workQueue;
        id = numWorkers++;
    }

    int getTime() {
        return sinceLastWork;
    }

    void incrementTime(){
        sinceLastWork++;
    }

    void resetTime(){
        sinceLastWork = 0;
    }

    void report(String msg) {
        System.out.printf("%s %s\n", this, msg);
    }

    @Override
    public String toString() {
        return "worker" + id;
    }

}

class Producer extends Worker {
    Producer(Queue<Work> workQueue) {
        super(workQueue);
    }

    @Override
    public void run() {
        if (workQueue.size() < 20) {
            Work work = new Work();
            workQueue.add(work);
            report("produce " + work);
        } else {
            report("failed to produce work");
        }
    }
}

class Consumer extends Worker {
    Consumer(Queue<Work> workQueue) {
        super(workQueue);
    }

    @Override
    public void run() {
        if(2 * Math.random() < 1){
            Work work = workQueue.poll();
            if(work != null){
                report("consumed " + work);
            }else{
                report("failed to consume work");
            }
        }else{
            report("failed to consume work");
        }
    }
}
