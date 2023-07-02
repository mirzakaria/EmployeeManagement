package com.zakaria.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.zakaria.model.Employee;

public class EmployeeDao {
	
	private String url = "jdbc:mysql://localhost:3306/Employee";
	private String username = "root";
	private String password = "zakaria";
	
	private static final String INSERT_EMP = "insert into employee (name, department, salary) values (?, ?, ?)";
	private static final String SELECT_ALL_EMP = "select * from employee";
	private static final String SELECT_BY_ID = "select * from employee where empId = ?";
	private static final String EDIT_EMP = "update employee set name = ?, department = ?, salary = ? where empId = ?";
	private static final String DELETE_EMP = "delete from employee where empId = ?";
	
	
	protected Connection getConnection() {
		Connection con = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return con;
	}
	
	//Insert Function
	public void insertEmployee(Employee emp) throws SQLException {
		try (Connection connection = getConnection();
			PreparedStatement preStatement = connection.prepareStatement(INSERT_EMP);) {
			preStatement.setString(1, emp.getName());
			preStatement.setString(2, emp.getDepartment());
			preStatement.setInt(3, emp.getSalary());
			
			preStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean updateEmployee(Employee emp) throws SQLException {
		boolean isUpdated;
		
		try (Connection connection = getConnection();
				PreparedStatement preStatement = connection.prepareStatement(EDIT_EMP);) {
				preStatement.setString(1, emp.getName());
				preStatement.setString(2, emp.getDepartment());
				preStatement.setInt(3, emp.getSalary());
				preStatement.setInt(4, emp.getEmpId());
				
				isUpdated = preStatement.executeUpdate() > 0;
			}
		return isUpdated;
	}
	
	public boolean deleteEmployee(int empId) throws SQLException {
		boolean isDeleted;
		
		try(Connection connection = getConnection();
				PreparedStatement preStatement = connection.prepareStatement(DELETE_EMP);) {
				preStatement.setInt(1, empId);
				isDeleted = preStatement.executeUpdate() > 0;
		}
		return isDeleted;
	}
	
	public List<Employee> selectAllEmployee() throws SQLException {
		List<Employee> employees = new ArrayList<>();
		
		try(Connection connection = getConnection();
				PreparedStatement preStatement = connection.prepareStatement(SELECT_ALL_EMP);) {
				ResultSet rs = preStatement.executeQuery();
				
				while (rs.next()) {
					int eid = rs.getInt("empId");
					String name = rs.getString("name");
					String dept = rs.getString("department");
					int salary = rs.getInt("salary");
					
					employees.add(new Employee(eid, name, dept, salary));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return employees;
	}
	
	public Employee selectEmpById(int eid) {
		Employee emp = null;
		
		try(Connection connection = getConnection();
				PreparedStatement preStatement = connection.prepareStatement(SELECT_BY_ID);) {
				preStatement.setInt(1, eid);
				ResultSet rs = preStatement.executeQuery();
				
				while (rs.next()) {
					int id = rs.getInt("empId");
					String name = rs.getString("name");
					String dept = rs.getString("department");
					int salary = rs.getInt("salary");
					
					emp = new Employee(id, name, dept, salary);
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return emp;
	}
}
