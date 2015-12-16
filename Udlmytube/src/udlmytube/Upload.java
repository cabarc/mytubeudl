package udlmytube;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Upload extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		boolean exists = false;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String filename = req.getParameter("key");
		String description = req.getParameter("description");
		String host = req.getParameter("host");
		String port = req.getParameter("port");
		Date date = new Date();
		Arquivo arquivo = new Arquivo(filename, description, host, port, date);

		Query query = pm.newQuery(Arquivo.class);
		List<Arquivo> arquivos = null;
		try {
			arquivos = (List<Arquivo>) query.execute();

		} finally {
			query.closeAll();
		}

		resp.setContentType("text/html");
		for (Arquivo i : arquivos) {
			if ((i.getKey().equals(filename))) {
				exists = true;
				resp.setHeader("key", "FIAL");
				resp.getWriter().println("FIAL");
			}
			if ((i.getDescription().equals(description))) {
				exists = true;
				resp.setHeader("key", "FIAL");
				resp.getWriter().println("FIAL");
			}
		}

		if (!exists) {
			try {
				resp.setHeader("key", "OK");
				resp.getWriter().println("OK");
				pm.makePersistent(arquivo);
			} finally {
			}
		}
		pm.close();
	}
}
