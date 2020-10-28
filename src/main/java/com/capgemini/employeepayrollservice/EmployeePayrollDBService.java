package com.capgemini.employeepayrollservice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.pojo.EmployeePayrollData;

public class EmployeePayrollDBService {

	public static List<EmployeePayrollData> readData() {
		EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();

		List<EmployeePayrollData> employeePayrollDataList = new ArrayList<EmployeePayrollData>();

		ResultSet rs;
		try (Connection conn = employeePayrollDBService.getConnection()) {

			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from employee_payroll");
			while (rs.next()) {
				int id = rs.getInt("Id");
				String name = rs.getString("Name");
				Double salary = rs.getDouble("salary");
				LocalDate startDate = rs.getDate("start").toLocalDate();
				employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, startDate));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return employeePayrollDataList;
	}

	private Connection getConnection() throws SQLException {
		String jdbcUrl = "jdbc:mysql://localhost:3306/payroll_service";
		String userName = "root";
		String passWord = "drownzer";
		Connection conn;

		System.out.println("Connecting to Database : " + jdbcUrl);
		conn = DriverManager.getConnection(jdbcUrl, userName, passWord);
		System.out.println("Connection is successful : " + conn);

		return conn;
	}

}
