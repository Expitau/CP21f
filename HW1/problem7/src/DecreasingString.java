public class DecreasingString {
  public static void printLongestDecreasingSubstringLength(String inputString) {
    // DO NOT change the skeleton code.
    // You can add codes anywhere you want.
    int ans = 1, res = 1, len = inputString.length();
    for(int i=1; i<len; i++){
      if(inputString.charAt(i-1) > inputString.charAt(i)) res++;
      else res = 1;
      if(ans < res) ans = res;
    }
    System.out.println(ans);
  }
}