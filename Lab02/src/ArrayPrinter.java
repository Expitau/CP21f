import java.util.Arrays;
import java.util.Scanner;

public class ArrayPrinter{
  public static void main(String[] args){
    Scanner scanner = new Scanner(System.in);

    int sz = scanner.nextInt();

    String[] arr = new String[sz];

    for(int i=0; i<sz; i++){
      arr[i] = scanner.next();
    }

    for(String str : arr){
      System.out.print(str + " ");
    }
    System.out.println();

    for(int i=sz-1; i>=0; i--){
      System.out.print(arr[i] + " ");
    }
    System.out.println();
  }
}
