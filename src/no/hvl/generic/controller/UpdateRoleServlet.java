package no.hvl.generic.controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.hvl.generic.database.AppUser;
import no.hvl.generic.database.AppUserDAO;
import no.hvl.generic.util.Role;
import no.hvl.generic.util.Validator;

@WebServlet("/updaterole")
public class UpdateRoleServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		// check that the user has a valid session
		if(RequestHelper.isLoggedIn(request))
			request.getRequestDispatcher("updaterole.jsp").forward(request, response);
		else {
			request.setAttribute("message", "Session has expired. Login again!");
			request.getRequestDispatcher("login").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		request.removeAttribute("message");

		boolean successfulRoleUpdate = false;
		
		String username = Validator.validString(request
				.getParameter("username"));
		String newrole = Validator.validString(request
				.getParameter("role"));
		
		AppUser user = (AppUser) request.getSession().getAttribute("user");
		
		if(username != null) {
			
			if (RequestHelper.isLoggedIn(request) && user.getRole().equals(Role.ADMIN.toString())) {
				
				AppUserDAO userDAO = new AppUserDAO();
				
				successfulRoleUpdate = userDAO.updateUserRole(username, newrole);
				
				if (successfulRoleUpdate) {
					
					response.sendRedirect("mydetails");

				} else {
					request.setAttribute("message", "Role update for "+username+" failed!");
					request.getRequestDispatcher("updaterole.jsp").forward(request,
							response);
				}
				
			} else {
				request.getSession().invalidate();
				request.setAttribute("message", "You are not authorized to perform this action!");
				request.getRequestDispatcher("login.jsp").forward(request,
						response);
			}
		}


	}

}
