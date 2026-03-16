create database Payroll;
USE Payroll;

CREATE TABLE employees (
    empId INT PRIMARY KEY,
    name VARCHAR(50),
    designation VARCHAR(50),
    basicSalary DOUBLE,
    netSalary DOUBLE
);