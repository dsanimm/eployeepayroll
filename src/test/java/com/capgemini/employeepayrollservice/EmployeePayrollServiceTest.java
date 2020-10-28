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
}
