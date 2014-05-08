import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.FilterMapping;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


public class Startup {
	//net.azib.photos.RequestRouter
	public static void main(String[] args) throws Exception {
		
//		Server server = new Server(Integer.valueOf(System.getenv("PORT")));
		Server server = new Server(28888);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        
        //context.addServlet(new ServletHolder(new HiServlet()),"/*");
        context.addFilter(new FilterHolder(new net.azib.photos.RequestRouter()), "/*", FilterMapping.REQUEST);
        context.addServlet(new ServletHolder(new net.azib.photos.SiteMapServlet()), "/robots.txt");
        context.addServlet(new ServletHolder(new net.azib.photos.SiteMapServlet()), "/sitemap.xml");
        context.addServlet(new ServletHolder(new net.azib.photos.JsonpServlet()), "/data/gallery.js");
        server.start();
        server.join();
	}

}
