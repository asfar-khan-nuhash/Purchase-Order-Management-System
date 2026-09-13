package POM;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class POM{
    private static final String FILE_PATH = "users.txt";
    
    public static void main(String[] args) {
        mainMenu main = new mainMenu();
        main.mainMenu();
    }

}
    
