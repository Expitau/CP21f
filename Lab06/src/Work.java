public class Work {
    private static int numWorks = 0;
    private final int id;

    public Work() {
        id = numWorks++;
    }

    @Override
    public String toString() {
        return "work" + id;
    }

}
