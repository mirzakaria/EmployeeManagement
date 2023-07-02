package com.zakaria.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.zakaria.dao.EmployeeDao;
import com.zakaria.model.Employee;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EmployeeServlet
 */
@WebServlet("/")
public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private EmployeeDao dao;
    public EmployeeServlet() {
        dao = new EmployeeDao();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getServletPath();
		
		switch (path) {
		case "/new":
			newForm(request, response);
			break;
			
		case "/insert":
			try {
				insertEmp(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "/delete":
			try {
				deleteEmp(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case "/edit":
			editForm(request, response);
			break;
			
		case "/update":
			try {
				updateEmp(request, response);
			} catch (SQLException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		default:
			try {
				showAllEmployee(request, response);
			} catch (SQLException | ServletException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private void showAllEmployee(HttpServletRequest request, HttpServletResponse response) throws SQLException, ServletException, IOException {
		List<Employee> allEmp = dao.selectAllEmployee();
		request.setAttribute("allEmp", allEmp);
		RequestDispatcher dispatcher = request.getRequestDispatcher("employee-list.jsp");
		dispatcher.forward(request, response);
	}
	
	private void newForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("employee-form.jsp");
		dispatcher.forward(request, response);
	}
	
	private void editForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("empId"));
		
		Employee emp = dao.selectEmpById(id);
		RequestDispatcher dispatcher = request.getRequestDispatcher("employee-form.jsp");
		
		request.setAttribute("employee", emp);
		dispatcher.forward(request, response);
	}
	
	private void insertEmp(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		String name = request.getParameter("name");
		String department = request.getParameter("department");
		int salary = Integer.parseInt(request.getParameter("salary"));
		
		Employee emp = new Employee(name, department, salary);
		
		dao.insertEmployee(emp);
		response.sendRedirect("empList");
	}
	
	private void updateEmp(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("empId"));
		String name = request.getParameter("name");
		String department = request.getParameter("department");
		int salary = Integer.parseInt(request.getParameter("salary"));
		
		Employee emp = new Employee(id, name, department, salary);
		
		dao.updateEmployee(emp);
		response.sendRedirect("empList");
	}
	
	private void deleteEmp(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int id = Integer.parseInt(request.getParameter("empId"));
		
		dao.deleteEmployee(id);
		response.sendRedirect("empList");
		
	}

}
