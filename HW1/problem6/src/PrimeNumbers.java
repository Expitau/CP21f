public class PrimeNumbers {
  public static void printPrimeNumbers(int m, int n) {
    // DO NOT change the skeleton code.
    // You can add codes anywhere you want.
    for(int i=m; i<=n; i++){
      if(isPrime(i)) System.out.printf("%d ", i);
    }
  }

  private static boolean isPrime(int n){
    for(int i=2; i*i<=n; i++){
      if(n%i == 0) return false;
    }
    return true;
  }
}
