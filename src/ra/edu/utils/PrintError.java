package ra.edu.utils;

public class PrintError{
    public static void println(Object obj){
        System.out.println("\u001B[31m" + String.valueOf(obj) + "\u001B[0m");
    }

    public static void println(){
        System.out.println();
    }

    public static void print(Object obj){
        System.out.print("\u001B[31m" + String.valueOf(obj) + "\u001B[0m");
    }
}
