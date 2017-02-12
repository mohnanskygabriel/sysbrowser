package factories;

import models.BookmarksTreeModel;

public enum ModelFactory {
	INSTANCE;
	BookmarksTreeModel bookmarksTreeModel;

	public BookmarksTreeModel getBookmarksTreeModel() {
		if (bookmarksTreeModel == null)
			bookmarksTreeModel = new BookmarksTreeModel();
		return bookmarksTreeModel;
	}
}
