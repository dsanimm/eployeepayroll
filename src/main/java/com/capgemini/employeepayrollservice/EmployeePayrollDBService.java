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
			rs = stmt.executeQuery(
					"select * FROM (((employee_payroll inner join emp_department on employee_payroll.Id = emp_department.Id) inner join payroll on employee_payroll.Id = payroll.Id)inner join Company on employee_payroll.Id = Company.company_Id); \r\n"
							+ "");
			while (rs.next()) {
				int id = rs.getInt("Id");
				String name = rs.getString("Name");
				Double salary = rs.getDouble("salary");
				LocalDate startDate = rs.getDate("start").toLocalDate();
				double basic_pay = rs.getDouble("basic_pay");
				double deductions = rs.getDouble("deductions");
				double taxable_pay = rs.getDouble("taxable_pay");
				double tax = rs.getDouble("tax");
				double net_pay = rs.getDouble("net_pay");
				String department = rs.getString("department");
				String Company_Name = rs.getString("Company_Name");
				String gender = rs.getString("gender");
				employeePayrollDataList.add(new EmployeePayrollData(id, name, salary, startDate, basic_pay, deductions,
						taxable_pay, tax, net_pay, department, Company_Name, gender));

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
			PreparedStatement stmt = conn.prepareStatement("select Id from employee_payroll where name=?;");
			stmt.setString(1, name);
			ResultSet rs = stmt.executeQuery();
			int Id = 0;
			while (rs.next()) {
				Id = rs.getInt("Id");
			}
			int id;
			while (rs.next())
				id = rs.getInt("max(Id)") + 1;
			conn.setAutoCommit(false);
			stmt = conn.prepareStatement("update employee_payroll set salary = ? where name = ?;");
			PreparedStatement stmtForPayroll = conn.prepareStatement("update payroll set basic_pay = ? where Id = ?;");
			stmtForPayroll.setDouble(1, salary);
			stmtForPayroll.setInt(2, Id);
			stmt.setDouble(1, salary);
			stmt.setString(2, name);
			conn.setAutoCommit(true);
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
		try (Connection conn = employeePayrollDBService.getConnection()) {
			employeePayrollDataStatement.setString(1, name);
			System.out.println(employeePayrollDataStatement.toString());
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
			System.out.println(id);
			String name = resultSet.getString("Name");
			Double salary = resultSet.getDouble("salary");
			LocalDate startDate = resultSet.getDate("start").toLocalDate();
			double basic_pay = resultSet.getDouble("salary");
			;
			double deductions = resultSet.getDouble("deductions");
			double taxable_pay = resultSet.getDouble("taxable_pay");
			double tax = resultSet.getDouble("tax");
			double net_pay = resultSet.getDouble("net_pay");
			String department = resultSet.getString("department");
			String Company_Name = resultSet.getString("Company_Name");
			String gender = resultSet.getString("gender");
			employeeDataList.add(new EmployeePayrollData(id, name, salary, startDate, basic_pay, deductions,
					taxable_pay, tax, net_pay, department, Company_Name, gender));
		}
		return employeeDataList;
	}

	private void prepareStatementForEmployeeData() {
		try {
			Connection conn = employeePayrollDBService.getConnection();
			employeePayrollDataStatement = conn.prepareStatement(
					"select * FROM (((employee_payroll inner join emp_department on employee_payroll.Id = emp_department.Id) inner join payroll on employee_payroll.Id = payroll.Id)inner join Company on employee_payroll.Id = Company.company_Id) where name = ?;");
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

	public int addEmployeeData(String name, Double salary, String startDate, double deductions, double taxable_pay,
			double tax, double net_pay, String department, String company_Name, String gender) {
		try (Connection conn = employeePayrollDBService.getConnection()) {
			int id = getMaxId();
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO employee_payroll(name, salary, start,gender) values \r\n"
							+ "			(	?, ?, ?,?	);");
			stmt.setDouble(2, salary);
			stmt.setString(1, name);
			stmt.setString(3, startDate);
			stmt.setString(4, gender);
			System.out.println(stmt.toString());
			stmt.executeUpdate();
			stmt = conn.prepareStatement(
					"INSERT INTO payroll(basic_pay, deductions, taxable_pay,tax,net_pay,Id) values \r\n"
							+ "(?, ?, ?,?,?,?);");
			stmt.setDouble(1, salary);
			stmt.setDouble(2, deductions);
			stmt.setDouble(3, taxable_pay);
			stmt.setDouble(4, tax);
			stmt.setDouble(5, net_pay);
			stmt.setInt(6, id);
			System.out.println(stmt.toString());
			stmt.executeUpdate();
			stmt = conn.prepareStatement("INSERT INTO emp_department(Id, department) values \r\n" + "(?, ?);");
			stmt.setInt(1, id);
			stmt.setString(2, department);
			stmt.executeUpdate();
			stmt = conn.prepareStatement("INSERT INTO company(company_Id, Company_Name) values \r\n" + "(?, ?);");
			stmt.setInt(1, id);
			stmt.setString(2, company_Name);
			stmt.executeUpdate();
			conn.setAutoCommit(true);

			return 1;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int addEmployeeDetailswPayroll(String name, double salary, String start, double deductions,
			double taxable_pay, double tax, double net_pay) {
		try (Connection conn = employeePayrollDBService.getConnection()) {
			int Id = getMaxId();
			conn.setAutoCommit(false);
			PreparedStatement stmtEmployee = conn
					.prepareStatement("INSERT INTO employee_payroll(name, salary, start) values (	?, ?, ?	);");
			stmtEmployee.setDouble(2, salary);
			stmtEmployee.setString(1, name);
			stmtEmployee.setString(3, start);
			System.out.println(stmtEmployee.toString());
			stmtEmployee.executeUpdate();
			PreparedStatement stmtPayroll = conn.prepareStatement("INSERT INTO payroll( basic_pay,deductions,\r\n"
					+ "			 taxable_pay, tax, net_pay,Id) values (?, ?, ?,?,?,?);");
			stmtPayroll.setDouble(1, salary);
			stmtPayroll.setDouble(2, deductions);
			stmtPayroll.setDouble(3, taxable_pay);
			stmtPayroll.setDouble(4, tax);
			stmtPayroll.setDouble(5, net_pay);
			stmtPayroll.setInt(6, Id);
			System.out.println(stmtPayroll.toString());
			stmtPayroll.executeUpdate();
			conn.commit();
			return 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public static int getMaxId() {

		ResultSet rs;
		int id = 0;
		try (Connection conn = employeePayrollDBService.getConnection()) {

			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery("select max(Id) from employee_payroll;");
			while (rs.next())
				id = rs.getInt("max(Id)") + 1;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	public int deleteEmployeeData(String name) {
		ResultSet rs;
		double result = 0;

		List<EmployeePayrollData> employeeList = new ArrayList<>();
		try (Connection conn = employeePayrollDBService.getConnection()) {
			String sql = String.format("delete from employee_payroll where name=%s;", name);
			Statement stmt = conn.prepareStatement(sql);
			System.out.println(sql);
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return 1;
	}

	public int addMultipleEmployeeData(List<EmployeePayrollData> empList) {

		try (Connection conn = employeePayrollDBService.getConnection()) {
			for (EmployeePayrollData e : empList) {
				int id = getMaxId();
				conn.setAutoCommit(false);
				PreparedStatement stmt = conn
						.prepareStatement("INSERT INTO employee_payroll(name, salary, start,gender) values \r\n"
								+ "			(	?, ?, ?,?	);");
				stmt.setDouble(2, e.getSalary());
				stmt.setString(1, e.getName());
				stmt.setString(3, e.getStartDate().toString());
				stmt.setString(4, e.getGender());
				System.out.println(stmt.toString());
				stmt.executeUpdate();
				stmt = conn.prepareStatement(
						"INSERT INTO payroll(basic_pay, deductions, taxable_pay,tax,net_pay,Id) values \r\n"
								+ "(?, ?, ?,?,?,?);");
				stmt.setDouble(1, e.getSalary());
				stmt.setDouble(2, e.getDeductions());
				stmt.setDouble(3, e.getTaxable_pay());
				stmt.setDouble(4, e.getTax());
				stmt.setDouble(5, e.getNet_pay());
				stmt.setInt(6, id);
				System.out.println(stmt.toString());
				stmt.executeUpdate();
				for (int i = 0; i < e.getDepartment().length; i++) {
					String department[] = e.getDepartment();
					stmt = conn.prepareStatement("INSERT INTO emp_department(Id, department) values \r\n" + "(?, ?);");
					stmt.setInt(1, id);
					stmt.setString(2, department[i]);
					stmt.executeUpdate();
				}
				stmt = conn.prepareStatement("INSERT INTO company(company_Id, Company_Name) values \r\n" + "(?, ?);");
				stmt.setInt(1, id);
				stmt.setString(2, e.getCompany_Name());
				stmt.executeUpdate();
				conn.setAutoCommit(true);
			}

			return 1;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public int addMultipleThreadEmployeeData(EmployeePayrollData e) {

		try (Connection conn = employeePayrollDBService.getConnection()) {
			int id = getMaxId();
			conn.setAutoCommit(false);
			PreparedStatement stmt = conn
					.prepareStatement("INSERT INTO employee_payroll(name, salary, start,gender) values \r\n"
							+ "			(	?, ?, ?,?	);");
			stmt.setDouble(2, e.getSalary());
			stmt.setString(1, e.getName());
			stmt.setString(3, e.getStartDate().toString());
			stmt.setString(4, e.getGender());
			System.out.println(stmt.toString());
			stmt.executeUpdate();
			stmt = conn.prepareStatement(
					"INSERT INTO payroll(basic_pay, deductions, taxable_pay,tax,net_pay,Id) values \r\n"
							+ "(?, ?, ?,?,?,?);");
			stmt.setDouble(1, e.getSalary());
			stmt.setDouble(2, e.getDeductions());
			stmt.setDouble(3, e.getTaxable_pay());
			stmt.setDouble(4, e.getTax());
			stmt.setDouble(5, e.getNet_pay());
			stmt.setInt(6, id);
			System.out.println(stmt.toString());
			stmt.executeUpdate();
			for (int i = 0; i < e.getDepartment().length; i++) {
				String department[] = e.getDepartment();
				stmt = conn.prepareStatement("INSERT INTO emp_department(Id, department) values \r\n" + "(?, ?);");
				stmt.setInt(1, id);
				stmt.setString(2, department[i]);
				stmt.executeUpdate();
			}
			stmt = conn.prepareStatement("INSERT INTO company(company_Id, Company_Name) values \r\n" + "(?, ?);");
			stmt.setInt(1, id);
			stmt.setString(2, e.getCompany_Name());
			stmt.executeUpdate();
			conn.setAutoCommit(true);

			return 1;

		} catch (SQLException m) {
			// TODO Auto-generated catch block
			m.printStackTrace();
		}
		return 0;
	}

}
