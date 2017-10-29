package kybsysbrowser.dao;

import java.util.List;

import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;

public interface BookmarkDAO {

	public void insertBookmark(Bookmark bookmark);

	public void deleteBookmark(Bookmark bookmark);

	public void editBookmark(Bookmark newBookmark);

	public List<Bookmark> getBookmarkAll();

	public Bookmark getBookmark(Bookmark bookmark);

	public Bookmark getBookmarkById(int bookmarkId);

	public Bookmark getBookmarkContainingPC(PC pc);

}
