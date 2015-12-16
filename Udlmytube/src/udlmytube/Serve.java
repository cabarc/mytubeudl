package udlmytube;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

public class Serve extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String key = req.getParameter("key");

		Query query = pm.newQuery(Arquivo.class);
		List<Arquivo> arquivos = null;
		try {
			arquivos = (List<Arquivo>) query.execute();

		} finally {
			query.closeAll();
		}
		
		for(Arquivo i : arquivos){
			if(i.getKey().equals(key)){
				String host = i.getHost();
				String port = i.getPort();
				
				res.setContentType("text/html");
				res.setHeader("key", key);
				res.getWriter().println(key);

				res.setContentType("text/html");
				res.setHeader("host", host);
				res.getWriter().println(host);

				res.setContentType("text/html");
				res.setHeader("port", port);
				res.getWriter().println(port);
			}
		}
	}
}