package com.capgemini.employeepayrollservice;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.capgemini.employeepayrollservice.EmployeePayrollService;

import com.capgemini.pojo.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import junit.framework.Assert;
import static org.junit.Assert.*;

public class EmployeePayrollServiceTest {
	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	public EmployeePayrollData[] getEmployee() {
		Response response = RestAssured.get("/employee_details");
		System.out.println("Employee Payroll entries in Json Server : \n" + response.asString());
		EmployeePayrollData[] empdata = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
		return empdata;
	}

	private Response addEmployeeToJsonServer(EmployeePayrollData newEmp) {
		String empJson = new GsonBuilder().setPrettyPrinting().create().toJson(newEmp);
		RequestSpecification request = RestAssured.given();
		request.header("Content-type", "application/json");
		request.body(empJson);
		return request.post("/employee_details");
	}

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
		assertEquals(300000.0, employeePayrollData, .1);
	}

	@Test
	public void givenNewEmployeeData_WhenAdded_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.reademployeePayrollData();
		employeePayrollService.addEmployee("tiger", 400000.0, "2017-04-02", 10000.0, 10000.0, 10000.0, 10000.0,
				"snacks", "patanjali", "F");
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("tiger");
		System.out.println(result);
		assertTrue(result);
	}

	@Test
	public void givenNewEmployeePayrollData_WhenAdded_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.reademployeePayrollData();
		System.out.println("true");
		employeePayrollService.addEmployeeDetailsWithPayroll("liger", 500000.0, "2016-04-02", 50000.0, 350000.0,
				10000.0, 340000.0);
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("liger");
		System.out.println(result);
		assertTrue(result);
	}

	@Test
	public void givenNEmployeeName_WhenDeleted_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.deleteEmployee("liger");
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.reademployeePayrollData();
		String result = employeePayrollData.stream().filter(n -> n.getName().equalsIgnoreCase("liger")).findFirst()
				.map(n -> n.getName()).orElse(null);
		assertTrue(result == (null));
	}

	@Test
	public void givenEmployeePayrollDatabase_whenAddedListOfEmployee_shouldReturnNoOfEntries() {
		EmployeePayrollData[] empArr = new EmployeePayrollData[] {
				new EmployeePayrollData("tiger", 400000.0, LocalDate.parse("2017-04-02"), 10000.0, 10000.0, 10000.0,
						10000.0, 10000.0, new String[] { "berserk" }, "patanjali", "F"),
				new EmployeePayrollData("viger", 400000.0, LocalDate.parse("2017-04-02"), 10000.0, 10000.0, 10000.0,
						10000.0, 10000.0, new String[] { "berserk", "one piece" }, "patanjali", "F"),
				new EmployeePayrollData("niger", 400000.0, LocalDate.parse("2017-04-02"), 10000.0, 10000.0, 10000.0,
						10000.0, 10000.0, new String[] { "naruto" }, "patanjali", "F"),
				new EmployeePayrollData("wiger", 400000.0, LocalDate.parse("2017-04-02"), 10000.0, 10000.0, 10000.0,
						10000.0, 10000.0, new String[] { "berserk", "naruto" }, "patanjali", "F"),
				new EmployeePayrollData("siger", 400000.0, LocalDate.parse("2017-04-02"), 10000.0, 10000.0, 10000.0,
						10000.0, 10000.0, new String[] { "berserk", "one piece", "naruto" }, "patanjali", "F") };
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Instant start = Instant.now();
		employeePayrollService.addMultipleEmployee((Arrays.asList(empArr)));
		Instant end = Instant.now();
		System.out.println("Duration Without Thread :" + Duration.between(start, end));
		Instant startWithThread = Instant.now();
		employeePayrollService.addListOfEmployeeWithThreads(Arrays.asList(empArr));
		Instant endWithThread = Instant.now();
		System.out.println("Duration With Thread :" + Duration.between(startWithThread, endWithThread));
		employeePayrollService.reademployeePayrollData();
		for (EmployeePayrollData emp : employeePayrollService.employeePayrollList) {
			System.out.println(emp);
		}
		assertEquals(14, employeePayrollService.employeePayrollList.size());

	}

	@Test
	public void givenEmployeeDetailsInJsonServer_whenRetrieved_shouldReturnNoOfCounts() {
		EmployeePayrollData[] empData = getEmployee();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(empData));
		long entries = employeePayrollService.employeePayrollList.size();
		assertEquals(5, entries);
	}

	@Test
	public void givenEmployeeDetailsInJsonServer_whenAddedEnEmployee_shouldReturnNoOfCountsAndResponseCode() {
		EmployeePayrollData[] empData = getEmployee();
		EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(empData));	
		Response response = addEmployeeToJsonServer((new EmployeePayrollData("tiger", 400000.0, LocalDate.parse("2017-04-02"), 10000.0, 10000.0, 10000.0,
				10000.0, 10000.0, new String[] { "berserk" }, "patanjali", "F")));
		int statusCode = response.getStatusCode();
		assertEquals(201, statusCode);
		EmployeePayrollData empDataFromResponse = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
		employeePayrollService.employeePayrollList.add(empDataFromResponse);
		System.out.println(employeePayrollService.employeePayrollList);
		long count = employeePayrollService.employeePayrollList.size();
		assertEquals(5, count);
	}

}
