# Purchase Order Management System

A console-based purchase order management system for a fictional company,
written in Java. Three user roles move stock requests through a requisition
and approval workflow, with all records stored in plain text files.

## About the project

The system models the purchasing cycle of a small business. A Sales Manager
records what stock is needed, a Purchase Manager turns those requests into
orders placed with suppliers, and an Admin oversees both and manages user
accounts. Everything runs in the terminal through numbered menus.

## Roles

| Role | Can do |
|---|---|
| Sales Manager | Manage items and suppliers, raise purchase requisitions, view orders and item sales |
| Purchase Manager | View items, suppliers and requisitions, create and view purchase orders |
| Admin | Everything above, plus registering new Sales and Purchase Managers |

## Getting started

**In NetBeans:** open the project and run it. The main class is `POM.POM`.

**From the command line,** with a JDK installed:

```bash
javac -d build/classes src/POM/*.java
java -cp build/classes POM.POM
```

Run it from the project root folder. The program reads and writes its text
files by name in the current directory, so launching from anywhere else will
fail to find them.

### Sample logins

| Role | Username | Password |
|---|---|---|
| Sales Manager | sm1 | john |
| Purchase Manager | pm1 | henry |
| Admin | admin | admin1 |

These are demonstration accounts with placeholder names. Passwords are stored
in plain text, which is fine for a sample or coursework projects but credentials 
should never be handled like this in production.

## Using the system

Every screen is a numbered menu. Type a number and press Enter; `0` goes back
or logs out.

**A typical run through the workflow:**

1. Log in as the Sales Manager. From **Item Menu**, add the items you stock,
   giving each an item code, a supplier code, a quantity and a price. Add the
   supplier first from **Supplier Menu** if it doesn't exist yet, since items
   reference suppliers by code.
2. Choose **Create Purchase Requisition** and add the items you need restocked.
   The requisition stays open until you select **Finish and save**.
3. Log out and log back in as the Purchase Manager. **View Purchase
   Requisition** shows what has been raised.
4. Choose **Create Purchase Order** to convert a requisition into an order.
   **View Purchase Order** confirms it was recorded.
5. Logging in as Admin gives access to both sides of the workflow, plus
   **Register Sales Manager** and **Register Purchase Manager** for adding
   accounts.

## Data storage

There is no database. Records are kept in pipe-delimited text files in the
project root, which the program reads on startup and rewrites as records
change.

| File | Contents |
|---|---|
| `item.txt` | `code \| name \| description \| supplierCode \| quantity \| price` |
| `supplier.txt` | `code \| name` |
| `smUsers.txt`, `pmUsers.txt` | `username \| password \| firstname \| lastname` |
| `purchaseRequisition.txt` | Saved requisitions |
| `purchaseOrder.txt` | Saved purchase orders |
| `sales.txt` | Recorded item sales |

The files are committed with sample data so the system runs immediately after
cloning. Some start empty and fill up as you use it.

## Built with

Java SE, using `java.io` for file handling and `Scanner` for terminal input.
Developed in NetBeans, with the `nbproject` files included so it opens
directly there.
