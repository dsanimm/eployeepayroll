package com.capgemini.employeepayrollservice;

import java.util.List;

import org.junit.Test;

import com.capgemini.employeepayrollservice.EmployeePayrollService;

import com.capgemini.pojo.*;

import junit.framework.Assert;
import static org.junit.Assert.*;

public class EmployeePayrollServiceTest {
	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.reademployeePayrollData();
		assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.reademployeePayrollData();
		employeePayrollService.updateEmployeePayrollSalary("terisa", 300000.0);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("terisa");
		System.out.println(result);
		assertTrue(result);
	}

	@Test
	public void givenDateRange_WhenRetrieved_ShouldMatchCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.retrieveByDate("2015-01-01");
		assertEquals(4, employeePayrollData.size());
	}

	@Test
	public void givenOperationOnGender_WhenRetrieved_ShouldMatchCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		double employeePayrollData = employeePayrollService.retrieveByGenderWithoperation("sum", "F");
		assertEquals(300000.0, employeePayrollData,.1);
	}
	@Test
	public void givenNewEmployeeData_WhenAdded_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.reademployeePayrollData();
		employeePayrollService.addEmployee("tiger", 400000.0, "2017-04-02",10000.0,10000.0,10000.0,10000.0,"snacks","patanjali","F");
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("tiger");
		System.out.println(result);
		assertTrue(result);
	}
	@Test
	public void givenNewEmployeePayrollData_WhenAdded_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.reademployeePayrollData();
		System.out.println("true");
		employeePayrollService.addEmployeeDetailsWithPayroll("liger", 500000.0, "2016-04-02",50000.0,350000.0,10000.0,340000.0);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("liger");
		System.out.println(result);
		assertTrue(result);
	}
}
