package kybsysbrowser.dao;

public interface SettingDAO {

	public void insertSetting(String name, String setting);

	public String getSetting(String name);

	public void editSetting(String name, String newSetting);

}
