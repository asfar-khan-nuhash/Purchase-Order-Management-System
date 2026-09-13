package POM;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class mainMenu {
    Scanner scanner = new Scanner(System.in);
    File smFile = new File("smUsers.txt");
    File pmFile = new File("pmUsers.txt");
    User user = new User();
    
    public void smLogin(Scanner scanner){
        scanner.nextLine();
        
        System.out.print("Enter username: ");
        user.setUsername(scanner.nextLine());
        System.out.print("Enter password: ");
        user.setPassword(scanner.nextLine());
        
        if (validateSMCredentials(user.getUsername(),user.getPassword())) {
            System.out.println("Login successful!\n");
            
            System.out.println("\nLogged in as Sales Manager");
            User.salesManager sm = user.new salesManager();
            try (BufferedReader reader = new BufferedReader(new FileReader(smFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] section = line.split("\\|");
                    if (section.length == 4 && section[0].equals(user.getUsername()) && section[1].equals(user.getPassword())) {
                        sm.setFirstname(section[2]);
                        sm.setLastname(section[3]);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error checking user credentials");
            }
            sm.smMenu(scanner, sm);
        } else {
            System.out.println("\nInvalid login credentials\n");
        }
    }
    
    public void pmLogin(Scanner scanner){
        scanner.nextLine();
        
        System.out.print("Enter username: ");
        user.setUsername(scanner.nextLine());
        System.out.print("Enter password: ");
        user.setPassword(scanner.nextLine());
        
        if (validatePMCredentials(user.getUsername(),user.getPassword())) {
            System.out.println("Login successful!");
            
            System.out.println("\nLogged in as Purchase Manager");
            User.purchaseManager pm = user.new purchaseManager();
            try (BufferedReader reader = new BufferedReader(new FileReader(pmFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] section = line.split("\\|");
                    if (section.length == 4 && section[0].equals(user.getUsername()) && section[1].equals(user.getPassword())) {
                        pm.setFirstname(section[2]);
                        pm.setLastname(section[3]);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error checking user credentials");
            }
            
            pm.pmMenu(scanner, pm);
        } else {
            System.out.println("\nInvalid login credentials\n");
        }

    }
    
    public void adLogin(Scanner scanner){
        scanner.nextLine();
        
        System.out.print("Enter username: ");
        user.setUsername(scanner.nextLine());
        System.out.print("Enter password: ");
        user.setPassword(scanner.nextLine());
        
        if(user.getUsername().equals("admin") && user.getPassword().equals("admin1")){
            System.out.println("\nLogged in as Admin");
            Admin admin = new Admin();
            admin.setName("Admin");
            admin.adMenu(scanner, admin);
        } else {
            System.out.println("\nInvalid login credentials\n");
        }
        
    }
    
    private boolean validateSMCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(smFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking user credentials");
        }
        return false;
    }
    
    private boolean validatePMCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pmFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4 && parts[0].equals(username) && parts[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error checking user credentials");
        }
        return false;
    }
    
    public void mainMenu(){
        System.out.println("\nWelcome to Sigma SDN BHD PURCHASE ORDER MANAGEMENT SYSTEM");
        
        while (true){
            System.out.println("\nMAIN MENU");
            System.out.println("1: Login as Sales Manager");
            System.out.println("2: Login as Purchase Manager");
            System.out.println("3: Login as Admin");
            System.out.println("0: Exit");
            
            System.out.print("Enter your choice: ");
            int option = scanner.nextInt();
            switch (option){
                case 1:
                    smLogin(scanner);
                    break;
                case 2:
                    pmLogin(scanner);
                    break;
                case 3:
                    adLogin(scanner);
                    break;
                case 0:
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Try again...");
                    break;
            }
        }
    } 
    
}
