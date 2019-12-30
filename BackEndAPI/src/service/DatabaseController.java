package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import util.Connector;

/**
 * Servlet implementation class DatabaseController
 */
@WebServlet("/Database")
public class DatabaseController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DatabaseController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//Digunakan untuk mengubah content menjadi tipe JSON
		response.setContentType("application/json");
		
		//Pengecekan Header kosong dan melakukan enkripsi data dari database yang digunakan sebagai token 
		if(request.getHeader("header") != null) {
			Connector conn = new Connector();
			Connection con = conn.getConnection();
			
			try {
				Statement stmt = con.createStatement();
				String query = "SELECT employee_id FROM copy_emp WHERE last_name = '"+request.getHeader("header")+"'";
				System.out.println(query);
				ResultSet rs = stmt.executeQuery(query);
				rs.next();
				String result = rs.getString(1);
				
				//Cara melakukan enkripsi
				StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
				encryptor.setPassword("header");
				String encrypted= encryptor.encrypt(result);
				
				PrintWriter out = response.getWriter();
				out.write(encrypted);
				
				System.out.println(result);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Header Salah");
		}
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("application/json");
		
		//Pengecekan Header kosong dan decrypt data untuk bisa dilakukan aksi selanjutnya
		if(request.getHeader("token") != null) {
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("header");
			String decrypted = encryptor.decrypt(request.getHeader("token"));
			
			if(authenticated(decrypted)) {
				//Aksi Selanjutnya bisa dibuat disini
				PrintWriter out = response.getWriter();
				out.print("OK");
			}
		}else {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Token Salah");
		}
	}
	
	//fungsi untuk autentikasi
	private boolean authenticated (String key) {
		boolean result = false;
		Connector conn = new Connector();
		Connection con = conn.getConnection();
		
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT COUNT(1) FROM copy_emp WHERE employee_id = "+key;
			ResultSet rs = stmt.executeQuery(query);
			rs.next();
			int n = rs.getRow();
			if(n == 1) {
				result = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
