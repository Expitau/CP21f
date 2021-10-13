import java.util.Queue;

public abstract class Worker {
    Queue<Work> workQueue;

    public abstract void run();

    Worker(Queue<Work> workQueue) {
        this.workQueue = workQueue;
        // TODO: problem1
    }

    void report(String msg){
        // TODO: problem2
    }

}

class Producer extends Worker {
    Producer(Queue<Work> workQueue) {
        super(workQueue);
    }

    @Override
    public void run() {
        // TODO: problem2
    }
}

class Consumer extends Worker {
    Consumer(Queue<Work> workQueue) {
        super(workQueue);
    }

    @Override
    public void run() {
        // TODO: problem2
    }
}
