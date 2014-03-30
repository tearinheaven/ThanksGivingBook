package com.thanksgiving.data;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.thanksgiving.entity.User;
import com.thanksgiving.service.IUserDs;
import com.thanksgiving.service.bo.UserBO;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//System.out.println("dddf");
		//response.sendRedirect(request.getContextPath() + "/index.jsp");
		
		WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
		IUserDs ds = (IUserDs)wac.getBean("userDs");
		
		String operation = request.getParameter("operation");
		if ("signUp".equals(operation))
		{
			User user = new User();
			user.setUserEmail(request.getParameter("userEmail"));
			user.setUserPassword(request.getParameter("userPassword"));
			UserBO userBO = new UserBO();
			userBO.setUser(user);

			userBO = ds.addUser(userBO);

			JSONObject rs = JSONObject.fromObject(userBO);
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.getWriter().print(rs);
		}
		
		//request.getRequestDispatcher("index.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
