import java.io.*;
import java.net.*;

public class Manip {

	public String enviarArquivo(Arquivo arquivo) throws UnsupportedEncodingException {
		try {
			URL url = new URL("http://mytubeudl.appspot.com/upload");
			//URL url = new URL("http://localhost:8888/upload");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write("key=" + arquivo.key + "&description=" + arquivo.description +"&host=" + arquivo.host + "&port=" + arquivo.port);
			writer.close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String key = connection.getHeaderField("key");
				if (key.equals("FIAL")) {
					return "FIAL";
				}
				return "OK";
			}
		} catch (MalformedURLException e) {
			// ...
		} catch (IOException e) {
			// ...
		}
		return "FIAL";
	}

	public String receberArquivo(String type, String desc) throws UnsupportedEncodingException {
		Arquivo arquivos;
		try {
			URL url = new URL("http://mytubeudl.appspot.com/serve?type="+type+"&value="+desc);
			//URL url = new URL("http://localhost:8888/serve?key=" + filename);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			String key = connection.getHeaderField("key");
			String description = connection.getHeaderField("description");
			String host = connection.getHeaderField("host");
			String port = connection.getHeaderField("port");
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				if (key != null) {
					return key + ";" + description + ";" + host + ";" + port;
				}
			}
		} catch (MalformedURLException e) {
			// ...
		} catch (IOException e) {
			// ...
		}
		return "FIAL";
	}

	public String receberAll() throws UnsupportedEncodingException {
		Arquivo arquivos;
		try {
			URL url = new URL("http://mytubeudl.appspot.com/serve?type=all&value=Y");
			//URL url = new URL("http://localhost:8888/serve?key=" + filename);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			String data = connection.getHeaderField("data");
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				if (data != null) {
					return data;
				}
			}
		} catch (MalformedURLException e) {
			// ...
		} catch (IOException e) {
			// ...
		}
		return "FIAL";
	}
}
