package com.ck.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.function.IntToDoubleFunction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/test" }, initParams = { @WebInitParam(name = "url", value = "jdbc:mysql:///students"),
		@WebInitParam(name = "user", value = "root"),
		@WebInitParam(name = "password", value = "root") }, loadOnStartup = 10)

public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private PreparedStatement prepareStatement = null;

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Driver loaded sucessfully");
	}

	@Override
	public void init() throws ServletException {

		String jdbcurl = getInitParameter("url");
		String user = getInitParameter("user");
		String password = getInitParameter("password");

		try {
			connection = DriverManager.getConnection(jdbcurl, user, password);
			System.out.println("connection established");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("name");
		String age = request.getParameter("age");
		String address = request.getParameter("address");
		String sqlquery = "insert into student(sname,sage,saddress) values(?,?,?)";

		try {
			prepareStatement = connection.prepareStatement(sqlquery);
			if (prepareStatement != null) {
				prepareStatement.setString(1, username);
				prepareStatement.setInt(2, Integer.parseInt(age));
				prepareStatement.setString(3, address);

				if (prepareStatement != null) {
					try {
						int rowAffected = prepareStatement.executeUpdate();
						PrintWriter out = response.getWriter();
						if (rowAffected == 1) {
							out.println("<h1 style='color:green,text-align:center;'>REGISTRATION SUCESSFUL</h1> ");
						} else {
							out.println("<h1>Registration failed try again with following link</h1>");
							out.println("<a href='./reg.html'/>REGISTRATION</a>");
						}
						out.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}

	}
	
	@Override
	public void destroy() {
		if(connection!=null)
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
	}

}
