public class FibonacciNumbers {
  public static void printFibonacciNumbers(int n) {
    // DO NOT change the skeleton code.
    // You can add codes anywhere you want.
    int sum = 0, flag = 0;
    int[] f = {0, 1};
    String res = "";
    for(int i=0; i<n; i++){
      res += String.format("%d ", f[i%2]);
      sum += f[i%2];
      if(sum >= 100000){
        flag = 1;
        sum %= 100000;
      }

      f[i%2] += f[1-i%2];
    }
    res = res.strip();
    System.out.println(res);
    if(flag == 1) System.out.printf("last five digits of sum = %05d", sum);
    else System.out.printf("sum = %d", sum);
  }
}
