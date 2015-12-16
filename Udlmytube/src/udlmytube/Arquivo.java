package udlmytube;

import java.util.Date;
import javax.jdo.annotations.*;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Arquivo {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String key;	
	
	@Persistent
	private String host;

	@Persistent
	private String port;
	
	@Persistent
	private Date date;

	public Arquivo(String key, String host, String port, Date date) {
		this.key = key;
		this.host = host;
		this.port = port;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}
	
	public void setHost(String host) {
		this.host = host;
	}

	public String getHost() {
		return this.host;
	}
	
	public void setPort(String port) {
		this.port = port;
	}

	public String getPort() {
		return this.port;
	}


	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

}