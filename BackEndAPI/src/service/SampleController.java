package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SampleController
 */
@WebServlet("/Sample")
public class SampleController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SampleController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Map<String,String> result = new HashMap<String, String>();
		result.put("kunci-1", "nilai 1");
		result.put("kunci-2", "nilai 2");
		
		response.setContentType("application/json");
		PrintWriter print = response.getWriter();
		print.write(result.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		if("secret".equals(request.getHeader("token"))){
			PrintWriter print = response.getWriter();
			print.write("OK");
		}else {
			response.setContentType("application/json");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Salah Token");
			return;
		}
	}

}
