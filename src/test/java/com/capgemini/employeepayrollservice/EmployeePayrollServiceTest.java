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
		employeePayrollService.updateEmployeePayrollSalary("terisa",300000.0);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("terisa");
		System.out.println(result);
		assertTrue(result);
	}
}
