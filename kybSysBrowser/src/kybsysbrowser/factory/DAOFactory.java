package kybsysbrowser.factory;

import kybsysbrowser.dao.BookmarkDAO;
import kybsysbrowser.dao.FileBookmarkDAO;
import kybsysbrowser.dao.FilePCDAO;
import kybsysbrowser.dao.FileSettingDAO;
import kybsysbrowser.dao.PCDAO;
import kybsysbrowser.dao.SettingDAO;

public enum DAOFactory {
	INSTANCE;
	PCDAO pcDao;
	BookmarkDAO bookmarkDao;
	SettingDAO settingDAO;

	public PCDAO getPCDAO() {
		if (pcDao == null)
			pcDao = new FilePCDAO();
		return pcDao;
	}

	public BookmarkDAO getBookmarkDao() {
		if (bookmarkDao == null)
			bookmarkDao = new FileBookmarkDAO();
		return bookmarkDao;
	}

	public SettingDAO getSettingDAO() {
		if (settingDAO == null)
			settingDAO = new FileSettingDAO();
		return settingDAO;
	}
}
