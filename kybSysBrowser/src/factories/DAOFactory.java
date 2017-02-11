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
		if (pcDao != null)
			return pcDao;
		return pcDao = new FilePCDAO();
	}

	public BookmarkDAO getBookmarkDao() {
		if (bookmarkDao != null)
			return bookmarkDao;
		return bookmarkDao = new FileBookmarkDAO();
	}

}
