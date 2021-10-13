import java.util.List;

public class Scheduler<T> {
    private static final int waitms = 400;
    private List<T> workers;

    public Scheduler(List<T> workers) {
        this.workers = workers;
    }

    T schedule() {
        return workers.get(0);
    }

    T schedule(int index) {
        // TODO: problem3.1 - Add scheduling logic
        return null;
    }

    T scheduleRandom() {
        // TODO: problem3.1 - Add scheduling logic
        return null;
    }

    T scheduleFair() {
        // TODO: problem3.2 - Add scheduling logic
        return null;
    }

    static void delay() {
        try {
            Thread.sleep(waitms);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
