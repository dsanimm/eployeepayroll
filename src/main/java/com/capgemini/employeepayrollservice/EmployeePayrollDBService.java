package com.capgemini.employeepayrollservice;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.pojo.EmployeePayrollData;

public class EmployeePayrollDBService {
	private static EmployeePayrollDBService employeePayrollDBService = new EmployeePayrollDBService();
	private static PreparedStatement employeePayrollDataStatement;
	static List<EmployeePayrollData> employeePayrollDataList = new ArrayList<EmployeePayrollData>();

	public static List<EmployeePayrollData> readData() {

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

	public int updateEmployeeData(String name, double salary) {

		try (Connection conn = employeePayrollDBService.getConnection()) {
			PreparedStatement stmt = conn.prepareStatement("update employee_payroll set salary = ? where name = ?;");
			stmt.setDouble(1, salary);
			stmt.setString(2, name);
			return stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) {
		List<EmployeePayrollData> employeePayrollList = null;
		if (employeePayrollDataStatement == null)
			employeePayrollDBService.prepareStatementForEmployeeData();
		try {
			employeePayrollDataStatement.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
			employeePayrollList = employeePayrollDBService.getEmployeePayrollData(resultSet);
			return employeePayrollList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) throws SQLException {
		List<EmployeePayrollData> employeeDataList = new ArrayList<EmployeePayrollData>();
		while (resultSet.next()) {
			int id = resultSet.getInt("Id");
			String name = resultSet.getString("Name");
			Double salary = resultSet.getDouble("salary");
			LocalDate startDate = resultSet.getDate("start").toLocalDate();
			employeeDataList.add(new EmployeePayrollData(id, name, salary, startDate));
		}
		return employeeDataList;
	}

	private void prepareStatementForEmployeeData() {
		try {
			Connection conn = employeePayrollDBService.getConnection();
			employeePayrollDataStatement = conn.prepareStatement("select * from employee_payroll where Name = ?;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<EmployeePayrollData> retrieveByDateFromDB(String startDate) {
		ResultSet rs;
		List<EmployeePayrollData> employeeList = new ArrayList<>();
		try (Connection conn = employeePayrollDBService.getConnection()) {

			PreparedStatement stmt = conn.prepareStatement(
					"select * from employee_payroll where start between CAST(? as date) and DATE(now());");
			stmt.setString(1, startDate);

			rs = stmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("Id");
				String name = rs.getString("Name");
				Double salary = rs.getDouble("salary");
				LocalDate startDated = rs.getDate("start").toLocalDate();
				employeeList.add(new EmployeePayrollData(id, name, salary, startDated));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return employeeList;
	}

	public double retrieveByGenderWithOperation(String operation, String gender) {
		ResultSet rs;
		double result = 0;

		List<EmployeePayrollData> employeeList = new ArrayList<>();
		try (Connection conn = employeePayrollDBService.getConnection()) {
			String sql = String.format("select %s(salary) from employee_payroll where gender = '%s' group by gender;",
					operation, gender);
			String header = String.format("%s(salary)", operation);
			Statement stmt = conn.prepareStatement(sql);
			System.out.println(sql);
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				result = rs.getDouble(header);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public int addEmployeeData(String name, double salary, String start) {
		try (Connection conn = employeePayrollDBService.getConnection()) {

			PreparedStatement stmt = conn.prepareStatement(
					"INSERT INTO employee_payroll(name, salary, start) values \r\n" + "			(	?, ?, ?	);");
			stmt.setDouble(2, salary);
			stmt.setString(1, name);
			stmt.setString(3, start);
			System.out.println(stmt.toString());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

}
