package POM;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Admin {
    private String username, password, name;
    
    File smFile = new File("smUsers.txt");
    File pmFile = new File("pmUsers.txt");
    
    User regUser = new User();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    protected void adMenu(Scanner scanner, Admin admin){
        User aduser = new User();
        while(true){
            System.out.println("\nAdmin Menu");
            System.out.println("1. Items Menu");
            System.out.println("2. Supplier Menu");
            System.out.println("3. Create Purchase Requisition");
            System.out.println("4. Show Purchase Requisition");
            System.out.println("5. Create Purchase Order");
            System.out.println("6. View Purchase Order");
            System.out.println("\n Add Users \n");
            System.out.println("7. Register Sales Manager");
            System.out.println("8. Register Purchase Manager");
            System.out.println("0. LOGOUT");
            
            System.out.print("Enter your option: ");
            int option = scanner.nextInt();
            
            switch (option){
                case 1:
                    Item adminitm = new Item();
                    adminitm.itemMenu(scanner);
                    break;
                case 2:
                    Supplier adminsp = new Supplier();
                    adminsp.supplierMenu(scanner);
                    break;
                case 3:
                    adCreatePurchaseRequisition(scanner, admin);
                    break;
                case 4:
                    aduser.showRequisitions();
                    break;
                case 5:
                    adCreatePurchaseOrder(scanner, admin);
                    break;
                case 6:
                    aduser.viewPurchaseOrders();
                    break;
                
                case 7:
                    smregister(scanner);
                    break;
                case 8:
                    pmregister(scanner);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("\nInvalid option"); 
            }
        }    
            
    }
    
    private boolean validateSMExist(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(smFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking if user exists");
        }
        return false;
    
    }
    
    private boolean validatePMExist(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(pmFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 4 && parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error checking if user exists");
        }
        return false;
    
    }
    
    public void smregister(Scanner scanner) {
        scanner.nextLine();
        User.salesManager smLog = regUser.new salesManager();
        
        do{
            System.out.print("Enter new username: ");
            smLog.setUsername(scanner.nextLine());
            
            if (validateSMExist(smLog.getUsername())) {
                System.out.println("Username already exists.");
                return;
            }
        }while(validateSMExist(smLog.getUsername()));
        
        
        System.out.print("Enter new password: ");
        smLog.setPassword(scanner.nextLine());
        System.out.print("Enter first name: ");
        smLog.setFirstname(scanner.nextLine());
        System.out.print("Enter last name: ");
        smLog.setLastname(scanner.nextLine());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(smFile, true))) {
            writer.write(smLog.getUsername() + "|" + smLog.getPassword() + "|" + smLog.getFirstname() + "|" + smLog.getLastname());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error adding user to file.");
        }
        
        System.out.println("Sales Manager registration successful!");
    }
    
    public void pmregister(Scanner scanner) {
        scanner.nextLine();
        User.purchaseManager pmLog = regUser.new purchaseManager();
        
        do{
            System.out.print("Enter new username: ");
            pmLog.setUsername(scanner.nextLine());
            
            if (validateSMExist(pmLog.getUsername())) {
                System.out.println("Username already exists.");
                return;
            }
        }while(validateSMExist(pmLog.getUsername()));
        
        
        System.out.print("Enter new password: ");
        pmLog.setPassword(scanner.nextLine());
        System.out.print("Enter first name: ");
        pmLog.setFirstname(scanner.nextLine());
        System.out.print("Enter last name: ");
        pmLog.setLastname(scanner.nextLine());

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(pmFile, true))) {
            writer.write(pmLog.getUsername() + "|" + pmLog.getPassword() + "|" + pmLog.getFirstname() + "|" + pmLog.getLastname());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error adding user to file.");
        }
        System.out.println("Purchase Manager registration successful!");
    }
    
    
    protected void adCreatePurchaseRequisition(Scanner scanner, Admin admin){
        User req = new User();
        String line;
        Item itmReq = new Item();
        Supplier spReq = new Supplier();
        
        String reqNumber = req.createRequisitionID(); //Requisition ID
        int year, month, date;
        
        do {
            System.out.print("Enter the year: ");
            year = scanner.nextInt();
            
            System.out.print("Enter the month: ");
            month = scanner.nextInt();
            
            System.out.print("Enter the date: ");
            date = scanner.nextInt(); 
            
            if(!req.validateDate(year, month, date)){
                System.out.println("Incorrect Date.\n");
            }
            
        }while(!req.validateDate(year, month, date)); //Requisition date
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
            writer.write(reqNumber + "|" + req.returnStringDate(year, month, date) + "|" + 
                    admin.getName() + "|" + spReq.returnSupplierName(spReq.getId()) + "\n");
            
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
                                req.setQuantity(scanner.nextInt());
                                scanner.nextLine();

                                totalprice = req.getQuantity() * itmReq.getPrice();

                                try(BufferedWriter writer = new BufferedWriter(new FileWriter (temp, true))){
                                    writer.write(itmReq.getId() + "|" + itmReq.getName() + "|" + 
                                            itmReq.getDescription() + "|" + itmReq.getPrice() + "|" + req.getQuantity() + "|" +
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
    
    protected void adCreatePurchaseOrder(Scanner scanner, Admin admin){
        User req = new User();
        scanner.nextLine();
        System.out.println("\n");
        req.showRequisitions();
        
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
                    writer.write("|" + admin.getName());
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
}
