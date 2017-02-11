package entities;

public class PC {
	String name = null;
	public String connectionType = null;
	public String ip = null;

	public PC(String name, String ip, String connectionType, String userName,
			String password) {
		this.name = name;
		this.ip = ip;
		this.connectionType = connectionType;
	}

	public PC() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}