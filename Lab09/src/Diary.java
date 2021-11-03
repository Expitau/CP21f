import java.util.*;

public class Diary {
    private List<DiaryEntry> diaryEntries = new LinkedList<>();
    private Map<Integer, Set<String>> searchMap = new HashMap<>();

    private static String DATA_PATH = "data/";

    public Diary() throws NoDataDirectoryException {
        // TODO
        // Practice 2 - Load diary entries
        loadEntries();
    }

    private void loadEntries() throws NoDataDirectoryException {
        List<List<String>> fileList = StorageManager.directoryChildrenLines(DATA_PATH);
        for (List<String> fileContent : fileList) {

            DiaryEntry entry = getEntry(fileContent);
            if (entry != null) {
                DiaryEntry.updateIncrementId(entry.getID());
                diaryEntries.add(entry);
                addSearchMap(entry);
            }
        }

    }

    private DiaryEntry getEntry(List<String> fileContent) {
        if (fileContent.size() < 4)
            return null;
        Iterator<String> iter = fileContent.iterator();
        int id = Integer.parseInt(fileContent.get(0));
        String createdTime = fileContent.get(1);
        String title = fileContent.get(2);
        String content = fileContent.get(3);
        return new DiaryEntry(id, title, content, createdTime);
    }

    public void createEntry() throws NoDataDirectoryException {
        // TODO
        // Practice 1 - Create a diary entry
        String title = DiaryUI.input("title : ");
        String content = DiaryUI.input("content : ");

        DiaryEntry entry = new DiaryEntry(title, content);

        diaryEntries.add(entry);
        addSearchMap(entry);

        // TODO
        // Practice 2 - Store the created entry in a file
        saveEntry(entry);

        DiaryUI.print("The entry is saved.");
    }

    private void saveEntry(DiaryEntry entry) throws NoDataDirectoryException {
        String filePath = DATA_PATH + entry.getFileName();
        List<String> fileData = entry.getFileData();
        StorageManager.writeLines(filePath, fileData);
    }

    private void addSearchMap(DiaryEntry entry) {
        Set<String> keywords = new HashSet<>();
        for (String keyword : entry.getTitle().split("\\s"))
            keywords.add(keyword);

        for (String keyword : entry.getContent().split("\\s"))
            keywords.add(keyword);

        searchMap.put(entry.getID(), keywords);
    }

    public void listEntries() {
        // TODO
        // Practice 1 - List all the entries - sorted in created time by ascending order
        Collections.sort(diaryEntries, new IDSort());
        ListIterator<DiaryEntry> iterator = diaryEntries.listIterator();
        while (iterator.hasNext()) {
            DiaryEntry currentDiaryEntry = iterator.next();
            DiaryUI.print(currentDiaryEntry.getShortString());
        }
    }

    public void listEntries(String condition1) {
        Collections.sort(diaryEntries, new titleSort());
        DiaryUI.print("List entries sorted by " + condition1 + ".");
        ListIterator<DiaryEntry> interator = diaryEntries.listIterator();
        while (interator.hasNext())
            DiaryUI.print(interator.next().getShortString());
    }

    public void listEntries(String condition1, String condition2) {
        Collections.sort(diaryEntries, new lengthSort());
        Collections.sort(diaryEntries, new titleSort());

        DiaryUI.print("List entries sorted by " + condition1 + " and " + condition2 + ".");
        ListIterator<DiaryEntry> iterator = diaryEntries.listIterator();
        while (iterator.hasNext()) {
            DiaryEntry currentEntry = iterator.next();
            DiaryUI.print(currentEntry.getShortString() + ", length: " + currentEntry.getContent().split("\\s").length);
        }
    }

    private DiaryEntry findEntry(int id) {
        for (DiaryEntry entry : diaryEntries)
            if (entry.getID() == id)
                return entry;

        return null;
    }

    public void readEntry(int id) {
        // TODO
        // Practice 1 - Read the entry of given id
        DiaryEntry entry = findEntry(id);

        if (entry == null) {
            DiaryUI.print("There is no entry id " + id + ".");
            return;
        }
        DiaryUI.print(entry.getFullString());
    }

    public void deleteEntry(int id) {
        // TODO
        // Practice 1 - Delete the entry of given id
        DiaryEntry entry = findEntry(id);

        if (entry == null) {
            DiaryUI.print("There is no entry id " + id + ".");
            return;
        }

        // TODO
        // Practice 2 - Delete the file of the entry
        StorageManager.deleteFile(DATA_PATH + entry.getFileName());
        diaryEntries.remove(entry);
        searchMap.remove(entry.getID());

        DiaryUI.print("Entry " + id + " is removed.");
    }

    public void searchEntry(String keyword) {
        // TODO
        // Practice 1 - Search and print all the entries containing given keyword
        List<DiaryEntry> searchResult = new ArrayList<>();

        for (int id : searchMap.keySet())
            if (searchMap.get(id).contains(keyword))
                searchResult.add(findEntry(id));

        if (searchResult.isEmpty()) {
            DiaryUI.print("There is no entry that contains \"" + keyword + "\".");
            return;
        }

        for (DiaryEntry entry : searchResult)
            DiaryUI.print(entry.getFullString() + "\n");
    }
}

class IDSort implements Comparator<DiaryEntry> {
    @Override
    public int compare(DiaryEntry entry1, DiaryEntry entry2) {
        return Integer.valueOf(entry1.getID()).compareTo(Integer.valueOf(entry2.getID()));
    }
}

class titleSort implements Comparator<DiaryEntry> {
    @Override
    public int compare(DiaryEntry entry1, DiaryEntry entry2) {
        return entry1.getTitle().compareTo(entry2.getTitle());
    }
}

class lengthSort implements Comparator<DiaryEntry> {
    @Override
    public int compare(DiaryEntry entry1, DiaryEntry entry2) {
        int length1 = entry1.getContent().split("\\s").length;
        int length2 = entry2.getContent().split("\\s").length;

        if (length1 == length2)
            return 0;

        return length1 < length2 ? 1 : -1;
    }
}
