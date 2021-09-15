import java.util.Scanner;

public class StudentIDValidator{

  private static boolean isProperLength(String id){
    return id.length() == 10;
  }

  private static boolean hasProperDivision(String id){
    return id.charAt(4) == '-';
  }

  private static boolean hasProperDigits(String id){
    boolean ret = true;
    for(int i=0; ret && i<10; i++){
      if(i == 4) continue;
      ret = ('0' <= id.charAt(i) &&  id.charAt(i) <= '9');
    }
    return ret;
  }

  private static void validateStudentID(String id){
    if(!isProperLength(id)){
      System.out.println("The input length should be 10.");
    }else if(!hasProperDivision(id)){
      System.out.println("Fifth character should be '-'.");
    }else if(!hasProperDigits(id)){
      System.out.println("Contains an invalid digit.");
    }else{
      System.out.println(id + " is a valid student ID.");
    }
  }

  public static void main(String[] args){
    Scanner scanner = new Scanner(System.in);
    String input = scanner.next();
    while(!input.equals("exit")){
      validateStudentID(input);
      input = scanner.next();
    }
  }
}
