package dao.interfaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import entities.Bookmark;

public interface BookmarkDAO {

	public void insertBookmark(Bookmark bookmark) throws FileNotFoundException,
			IOException;

	public void deleteBookmark(Bookmark bookmark) throws FileNotFoundException,
			IOException;

	public void editBookmark(Bookmark oldBookmark, Bookmark newBookmark)
			throws FileNotFoundException, IOException;

	public Bookmark getBookmark(Bookmark bookmark)
			throws FileNotFoundException, IOException;

	public List<Bookmark> getBookmarkAll();

}
