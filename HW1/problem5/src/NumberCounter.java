public class NumberCounter {
  public static void countNumbers(String str0, String str1, String str2) {
    // DO NOT change the skeleton code.
    // You can add codes anywhere you want.
    int mul = stoi(str0) * stoi(str1) * stoi(str2);
    int[] cnt = new int[10];
    int length = 0;
    if(mul == 0){
      length = 1;
      cnt[0] = 1;
    }
    while(mul > 0){
      cnt[mul % 10]++;
      length++;
      mul /= 10;
    }
    for(int i=0; i<10; i++){
      if(cnt[i] > 0) printNumberCount(i, cnt[i]);
    }
    System.out.printf("length: %d", length);
  }

  private static void printNumberCount(int number, int count) {
    System.out.printf("%d: %d times\n", number, count);
  }

  private static int stoi(String str){
    int res = 0;
    for(char it : str.toCharArray()){
      res = res * 10 + (it - '0');
    }
    return res;
  }

}