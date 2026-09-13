
package POM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Item {
    private String id, name, description, supplier;
    private int stock;
    private double price;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    
    
    public void itemMenu(Scanner scanner){
        while (true){    
            System.out.println("\n");
            System.out.println("1. Add Item");
            System.out.println("2. Delete Item");
            System.out.println("3. Edit Items");
            System.out.println("0. Go Back");

            System.out.print("Enter your option: ");
            int option = scanner.nextInt();
        
            switch(option){
                case 1:
                    createItem(scanner);
                    break;
                case 2:
                    delItem(scanner);
                    break;
                case 3:
                    editItem(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
            
    }
    
    public void createItem(Scanner scanner){
        scanner.nextLine(); //Note: this line consumes the newline created by the nextInt() from the menu.
        
        System.out.print("\nAssign an ID: ");
        this.id = scanner.nextLine();
        
        if(validateItemExistence(this.id)){
            System.out.println("This ID already exists\n");
            return;
        }
        
        System.out.print("\nAssign item name: ");
        this.name = scanner.nextLine();
        
        System.out.print("\nAssign item description (maximum 20 characters): ");
        this.description = scanner.nextLine();
        while(this.description.length() > 20){
            System.out.println("The length of the description is too long.");
            System.out.print("\nAssign item description (maximum 20 characters): ");
            this.description = scanner.nextLine();
        }
            
        System.out.print("\nAssign item supplier: ");
        this.supplier = scanner.nextLine();
        Supplier checkSupplier = new Supplier();
        while(!checkSupplier.checkSupplierID(this.supplier)){
            System.out.println("This supplier ID does not exist.\n");
            System.out.print("Assign existing supplier ID: ");
            this.supplier = scanner.nextLine();
        }
        
        System.out.print("\nAssign item stock: ");
        this.stock = scanner.nextInt();
        System.out.print("\nAssign item price: ");
        this.price = scanner.nextFloat();

        String stringStock = String.valueOf(this.stock);
        String stringPrice = String.valueOf(this.price);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("item.txt", true))){
            writer.write(this.id + "|" + this.name + "|" +this.description+ "|" +this.supplier+ "|" 
                    + stringStock + "|" + stringPrice + "\n");
        } catch (IOException e) {
            System.out.println("\nError in editing file.");
        }
    }
    
    public boolean validateItemExistence(String ID){
        try(BufferedReader reader = new BufferedReader(new FileReader("item.txt"))){
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

    public void delItem(Scanner scanner){
        
        scanner.nextLine(); //Note: this line consumes the newline created by the nextInt() from the menu.
        
        System.out.print("\nEnter item ID to delete: ");
        String searchID = scanner.nextLine();

        File tempFile = new File("temp_item.txt"); //Note: This is how you create new files

        try (BufferedReader reader = new BufferedReader(new FileReader("item.txt"));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                
                String[] section = line.split("\\|");
                if (section[0].equals(searchID)) {
                    //Line skipped.
                    System.out.println("The item has been deleted successfully!");

                } else {
                    writer.write(line);
                    writer.newLine();  
                }                
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        try (BufferedReader tempReader = new BufferedReader(new FileReader(tempFile));
             BufferedWriter originalWriter = new BufferedWriter(new FileWriter("item.txt"))) {

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
    
    public void editItem(Scanner scanner){
        
        scanner.nextLine(); //Note: this line consumes the newline created by the nextInt() from the menu.
        
        System.out.print("\nEnter item ID to edit: ");
        String searchID = scanner.nextLine();

        File tempFile = new File("temp_item.txt"); //Note: This is how you create new files

        try (BufferedReader reader = new BufferedReader(new FileReader("item.txt"));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;

            while ((line = reader.readLine()) != null) {
                
                String[] section = line.split("\\|");
                if (section[0].equals(searchID)) {
                    //Replace match found
        
                    System.out.print("\nAssign an ID: ");
                    this.id = scanner.nextLine();

                    System.out.print("\nAssign item name: ");
                    this.name = scanner.nextLine();

                    System.out.print("\nAssign item description (maximum 20 characters): ");
                    this.description = scanner.nextLine();
                    while(this.description.length() > 20){
                        System.out.println("The length of the description is too long.");
                        System.out.print("\nAssign item description (maximum 20 characters): ");
                        this.description = scanner.nextLine();
                    }

                    System.out.print("\nAssign item supplier: ");
                    this.supplier = scanner.nextLine();
                    Supplier checkSupplier = new Supplier();
                    while(!checkSupplier.checkSupplierID(this.supplier)){
                        System.out.println("This supplier ID does not exist.\n");
                        System.out.print("Assign existing supplier ID: ");
                        this.supplier = scanner.nextLine();
                    }

                    System.out.print("\nAssign item stock: ");
                    this.stock = scanner.nextInt();
                    System.out.print("\nAssign item price: ");
                    this.price = scanner.nextFloat();

                    String stringStock = String.valueOf(this.stock);
                    String stringPrice = String.valueOf(this.price);
                    
                    writer.write(this.id + "|" + this.name + "|" +this.description+ "|" +this.supplier+ "|" 
                    + stringStock + "|" + stringPrice + "\n");
                    
                    System.out.println("The item has been edited successfully!");

                } else {
                    writer.write(line);
                    writer.newLine();  
                }                
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

        try (BufferedReader tempReader = new BufferedReader(new FileReader(tempFile));
             BufferedWriter originalWriter = new BufferedWriter(new FileWriter("item.txt"))) {

            String tempLine;
            while ((tempLine = tempReader.readLine()) != null) {
                originalWriter.write(tempLine);
                originalWriter.newLine(); 
            }

        } catch (IOException e) {
            System.err.println("Error copying content to the original file: " + e.getMessage());
        }
        
        if (!tempFile.delete()) {
            System.err.println("Error deleting the temporary file");
        }
    }
    
    public void viewItems(){
        System.out.print("\n");
        try(BufferedReader reader = new BufferedReader(new FileReader("item.txt"))){
            String line;
            System.out.printf("%-15s%-20s%-30s%-20s%-15s%-15s\n", "Item ID", "Item Name", "Item Description","Item Supplier ID", 
                    "Item Stock","Item Price");
            while ((line = reader.readLine()) != null){
                String[] section = line.split("\\|");
                
                System.out.printf("%-15s%-20s%-30s%-20s%-15s%-15s\n",section[0], section[1], section[2], section[3], section[4], section[5]);
            }
        } catch (IOException e){
            System.out.println("Item file missing.");
        }
    }
}


