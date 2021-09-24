public class DrawingFigure {
  public static void drawFigure(int n) {
    // DO NOT change the skeleton code.
    // You can add codes anywhere you want.
    for(int i=0; i<n; i++) drawLine(i, n, true);
    drawLine(n, n, true);
    for(int i=n-1; i>=0; i--) drawLine(i, n, i != 0);
  }
  private static void drawLine(int m, int n, boolean newline){
    String res = "";
    for(int i = 0; i<n-m; i++) res += "  ";
    for(int i = 0; i<2*m+1; i++) res += "* ";
    for(int i = 0; i<n-m; i++) res += "  ";
    res = res.substring(0, res.length()-1);
    if(newline) System.out.println(res);
    else System.out.print(res);
  }
}
