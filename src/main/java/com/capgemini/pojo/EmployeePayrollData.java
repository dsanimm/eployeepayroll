package com.capgemini.pojo;

import java.time.LocalDate;

public class EmployeePayrollData {
	private int id;
	private String name;
	private double salary;
	public LocalDate startDate;

	double basic_pay;
	double deductions;
	double taxable_pay;
	double tax;
	double net_pay;

	String department;
	String Company_Name;
	String gender;

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public double getBasic_pay() {
		return basic_pay;
	}

	public void setBasic_pay(double basic_pay) {
		this.basic_pay = basic_pay;
	}

	public double getDeductions() {
		return deductions;
	}

	public void setDeductions(double deductions) {
		this.deductions = deductions;
	}

	public double getTaxable_pay() {
		return taxable_pay;
	}

	public void setTaxable_pay(double taxable_pay) {
		this.taxable_pay = taxable_pay;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getNet_pay() {
		return net_pay;
	}

	public void setNet_pay(double net_pay) {
		this.net_pay = net_pay;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getCompany_Name() {
		return Company_Name;
	}

	public void setCompany_Name(String company_Name) {
		Company_Name = company_Name;
	}

	public EmployeePayrollData(int id, String name, Double salary, LocalDate startDate, double basic_pay,
			double deductions, double taxable_pay, double tax, double net_pay, String department, String company_Name,
			String gender) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.startDate = startDate;
		this.basic_pay = basic_pay;
		this.deductions = deductions;
		this.taxable_pay = taxable_pay;
		this.tax = tax;
		this.net_pay = net_pay;
		this.department = department;
		Company_Name = company_Name;
	}

	public EmployeePayrollData(int id, String name, Double salary, LocalDate startDated) {
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.startDate = startDated;	}

	@Override
	public String toString() {
		return "EmployeePayrollData [id=" + id + ", name=" + name + ", salary=" + salary + ", startDate=" + startDate
				+ ", basic_pay=" + basic_pay + ", deductions=" + deductions + ", taxable_pay=" + taxable_pay + ", tax="
				+ tax + ", net_pay=" + net_pay + ", department=" + department + ", Company_Name=" + Company_Name
				+ ", gender=" + gender + "]";
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeePayrollData other = (EmployeePayrollData) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

}