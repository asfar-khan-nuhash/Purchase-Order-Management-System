package POM;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

class User {
    private String username, password, firstname, lastname;
    private int Quantity;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }
    
    public void createPurchaseRequisition(Scanner scanner, User user){
        String line;
        Item itmReq = new Item();
        Supplier spReq = new Supplier();
        
        String reqNumber = createRequisitionID(); //Requisition ID
        int year, month, date;
        
        do {
            System.out.print("Enter the year: ");
            year = scanner.nextInt();
            
            System.out.print("Enter the month: ");
            month = scanner.nextInt();
            
            System.out.print("Enter the date: ");
            date = scanner.nextInt(); 
            
            if(!validateDate(year, month, date)){
                System.out.println("Incorrect Date.\n");
            }
            
        }while(!validateDate(year, month, date)); //Requisition date
        scanner.nextLine();
        
        do{
            System.out.print("Enter Supplier ID: ");
            spReq.setId(scanner.nextLine());
            if(!spReq.checkSupplierID(spReq.getId())){
                System.out.println("Supplier not found.\n");
            }
        }while(!spReq.checkSupplierID(spReq.getId()));
        
        
        File temp = new File("tempRequisition.txt");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter (temp))){
            writer.write(reqNumber + "|" + returnStringDate(year, month, date) + "|" + 
                    user.firstname + " " + user.lastname + "|" + spReq.returnSupplierName(spReq.getId()) + "\n");
            
        }catch(IOException e){
            System.out.println("Error writing into file.");
        }
        //Find out how to read lines with different formatting, because you're going to have to read from these files

        while(true){
            double totalprice;
            itmReq.viewItems();
            System.out.println("\nCreate Purchase Requisition Menu\n");
            System.out.println("Notice: Items have to be registered before being able to request the item's purchase.\n");
            System.out.println("1. Add items to requisition");
            System.out.println("0. Finish and save requisition");
            
            System.out.print("Select your option: ");
            int option = scanner.nextInt();
            switch (option){
                case 1:
                    boolean reqboolean = false;
                    scanner.nextLine();
                    do{
                        
                        System.out.print("Input item ID: ");
                        itmReq.setId(scanner.nextLine());
                        
                        if(!itmReq.validateItemExistence(itmReq.getId())){
                            System.out.println("Item does not exist.\n");
                        }
                    }while(!itmReq.validateItemExistence(itmReq.getId()));
                    
                    try(BufferedReader reader = new BufferedReader(new FileReader("item.txt"))){
                        
                        while ((line = reader.readLine()) != null){
                            String[] section = line.split("\\|");
                            if (section[0].equals(itmReq.getId()) && section[3].equals(spReq.getId())){
                                itmReq.setId(section[0]);
                                itmReq.setName(section[1]);
                                itmReq.setDescription(section[2]);
                                itmReq.setPrice(Double.parseDouble(section[5]));
                                
                                System.out.print("Input item quantity: ");
                                this.Quantity = scanner.nextInt();
                                scanner.nextLine();

                                totalprice = this.Quantity * itmReq.getPrice();

                                try(BufferedWriter writer = new BufferedWriter(new FileWriter (temp, true))){
                                    writer.write(itmReq.getId() + "|" + itmReq.getName() + "|" + 
                                            itmReq.getDescription() + "|" + itmReq.getPrice() + "|" + this.Quantity + "|" +
                                            totalprice + "\n");

                                }catch(IOException e){
                                    System.out.println("Error writing into file.");
                                }
                                
                                System.out.println("Item added to purchase requisition.");
                                break;
                            }
                            
                        }
                        if (!reqboolean){
                            System.out.println("Supplier does not supply this item.\n");
                        }
                    }catch(IOException e){
                        System.out.println("Error in reading from file.");
                    }

                    break;
                case 0:
                    scanner.nextLine();
                    System.out.print("Enter instructions to implement: ");
                    String instruction = scanner.nextLine();
                    try (BufferedReader tempReader = new BufferedReader(new FileReader(temp));
                            BufferedWriter tempwriter = new BufferedWriter(new FileWriter("purchaseRequisition.txt", true))) {

                        String tempLine;
                        while ((tempLine = tempReader.readLine()) != null) {
                           tempwriter.write(tempLine);
                           tempwriter.newLine(); 
                        }
                        tempwriter.write("Instruction" + "|" + instruction);
                        tempwriter.newLine();

                    } catch (IOException e) {
                       System.err.println("Error copying content to the original file: " + e.getMessage());
                    }
                    
                    if (!temp.delete()) {
                        System.err.println("Error deleting the temporary file");
                    }
                    System.out.println("\nRequisition has been created successfully!\n");
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
    
    public void showRequisitions(){
        try(BufferedReader reader = new BufferedReader(new FileReader("purchaseRequisition.txt"))){
            String line;
            double netTotal = 0;
            while((line = reader.readLine()) != null){
                String[] section = line.split("\\|");
                if(section.length == 4){
                    System.out.printf("\nRequisition ID: %s\n", section[0]);
                    System.out.printf("Date: %s\n", section[1]);
                    System.out.printf("Sales Manager: %s\n", section[2]);
                    System.out.printf("Supplier Name: %s\n\n", section[3]);
                    System.out.printf("%-8s%-12s%-20s%-15s%-10s%-10s\n", "Item ID", "Item Name", "Item Description",
                    "Unit Price", "Quantity", "Total");
                
                }else if(section.length ==6){
                    System.out.printf("%-8s%-12s%-20s%-15s%-10s%-10s\n", section[0], section[1],section[2],section[3],section[4],section[5]);
                    netTotal = netTotal + Double.parseDouble(section[5]);
                }else if(section.length == 2){
                    System.out.println("\nNet Total: " + netTotal + "\n");
                    System.out.println("Instruction: " + section[1] + "\n\n");
                    netTotal = 0;
                }
            }
        }catch(IOException e){
            System.out.println("Error in reading file.");
        }
    }
    
    public void createPurchaseOrder(Scanner scanner, User user){
        scanner.nextLine();
        System.out.println("\n");
        showRequisitions();
        
        boolean foundReqID = false;
        System.out.print("Input the requisition ID to approve: ");
        String findReqID = scanner.nextLine();
        
        
        try(BufferedReader reader = new BufferedReader(new FileReader("purchaseRequisition.txt"));
                BufferedWriter writer = new BufferedWriter(new FileWriter("purchaseOrder.txt",true))){
            String line;
            
            while((line = reader.readLine()) != null){
                String[] section = line.split("\\|");
                if(section.length == 4 && section[0].equals(findReqID)){
                    writer.write(line);
                    writer.write("|" + user.firstname + " " + user.lastname);
                    writer.newLine();
                    
                    foundReqID = true;

                }else if(section.length == 6 && foundReqID){
                    writer.write(line + "\n");
                
                }else if(section.length == 2 && foundReqID){
                    writer.write(line + "\n");
                    foundReqID = false;
                }
            }
        }catch(IOException e){
            System.out.println("Error in reading file.");
        }
        
        //Updates the Purchase Requisition file after purchase order has been approved.
        File temp = new File("temp.txt");
        
        try(BufferedReader reader = new BufferedReader(new FileReader("purchaseRequisition.txt"));
                BufferedWriter writer = new BufferedWriter(new FileWriter(temp))){
            String line;
            
            while((line = reader.readLine()) != null){
                String[] section = line.split("\\|");
                if(section.length == 4 && section[0].equals(findReqID)){
                    //Line skipped
                    foundReqID = true;
                }else if(section.length == 6 && foundReqID){
                    //Line skipped
                
                }else if(section.length == 2 && foundReqID){
                    //Line skipped
                    foundReqID = false;
                } else {
                    writer.write(line + "\n");
                }
            }
        }catch(IOException e){
            System.out.println("Error in reading file.");
        }
        
        //Copies the content in temp text file to the original file
        try (BufferedReader tempReader = new BufferedReader(new FileReader(temp));
             BufferedWriter writer = new BufferedWriter(new FileWriter("purchaseRequisition.txt"))) {

            String line;
            while ((line = tempReader.readLine()) != null) {
                writer.write(line);
                writer.newLine(); 
            }

        } catch (IOException e) {
            System.err.println("Error copying content to the original file: " + e.getMessage());
        }
        
        if (!temp.delete()) {
            System.err.println("Error deleting the temporary file");
        
        }
        
    }
    
    public void viewPurchaseOrders(){
        try(BufferedReader reader = new BufferedReader(new FileReader("purchaseOrder.txt"))){
            String line;
            double netTotal = 0;
            while((line = reader.readLine()) != null){
                String[] section = line.split("\\|");
                if(section.length == 5){
                    System.out.printf("\nRequisition ID: %s\n", section[0]);
                    System.out.printf("Date: %s\n", section[1]);
                    System.out.printf("Sales Manager: %s\n", section[2]);
                    System.out.printf("Supplier Name: %s\n", section[3]);
                    System.out.printf("Approved by: %s\n\n", section[4]);
                    System.out.printf("%-8s%-12s%-20s%-15s%-10s%-10s\n", "Item ID", "Item Name", "Item Description",
                    "Unit Price", "Quantity", "Total");
                
                }else if(section.length == 6){
                    System.out.printf("%-8s%-12s%-20s%-15s%-10s%-10s\n", section[0], section[1],section[2],section[3],section[4],section[5]);
                    netTotal = netTotal + Double.parseDouble(section[5]);
                
                }else if(section.length == 2){
                    System.out.println("\nNet Total: " + netTotal + "\n");
                    System.out.println("Instruction: " + section[1] + "\n\n");
                    netTotal = 0;
                }
            }
        }catch(IOException e){
            System.out.println("Error in reading file.");
        }
    }
    
    public String createRequisitionID(){
        Random reqID = new Random(System.currentTimeMillis());
        return returnStringReqID(reqID);
    }
    
    public String returnStringReqID(Random random){
        int low = 1;
        int high = 9999;
        
        return String.format("%04d", low + random.nextInt(high - low + 1));
    }
    
    public boolean validateDate(int year, int month, int date){
        if(year % 4 == 0){
            if(month == 2){
                if (date >= 1 && date <= 29){
                    System.out.println("Date is valid");
                    return true;
                } else {
                    System.out.println("Invalid date. Maximum date is 29 and minimum date is 1.");
                    return false;
                }
            } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                if (date >= 1 && date <= 31){
                    System.out.println("Date is valid");
                    return true;
                } else {
                    System.out.println("Invalid date. Maximum date is 31 and minimum date is 1.");
                    return false;
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11){
                if (date >= 1 && date <= 30){
                    System.out.println("Date is valid");
                    return true;
                } else {
                    System.out.println("Invalid date. Maximum date is 30 and minimum date is 1.");
                    return false;
                }
            } else {
                System.out.println("Invalid Month.");
                return false;
            }
        } else {
            if(month == 2){
                if (date >= 1 && date <= 28){
                    System.out.println("Date is valid");
                    return true;
                } else {
                    System.out.println("Invalid date. Maximum date is 28 and minimum date is 1.");
                    return false;
                }
            } else if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
                if (date >= 1 && date <= 31){
                    System.out.println("Date is valid");
                    return true;
                } else {
                    System.out.println("Invalid date. Maximum date is 31 and minimum date is 1.");
                    return false;
                }
            } else if (month == 4 || month == 6 || month == 9 || month == 11){
                if (date >= 1 && date <= 30){
                    System.out.println("Date is valid");
                    return true;
                } else {
                    System.out.println("Invalid date. Maximum date is 30 and minimum date is 1.");
                    return false;
                }
            } else {
                System.out.println("Invalid Month.");
                return false;
            }
        }
    }
    
    public String returnStringDate(int year, int month, int date){
        
        String stringYear = Integer.toString(year);
        String stringMonth = Integer.toString(month);
        String stringDate = Integer.toString(date);
        
        return stringYear + "/" + stringMonth + "/" + stringDate;
    }
    
    class salesManager extends User {
        
        public void smMenu(Scanner scanner, User user){
            while(true){    
                System.out.println("\nWelcome " + user.getFirstname() + " " + user.getLastname());
                System.out.println("\nSales Manager Menu");
                System.out.println("1. Item Menu");
                System.out.println("2. Supplier Menu");
                System.out.println("3. Create Purchase Requisition");
                System.out.println("4. Show Purchase Requisition");
                System.out.println("5. View Purchase Orders");
                System.out.println("6. Item Sales");
                System.out.println("0. LOGOUT");

                System.out.print("Enter your option: ");
                int option = scanner.nextInt();

                switch (option){
                    case 1:
                        Item itm = new Item();
                        itm.itemMenu(scanner);
                        break;
                    case 2:
                        Supplier spl = new Supplier();
                        spl.supplierMenu(scanner);
                        break;
                    case 3:
                        scanner.nextLine();
                        createPurchaseRequisition(scanner, user);
                        break;
                    case 4:
                        scanner.nextLine();
                        showRequisitions();
                        break;
                    case 5:
                        scanner.nextLine();
                        viewPurchaseOrders();
                        break;
                    case 6:
                        itemSalesMenu(scanner);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("\nInvalid option"); 
                }

            }
            
        }
        
        public void itemSalesMenu(Scanner scanner){
            
            scanner.nextLine();
            String line;
            double sumTotal = 0;
            while(true){
                //Show the current list of sales
                System.out.println("\nItem Sales");
                System.out.printf("%-15s%-20s%-30s%-20s%-15s%-15s\n", "Item ID", "Item Name", "Item Description",
                        "Item Supplier ID", "Item Quantity", "Total Cost");

                try (BufferedReader reader = new BufferedReader(new FileReader("sales.txt"))){
                    
                    while ((line = reader.readLine()) != null){
                        String[] section = line.split("\\|");
                        if (section.length >= 6){
                            System.out.printf("%-15s%-20s%-30s%-20s%-15s%-15s\n", section[0], section[1], section[2], 
                                section[3], section[4], section[5]);
                            
                            sumTotal = sumTotal + Double.parseDouble(section[5]);
                        }
                    }
                    
                    System.out.println("\nTotal Sale: " + sumTotal);
                } catch (IOException e){
                    System.out.println("Error occured while opening file.");
                }
                System.out.println("\nSales Menu\n");
                System.out.println("1. Append Sale");
                System.out.println("2. Empty Sale data");
                System.out.println("3. GO BACK");

                System.out.print("Enter your option: ");
                int option = scanner.nextInt();

                switch(option){
                    case 1:
                        scanner.nextLine();

                        Item itm = new Item();
                        itm.viewItems();

                        System.out.print("Enter item ID: ");
                        String findID = scanner.nextLine();

                        String stringQuantity;
                        double quantityPrice;
                        
                        try (BufferedReader reader = new BufferedReader(new FileReader("item.txt"));
                                BufferedWriter writer = new BufferedWriter(new FileWriter("sales.txt", true))){

                            while ((line = reader.readLine()) != null){
                                String[] section = line.split("\\|");
                                if(section[0].equals(findID)){
                                    System.out.println("Item found.");
                                    
                                    do{
                                        System.out.print("Set quantity of item sold: ");
                                        Quantity = scanner.nextInt();
                                        scanner.nextLine();
                                        if (Quantity > Integer.parseInt(section[4])){
                                            System.out.println("\nQuantity is more than available stock.");
                                        }
                                    }while(Quantity > Integer.parseInt(section[4]));
                                    
                                    double doublePrice = Double.parseDouble(section[5]); //Note: Convert the read string to double

                                    quantityPrice = doublePrice * Quantity;

                                    stringQuantity = String.valueOf(Quantity); //Note: Converts the int input into string

                                    writer.write(section[0] + "|" + section[1] + "|" + section[2] + "|" + stringQuantity  + 
                                            "|" + section[5] + "|" + quantityPrice + "\n");
                                    
                                    System.out.println("Item Added successfully!");
                                    break;
                                }   
                            }
                            //If itemfound = false, print "Item not found". Empty Else statement
                        } catch (IOException e){
                            System.out.println("Error in opening the files.");
                        }
                        
                        File tempFile = new File("temp_item.txt"); //Note: This is how you create new files

                        try (BufferedReader reader = new BufferedReader(new FileReader("item.txt"));
                                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                            while ((line = reader.readLine()) != null) {

                                String[] section = line.split("\\|");
                                if (section[0].equals(findID)) {
                                    writer.write(section[0] + "|" + section[1] + "|" +section[2]+ "|" +section[3]+ "|" 
                                    + Integer.toString(Integer.parseInt(section[4]) - Quantity) + "|" + section[5] + "\n");
                                    
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

                        break;
                    case 2:
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("sales.txt", false))){
                            System.out.println("\nSales data has been removed successfully.");
                        } catch (IOException e) {
                            System.err.println("Error: " + e.getMessage());
                        }
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println("\nInvalid option.");
                }
            }
        }
    }
    class purchaseManager extends User {
        
        public void pmMenu(Scanner scanner, User user){
            while(true){
                System.out.println("\nPurchase Manager Menu");
                System.out.println("1. View Items");
                System.out.println("2. View Suppliers");
                System.out.println("3. View Purchase Requisition");
                System.out.println("4. Create Purchase Order");
                System.out.println("5. View Purchase Order");
                System.out.println("0. LOGOUT");

                System.out.print("Enter your option: ");
                int option = scanner.nextInt();
            
                switch (option){
                    case 1:
                        Item itm = new Item();
                        itm.viewItems();
                        break;
                    case 2:
                        Supplier sp = new Supplier();
                        sp.viewSuppliers();
                        break;
                    case 3:
                        scanner.nextLine();
                        showRequisitions();
                        break;
                    case 4:
                        createPurchaseOrder(scanner,user);
                        break;
                    case 5:
                        scanner.nextLine();
                        viewPurchaseOrders();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("\nInvalid option"); 
                }
            
            }
            
        }
    }
}

