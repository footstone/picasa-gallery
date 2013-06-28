package com.footstone.photos;

import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends HttpServlet {
	
	private static String t6 = "https://lh6.googleusercontent.com/-J5UfjX7P9bo/Ub1FB0IadrE/AAAAAAAACi8/om5MdSAoDg0/s212-c/201303.jpg";
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public TestServlet() {
        super();
    }
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key = request.getParameter("key");
		byte[] data = null;
		try {
			data = Util.getBytesFromUrl(t6);
			ConfigFactory.getCache().save("test-001", data);
			//data = ConfigFactory.getCache().get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			data = ConfigFactory.getCache().get("test-001");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (data != null){
			BufferedOutputStream bout = new BufferedOutputStream(response.getOutputStream());
			bout.write(data);
			bout.flush();
			bout.close();
		}
	}

}
