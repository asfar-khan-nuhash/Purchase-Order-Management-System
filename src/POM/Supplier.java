package POM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class Supplier {
    private String id, name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
    public void supplierMenu(Scanner scanner){
        while(true){
            System.out.println("\nSupplier Menu\n");
            System.out.println("1. Add Supplier");
            System.out.println("2. Edit Supplier");
            System.out.println("3. Delete Supplier");
            System.out.println("0. GO BACK");
            
            int option = scanner.nextInt();
            switch(option){
                case 1:
                    createSupplier(scanner);
                    break;
                case 2:
                    editSupplier(scanner);
                    break;
                case 3:
                    delSupplier(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nIncorrect option.");
            }
        }
    }
    
    public void createSupplier(Scanner scanner){
        
        scanner.nextLine(); //Note: this line consumes the newline created by the nextInt() from the menu.
        
        System.out.print("\nAssign an ID: ");
        this.id = scanner.nextLine();
        
        if(checkSupplierID(this.id)){
            System.out.println("This ID already exists.\n");
            return;
        }
        
        System.out.print("\nEnter supplier name: ");
        this.name = scanner.nextLine();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("supplier.txt", true))){
            writer.write(this.id + "|" + this.name + "\n");
        } catch (IOException e) {
            System.out.println("\nError in editing file.");
        }
    }
    
    public boolean checkSupplierID(String ID){
        
        try(BufferedReader reader = new BufferedReader(new FileReader("supplier.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] section = line.split("\\|");
                if (section[0].equals(ID)){
                    return true;
                }
            }
            
        } catch (IOException e){
            System.out.println("An Error Occured in validating existing ID.\n");
        }
        return false;
        
    }   
    
    public void delSupplier(Scanner scanner){
        
        scanner.nextLine(); //Note: this line consumes the newline created by the nextInt() from the menu.
        
        System.out.print("\nEnter supplier ID to delete: ");
        String searchID = scanner.nextLine();

        File tempFile = new File("temp_supplier.txt"); //Note: This is how you create new files

        try (BufferedReader reader = new BufferedReader(new FileReader("supplier.txt"));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                
                String[] section = line.split("\\|");
                if (section[0].equals(searchID)) {
                    //Line skipped.
                    System.out.println("The supplier has been deleted successfully!");

                } else {
                    writer.write(line);
                    writer.newLine();  
                }                
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        try (BufferedReader tempReader = new BufferedReader(new FileReader(tempFile));
             BufferedWriter originalWriter = new BufferedWriter(new FileWriter("supplier.txt"))) {

            String tempLine;
            while ((tempLine = tempReader.readLine()) != null) {
                originalWriter.write(tempLine);
                originalWriter.newLine(); 
            }

        } catch (IOException e) {
            System.err.println("Error copying content to the original file: " + e.getMessage());
        }
        
        if (!tempFile.delete()) {
            System.err.println("Error deleting the temporary file");}
    
    }
    
    public void editSupplier(Scanner scanner) {
        
        scanner.nextLine();
        
        System.out.print("\nEnter supplier ID to edit: ");
        String searchID = scanner.nextLine();

        // Create a temporary file to write the modified content
        File tempFile = new File("temp_supplier.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader("supplier.txt"));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                
                String[] section = line.split("\\|");
                if (section[0].equals(searchID)) {
                    
                    System.out.print("Enter new ID: ");
                    this.id = scanner.nextLine();

                    System.out.print("Enter new name: ");
                    this.id = scanner.nextLine();

                    writer.write(this.id + "|" + this.id);
                    writer.newLine();  // Note: Different systems deal with newlines differently, so a newline is added separately
                    
                    System.out.println("The supplier has been edited successfully!");

                } else {
                    writer.write(line);
                    writer.newLine();  
                }                
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        try (BufferedReader tempReader = new BufferedReader(new FileReader(tempFile));
             BufferedWriter originalWriter = new BufferedWriter(new FileWriter("supplier.txt"))) {

            String tempLine;
            while ((tempLine = tempReader.readLine()) != null) {
                originalWriter.write(tempLine);
                originalWriter.newLine(); 
            }

        } catch (IOException e) {
            System.err.println("Error copying content to the original file: " + e.getMessage());
        }
        
        if (!tempFile.delete()) {
            System.err.println("Error deleting the temporary file");}
    }

    public void viewSuppliers(){
        System.out.print("\n");
        try(BufferedReader reader = new BufferedReader(new FileReader("supplier.txt"))){
            String line;
            System.out.printf("%-15s%-20s\n", "Supplier ID", "Supplier Name");
            while ((line = reader.readLine()) != null){
                String[] section = line.split("\\|");
                
                System.out.printf("%-15s%-20s\n",section[0], section[1]);
            }
        } catch (IOException e){
            System.out.println("Supplier file missing.");
        }
        
    }
    
    public String returnSupplierName(String ID){
        
        try(BufferedReader reader = new BufferedReader(new FileReader("supplier.txt"))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] section = line.split("\\|");
                if (section[0].equals(ID)){
                    this.name = section[1];
                }
            }
            
        } catch (IOException e){
            System.out.println("An Error Occured in validating existing ID.\n");
        }

        return this.name;
    }
    
}
