package kybsysbrowser.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;
import kybsysbrowser.factory.DAOFactory;

public class FilePCDAO implements PCDAO {

	@Override
	public void insertPC(PC pc, int parentId) throws FileNotFoundException {
		Bookmark newBookmark = DAOFactory.INSTANCE.getBookmarkDao().getBookmarkById(parentId);
		newBookmark.addPC(pc);
		DAOFactory.INSTANCE.getBookmarkDao().editBookmark(newBookmark);
	}

	@Override
	public void deletePC(PC pc) throws FileNotFoundException {
		Bookmark bookmark = DAOFactory.INSTANCE.getBookmarkDao().getBookmarkContainingPC(pc);
		bookmark.getComputerList().remove(pc);
		DAOFactory.INSTANCE.getBookmarkDao().editBookmark(bookmark);
	}

	@Override
	public void editPC(PC pcNew) throws FileNotFoundException {
		List<Bookmark> bmList = DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll();
		deleteFileOfBookmarksAndCreateNew();
		for (Bookmark bookmark : bmList) {
			LinkedList<PC> pcList = bookmark.getComputerList();
			for (PC pc : pcList) {
				if (pc.getId() == pcNew.getId()) {
					pc.setName(pcNew.getName());
					pc.setIp(pcNew.getIp());
					pc.setConnectionType(pcNew.getConnectionType());
					break;
				}
			}
			DAOFactory.INSTANCE.getBookmarkDao().insertBookmark(bookmark);
		}
	}

	@Override
	public PC getAllPCOfBookmark(Bookmark bookmark) {

		return null;
	}

	@Override
	public int getPCCount() throws FileNotFoundException {
		List<Bookmark> bookmarks = DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll();

		int count = 0;
		for (Bookmark bookmark : bookmarks) {
			count = count + bookmark.getComputerList().size();
		}
		return count;
	}

	public File getFileOfBookmarks() {
		return new File(DAOFactory.INSTANCE.getSettingDAO().getSetting("File of bookmarks"));
	}

	private void deleteFileOfBookmarksAndCreateNew() {
		boolean deleted = false;
		for (int i = 0; i < 20; i++) {
			deleted = getFileOfBookmarks().delete();
			if (deleted)
				break;
			System.gc();
			Thread.yield();
		}
		try {
			getFileOfBookmarks().createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
