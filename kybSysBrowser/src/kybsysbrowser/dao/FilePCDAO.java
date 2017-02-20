package kybsysbrowser.dao;

import java.io.FileNotFoundException;

import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;
import kybsysbrowser.factory.DAOFactory;

public class FilePCDAO implements PCDAO {

	@Override
	public void insertPC(PC pc, int parentId) throws FileNotFoundException {
		Bookmark newBookmark = DAOFactory.INSTANCE.getBookmarkDao()
				.getBookmarkById(parentId);
		newBookmark.addPC(pc);
		DAOFactory.INSTANCE.getBookmarkDao().editBookmark(newBookmark);
	}

	@Override
	public void deletePC(PC pc) throws FileNotFoundException {
		Bookmark bookmark = DAOFactory.INSTANCE.getBookmarkDao()
				.getBookmarkContainingPC(pc);
		bookmark.getComputerList().remove(pc);
		DAOFactory.INSTANCE.getBookmarkDao().editBookmark(bookmark);
	}

	@Override
	public void editPC(PC pcOld, PC pcNew) {

	}

	@Override
	public PC getAllPCOfBookmark(Bookmark bookmark) {

		return null;
	}

}
