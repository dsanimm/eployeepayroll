package com.capgemini.employeepayrollservice;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.pojo.*;

public class EmployeePayrollService {
	public static List<EmployeePayrollData> employeePayrollList;

	public List<EmployeePayrollData> reademployeePayrollData() {
		this.employeePayrollList = new EmployeePayrollDBService().readData();
		return this.employeePayrollList;
	}

	public void updateEmployeePayrollSalary(String name, double salary) {

		int result = new EmployeePayrollDBService().updateEmployeeData(name, salary);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null) {
			employeePayrollData.setSalary(salary);
		}

	}

	public void deleteEmployee(String name) {

		new EmployeePayrollDBService().deleteEmployeeData(name);

	}

	public void addEmployee(String name, Double salary, String startDate, double deductions, double taxable_pay,
			double tax, double net_pay, String department, String company_Name, String gender) {

		int result = new EmployeePayrollDBService().addEmployeeData(name, salary, startDate, deductions, taxable_pay,
				tax, net_pay, department, company_Name, gender);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null) {
			employeePayrollData.setSalary(salary);
		}

	}

	public void addMultipleEmployee(List<EmployeePayrollData> empList) {

		int result = new EmployeePayrollDBService().addMultipleEmployeeData(empList);
	}

	public void addEmployeeDetailsWithPayroll(String name, double salary, String start, double deductions,
			double taxable_pay, double tax, double net_pay) {

		int result = new EmployeePayrollDBService().addEmployeeDetailswPayroll(name, salary, start, deductions,
				taxable_pay, tax, net_pay);
		if (result == 0)
			return;
		EmployeePayrollData employeePayrollData = this.getEmployeePayrollData(name);
		if (employeePayrollData != null) {
			employeePayrollData.setSalary(salary);
		}

	}

	public List<EmployeePayrollData> retrieveByDate(String startDate) {

		return new EmployeePayrollDBService().retrieveByDateFromDB(startDate);

	}

	public double retrieveByGenderWithoperation(String operation, String gender) {

		return new EmployeePayrollDBService().retrieveByGenderWithOperation(operation, gender);

	}

	private EmployeePayrollData getEmployee(List<EmployeePayrollData> list, String name) {
		return list.stream().filter(n -> n.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		List<EmployeePayrollData> employeePayrollDataList = EmployeePayrollDBService.getEmployeePayrollDataFromDB(name);
		System.out.println(employeePayrollDataList.get(0));
		System.out.println(getEmployeePayrollData(name));
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		EmployeePayrollData employeePayrollData;
		employeePayrollData = this.employeePayrollList.stream().filter(l -> l.getName().equals(name)).findFirst()
				.orElse(null);
		return employeePayrollData;
	}

}
