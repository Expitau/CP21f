public class CharacterCounter {
  public static void countCharacter(String str) {
    // DO NOT change the skeleton code.
    // You can add codes anywhere you want.
    int[] upper_cnt = new int[26];
    int[] lower_cnt = new int[26];
    for(char it : str.toCharArray()){
      if('A' <= it && it <= 'Z') upper_cnt[it-'A']++;
      else lower_cnt[it-'a']++;
    }
    for(int i=0; i<26; i++){
      String res = "";
      if(upper_cnt[i] > 0){
        res += String.format("%c: %d times", 'A'+i, upper_cnt[i]);
        if(lower_cnt[i] > 0) res+=", ";
      }
      if(lower_cnt[i] > 0){
        res += String.format("%c: %d times", 'a'+i, lower_cnt[i]);
      }
      if(!res.isEmpty()) System.out.println(res);
    }
  }

  private static void printCount(char character, int count) {
    System.out.printf("%c: %d times\n", character, count);
  }
}
