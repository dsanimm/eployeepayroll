package com.capgemini.pojo;

import java.time.LocalDate;

public class EmployeePayrollData {
	private int id;
	private String name;
	private double salary;
	public LocalDate startDte;

	public EmployeePayrollData(int id, String name, double salary) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
	}

	public EmployeePayrollData(int id, String name, double salary, LocalDate startDte) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.startDte = startDte;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData [id=" + id + ", name=" + name + ", salary=" + salary + "]";
	}

}