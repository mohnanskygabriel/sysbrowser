package kybsysbrowser.dao;

import java.io.FileNotFoundException;
import java.util.List;

import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;

public interface BookmarkDAO {

	public void insertBookmark(Bookmark bookmark) throws FileNotFoundException;

	public void deleteBookmark(Bookmark bookmark) throws FileNotFoundException;

	public void editBookmark(Bookmark newBookmark) throws FileNotFoundException;

	public List<Bookmark> getBookmarkAll() throws FileNotFoundException;

	public Bookmark getBookmark(Bookmark bookmark) throws FileNotFoundException;

	public Bookmark getBookmarkById(int bookmarkId) throws FileNotFoundException;

	public Bookmark getBookmarkContainingPC(PC pc);

}
