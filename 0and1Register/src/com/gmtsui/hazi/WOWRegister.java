package com.gmtsui.hazi;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/WOWRegister" })
public class WOWRegister extends HttpServlet {

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * Constructor of the object.
	 */
	public WOWRegister() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("utf-8");
		String user = request.getParameter("user").toUpperCase();
		String pass = request.getParameter("pass").toUpperCase();
		try {
			pass = encode("SHA1", user + ":" + pass);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			response.getWriter().write("�û����Ƿ�");
			return;
		}
		try {
			// ����MySql��������
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("�Ҳ������������� ����������ʧ�ܣ�");
			e.printStackTrace();
			response.getWriter().write("�����������⣡");
			return;
		}
		String url = "jdbc:mysql://localhost:3306/realmd";
		StringBuilder sb = new StringBuilder(
				"INSERT INTO `account` (`username`,`sha_pass_hash`,`gmlevel`) VALUES ('");
		sb.append(user).append("','").append(pass).append("',0);");
		String username = "mangos";
		String password = "mangos";
		Connection con = null;
		Statement stmt = null;
		try {
			con = DriverManager.getConnection(url, username, password);
			stmt = con.createStatement();
			int i = stmt.executeUpdate(sb.toString());
			if (1 != i) {
				throw new SQLException();
			}

		} catch (SQLException se) {
			response.getWriter().write("�û����ѱ�ע�ᣡ");
			return;
		} finally {
			if (null != stmt) {
				try {
					stmt.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (null != con) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		response.getWriter().write("ע��ɹ���");

	}

	private static String encode(String algorithm, String str)
			throws NoSuchAlgorithmException {
		if (str == null) {
			return null;
		}
		MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
		messageDigest.update(str.getBytes());
		return getFormattedText(messageDigest.digest());
	}

	private static String getFormattedText(byte[] bytes) {
		int len = bytes.length;
		StringBuilder buf = new StringBuilder(len * 2);
		// ������ת����ʮ�����Ƶ��ַ�����ʽ
		for (int j = 0; j < len; j++) {
			buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
			buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
		}
		return buf.toString();
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
