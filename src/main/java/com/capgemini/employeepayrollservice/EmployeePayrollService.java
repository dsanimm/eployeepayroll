package com.capgemini.employeepayrollservice;
import java.util.List;

import com.capgemini.pojo.*;
public class EmployeePayrollService {

	public List<EmployeePayrollData> reademployeePayrollData() {
		return EmployeePayrollDBService.readData();
	
	}

}
