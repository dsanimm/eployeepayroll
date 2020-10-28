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

	public List<EmployeePayrollData> retrieveByDate(String startDate) {

		return new EmployeePayrollDBService().retrieveByDateFromDB(startDate);

	}

	private EmployeePayrollData getEmployee(List<EmployeePayrollData> list, String name) {
		return list.stream().filter(n -> n.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public boolean checkEmployeePayrollInSyncWithDB(String name) {
		List<EmployeePayrollData> employeePayrollDataList = EmployeePayrollDBService.getEmployeePayrollDataFromDB(name);
		return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
	}

	private EmployeePayrollData getEmployeePayrollData(String name) {
		EmployeePayrollData employeePayrollData;
		employeePayrollData = this.employeePayrollList.stream().filter(l -> l.getName().equals(name)).findFirst()
				.orElse(null);
		return employeePayrollData;
	}

}
