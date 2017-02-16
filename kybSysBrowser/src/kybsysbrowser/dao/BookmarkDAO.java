package kybsysbrowser.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import kybsysbrowser.entity.Bookmark;

public interface BookmarkDAO {

	public void insertBookmark(Bookmark bookmark) throws FileNotFoundException;

	public void deleteBookmark(Bookmark bookmark) throws FileNotFoundException;

	public void editBookmark(Bookmark oldBookmark, Bookmark newBookmark);

	public List<Bookmark> getBookmarkAll() throws FileNotFoundException;

	Bookmark getBookmark(Bookmark bookmark) throws FileNotFoundException,
			IOException;

}
