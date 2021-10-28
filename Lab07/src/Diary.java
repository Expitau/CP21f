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

    public void listEntries(){
        listEntries(false, false);
    }

    public void listEntries(boolean titleOrder, boolean lengthOrder) {
        List<DiaryEntry> tempList = new LinkedList<DiaryEntry>(this.diaryEntries);
        if(titleOrder){
            if(lengthOrder){
                Collections.sort(tempList, new titleLengthSort());
                DiaryUI.print("List entries sorted by title and length.");
            }else{
                Collections.sort(tempList, new titleSort());
                DiaryUI.print("List entries sorted by title.");
            }
        }
        
        Iterator<DiaryEntry> iterator = tempList.iterator();
        while (iterator.hasNext()) {
            DiaryEntry entry = iterator.next();
            if(lengthOrder) DiaryUI.print(entry.getShortString() + ", length: "+entry.getContent().split("\\s").length);
            else DiaryUI.print(entry.getShortString());
        }
        // Practice 2 - Your list should contain previously stored files
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

class titleSort implements Comparator<DiaryEntry>{
    @Override
    public int compare(DiaryEntry entry1, DiaryEntry entry2){
        return entry1.getTitle().compareTo(entry2.getTitle());
    }
}

class titleLengthSort implements Comparator<DiaryEntry>{
    @Override
    public int compare(DiaryEntry entry1, DiaryEntry entry2){
        if(!entry1.getTitle().equals(entry2.getTitle())){
            return entry1.getTitle().compareTo(entry2.getTitle());
        }

        int length1 = entry1.getContent().split("\\s").length;
        int length2 = entry2.getContent().split("\\s").length;
        if(length1 == length2) return 0;
        return length1 < length2 ? 1 : -1;
    }
}