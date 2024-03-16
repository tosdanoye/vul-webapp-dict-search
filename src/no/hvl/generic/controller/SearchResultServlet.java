package no.hvl.generic.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import no.hvl.generic.database.SearchItem;
import no.hvl.generic.database.SearchItemDAO;
import no.hvl.generic.dictionary.DictionaryDAO;
import no.hvl.generic.util.Validator;

@WebServlet("/dosearch")
public class SearchResultServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (RequestHelper.isLoggedIn(request)) {

			String dicturl = RequestHelper.getCookieValue(request, "dicturl");

			String user = Validator.validString(request.getParameter("user"));
			String searchkey = Validator.validString(request
					.getParameter("searchkey"));

			Timestamp datetime = new Timestamp(new Date().getTime());
			SearchItem search = new SearchItem(datetime, user, searchkey);
			
			SearchItemDAO searchItemDAO = new SearchItemDAO();
			searchItemDAO.saveSearch(search);
			DictionaryDAO dict = new DictionaryDAO(dicturl);

			List<String> foundEntries = new ArrayList<String>();
			try {
				String fpath = getServletContext().getRealPath("/v003/");
				foundEntries = dict.findEntries(fpath, searchkey);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			request.setAttribute("searchkey", searchkey);
			request.setAttribute("result", foundEntries);
			request.getRequestDispatcher("searchresult.jsp").forward(request,
					response);
		} else {
			request.getSession().invalidate();
			request.getRequestDispatcher("index.jsp").forward(request,
					response);
		}
	}

}
