package udlmytube;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

public class Serve extends HttpServlet {

	public void htmlPage(Arquivo i, HttpServletResponse res) throws IOException {
		String key = i.getKey();
		String host = i.getHost();
		String port = i.getPort();
		String description = i.getDescription();

		res.setContentType("text/html");
		res.setHeader("key", key);
		res.getWriter().println(key);

		res.setContentType("text/html");
		res.setHeader("description", description);
		res.getWriter().println(description);

		res.setContentType("text/html");
		res.setHeader("host", host);
		res.getWriter().println(host);

		res.setContentType("text/html");
		res.setHeader("port", port);
		res.getWriter().println(port);
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String type = req.getParameter("type");
		String value = req.getParameter("value");

		Query query = pm.newQuery(Arquivo.class);
		List<Arquivo> arquivos = null;
		try {
			arquivos = (List<Arquivo>) query.execute();

		} finally {
			query.closeAll();
		}

		if (type.equals("key")) {
			for (Arquivo i : arquivos) {
				if (i.getKey().equals(value)) {
					htmlPage(i, res);
				}
			}
		} else if (type.equals("description")) {
			for (Arquivo i : arquivos) {
				if (i.getDescription().equals(value)) {
					htmlPage(i, res);
				}
			}
		} else if (type.equals("all")) {
			String output = "";
			for (Arquivo i : arquivos) {
				output = output + i.getKey() + "\n";
			}
			res.setContentType("text/html");
			res.setHeader("data", output);
			res.getWriter().println(output);
		}
	}

}
