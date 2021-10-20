import java.util.List;

public class Scheduler<T extends Worker> {
    private static final int waitms = 400;
    private List<T> workers;

    public Scheduler(List<T> workers) {
        this.workers = workers;
    }

    T schedule() {
        return workers.get(0);
    }

    T schedule(int index) {
        if(0 <= index && index < workers.size()){
            return workers.get(index);
        }else{
            return workers.get(0);
        }
    }

    T scheduleRandom() {
        int index = (int) (Math.random() * workers.size());
        return workers.get(index);
    }

    T scheduleFair() {
        T lastWorker = workers.get(0);
        for (T worker : workers) {
            if (worker.getTime() > lastWorker.getTime()) {
                lastWorker = worker;
            }
        }
        incrementTime();
        lastWorker.resetTime();
        return lastWorker;
    }

    private void incrementTime(){
        for (T worker : workers){
            worker.incrementTime();
        }
    }

    static void delay() {
        try {
            Thread.sleep(waitms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
