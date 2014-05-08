package net.azib.photos;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Source;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.GphotoEntry;

public class RequestRouter implements Filter {
    public void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String path = request.getServletPath();
        
        if (path.toLowerCase().startsWith("/image")){
        	chain.doFilter(req, resp);
        }
        
        Picasa picasa = new Picasa(request.getParameter("by"), request.getParameter("authkey"));
        request.setAttribute("picasa", picasa);
        request.setAttribute("host", request.getHeader("host"));
        
        if (request.getParameter("random") != null) {
            render("random", picasa.getRandomPhoto(), request, response);
        }
        else if (path == null || "/".equals(path) || "gallery".equalsIgnoreCase(path)) {
            render("gallery", picasa.getGallery(), request, response);
        }
        else if (path.lastIndexOf('.') >= path.length() - 4) {
            chain.doFilter(req, resp);
        }
        else {
            String[] parts = path.split("/");
            AlbumFeed album = picasa.getAlbum(parts[1]);
            if (parts.length > 2) {
                for (GphotoEntry photo : album.getPhotoEntries()) {
                    if (photo.getGphotoId().equals(parts[2])) {
                        request.setAttribute("photo", photo);
                        break;
                    }
                }
            }
            request.setAttribute("comments", picasa.getAlbumComments(parts[1]));
            render("album", album, request, response);
        }
    }

    void render(String template, Object source, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute(template, source);

        response.setContentType("text/html; charset=utf8");
        if (source instanceof Source)
            response.addDateHeader("Last-Modified", ((Source)source).getUpdated().getValue());
        if (source instanceof BaseFeed)
            response.addHeader("ETag", ((BaseFeed)source).getEtag());

        request.getRequestDispatcher("/WEB-INF/jsp/" + template + ".jsp").include(request, response);
    }

    public void destroy() {
    }
    public static void main(String[] args) {
		String path="/08";
		 String[] parts = path.split("/");
		 System.out.println(parts.length);
		 System.out.println(parts[0]);
		 System.out.println(parts[1]);
	}
}
