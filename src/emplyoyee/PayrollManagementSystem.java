package emplyoyee;

import java.util.*;
import java.io.*;
import java.sql.*;

class Employee 
{
    int empId;
    String name;
    String designation;
    double basicSalary;
    double hra, da, grossSalary, tax, netSalary;

    Employee(int empId, String name, String designation, double basicSalary) 
    {
        this.empId = empId;
        this.name = name;
        this.designation = designation;
        this.basicSalary = basicSalary;
        calculateSalary();
    }

    void calculateSalary() 
    {
        if (designation.equalsIgnoreCase("Manager")) 
        {
            hra = 0.30 * basicSalary;
            da = 0.15 * basicSalary;
        } 
        else if (designation.equalsIgnoreCase("Developer")) 
        {
            hra = 0.25 * basicSalary;
            da = 0.12 * basicSalary;
        } 
        else 
        {
            hra = 0.20 * basicSalary;
            da = 0.10 * basicSalary;
        }

        grossSalary = basicSalary + hra + da;

        // Tax Slabs
        if (grossSalary < 250000)
            tax = 0;
        else if (grossSalary < 500000)
            tax = grossSalary * 0.05;
        else if (grossSalary < 1000000)
            tax = grossSalary * 0.20;
        else
            tax = grossSalary * 0.30;

        netSalary = grossSalary - tax;
    }

    void display() 
    {
        System.out.println("\nEmployee ID: " + empId);
        System.out.println("Name: " + name);
        System.out.println("Designation: " + designation);
        System.out.println("Basic Salary: ₹" + basicSalary);
        System.out.println("Gross Salary: ₹" + grossSalary);
        System.out.println("Tax: ₹" + tax);
        System.out.println("Net Salary: ₹" + netSalary);
    }
}

public class PayrollManagementSystem 
{

    static ArrayList<Employee> employees = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    static void addEmployee() 
    {

        try {

            System.out.print("Enter Employee ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Enter Name: ");
            String name = sc.nextLine();

            System.out.print("Enter Designation (Manager/Developer/Other): ");
            String designation = sc.nextLine();

            System.out.print("Enter Basic Salary: ");
            double salary = sc.nextDouble();

            Employee emp = new Employee(id, name, designation, salary);
            employees.add(emp);

            saveToFile(emp);
            saveToDatabase(emp);

            System.out.println("Employee Added Successfully!");

        } 
        catch (InputMismatchException e) 
        {
            System.out.println("Invalid Input! Please enter correct data.");
            sc.nextLine();
        }
    }

    static void viewAllEmployees() 
    {

        if (employees.isEmpty()) 
        {
            System.out.println("No Employees Found!");
            return;
        }

        for (Employee e : employees) 
        {
            e.display();
        }
    }

    // FILE HANDLING
    static void saveToFile(Employee e) 
    {

        try {
            FileWriter fw = new FileWriter("payroll_records.txt", true);

            fw.write(e.empId + "," +
                    e.name + "," +
                    e.designation + "," +
                    e.netSalary + "\n");

            fw.close();
        } 
        catch (IOException ex) 
        {
            System.out.println("File Error!");
        }
    }

    // JDBC DATABASE SAVE
    static void saveToDatabase(Employee e) 
    {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/Payroll", "root", "mysqlproject3337");

            String query = "INSERT INTO employees VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1, e.empId);
            ps.setString(2, e.name);
            ps.setString(3, e.designation);
            ps.setDouble(4, e.basicSalary);
            ps.setDouble(5, e.netSalary);

            ps.executeUpdate();

            con.close();

        } 
        catch (Exception ex) 
        {
            System.out.println("Database connection failed.");
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) 
    {

        int choice;

        do {

            System.out.println("\n----- Payroll Management System -----");
            System.out.println("1. Add Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Exit");

            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    addEmployee();
                    break;

                case 2:
                    viewAllEmployees();
                    break;

                case 3:
                    System.out.println("Exiting System...");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 3);
    }
}