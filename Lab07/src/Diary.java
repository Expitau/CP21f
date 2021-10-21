import java.util.*;

public class Diary {
    private List<DiaryEntry> diaryEntries = new LinkedList<>();
    private Map<Integer, Set<String>> searchMap = new HashMap<>();

    public Diary() {

    }

    public void createEntry() {
        String title = DiaryUI.input("title : ");
        String content = DiaryUI.input("content : ");

        DiaryEntry entry = new DiaryEntry(title, content);
        diaryEntries.add(entry);

        // Add the entry to the HashMap

        Set<String> words = new HashSet<>();
        for (String str : title.split("\\s")) {
            words.add(str);
        }
        for (String str : content.split("\\s")) {
            words.add(str);
        }

        searchMap.put(entry.getID(), words);

        DiaryUI.print("The entry is saved.");
        // Practice 2 - Store the created entry in a file
    }

    public void listEntries(boolean titleOrder) {
        if(titleOrder) sortEntriesTitleOrder();
        else sortEntriesTimeOrder();
        
        Iterator<DiaryEntry> iterator = diaryEntries.iterator();
        while (iterator.hasNext()) {
            DiaryEntry entry = iterator.next();
            DiaryUI.print(entry.getShortString());
        }
        // Practice 2 - Your list should contain previously stored files
    }

    private void sortEntriesTimeOrder(){
        Collections.sort(diaryEntries, (o1, o2) -> {
            return o1.getDate().compareTo(o2.getTitle());
        });
    }

    private void sortEntriesTitleOrder(){
        Collections.sort(diaryEntries, (o1, o2) -> {
            return o1.getTitle().compareTo(o2.getTitle());
        });
    }


    private DiaryEntry findEntry(int id) {
        for (DiaryEntry entry : diaryEntries) {
            if (entry.getID() == id) {
                return entry;
            }
        }
        return null;
    }

    public void readEntry(int id) {
        DiaryEntry entry = findEntry(id);

        if (entry == null) {
            DiaryUI.print("There is no entry with id " + id + ".");
            return;
        }

        DiaryUI.print(entry.getFullString());
        // Practice 2 - Your read should contain previously stored files
    }

    public void deleteEntry(int id) {
        DiaryEntry entry = findEntry(id);

        if (entry == null) {
            DiaryUI.print("There is no entry with id " + id + ".");
            return;
        }

        diaryEntries.remove(entry);
        searchMap.remove(entry.getID());

        DiaryUI.print("Entry " + id + " is removed.");
        // Practice 2 - Delete the file of the entry
    }

    public void searchEntry(String keyword) {
        List<DiaryEntry> searchResult = new LinkedList<>();
        for(Map.Entry<Integer, Set<String>> entry : searchMap.entrySet()){
            if(entry.getValue().contains(keyword)){
                searchResult.add(findEntry(entry.getKey()));
            }
        }
        if(searchResult.isEmpty()){
            DiaryUI.print("There is no entry that contains \""+keyword+"\".");
            return;
        }

        for(DiaryEntry entry : searchResult){
            DiaryUI.print(entry.getFullString()+"\n");
        }
        // Practice 2 - Your search should contain previously stored files
    }
}
