package com.footstone.photos;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ImageViewServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(ImageViewServlet.class.getName());
	
	/**
     * @see HttpServlet#HttpServlet()
     */
    public ImageViewServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init(ServletConfig config) throws ServletException {
    	
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
		String key = request.getParameter("k");
		if (key==null || ("").equals(key)){
			return;
		}
		key = key.trim();
		byte[] data = null;
		try {
			data = ConfigFactory.getCache().get(key);
		} catch (Exception e) {
			log.severe("read photo cache faild. key="+key+" "+e.getMessage());
			e.printStackTrace();
		}
		if (data == null){
			String url = PhotoURLCache.get(key);
			//TODO
			if (url != null){
				data = Util.getBytesFromUrl(url);
				try {
					ConfigFactory.getCache().save(key, data);
				} catch (Exception e) {
					log.severe("save photo cache faild. key="+key+" "+e.getMessage());
					e.printStackTrace();
				}
			}
		}
		if (data != null){
			BufferedOutputStream bout = new BufferedOutputStream(response.getOutputStream());
			bout.write(data);
			bout.flush();
			bout.close();
		}
	}

}
