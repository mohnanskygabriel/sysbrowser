package factories;

import dao.implementations.FileBookmarkDAO;
import dao.implementations.FilePCDAO;
import dao.interfaces.BookmarkDAO;
import dao.interfaces.PCDAO;

public enum DAOFactory {
	INSTANCE;
	PCDAO pcDao;
	BookmarkDAO bookmarkDao;

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
}
