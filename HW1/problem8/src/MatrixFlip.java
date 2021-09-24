public class MatrixFlip{
  public static void printFlippedMatrix(char[][] matrix){
    // DO NOT change the skeleton code.
    // You can add codes anywhere you want.
    int m = matrix.length, n = matrix[0].length;
    for(int i=0; i <m; i++){
      for(int j=0; j<n; j++){
        System.out.printf("%c", matrix[m-i-1][n-j-1]);
      }
      System.out.println("");
    }
  }
}
