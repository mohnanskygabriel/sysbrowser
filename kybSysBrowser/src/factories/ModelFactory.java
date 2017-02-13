package factories;

import models.BookmarkTreeModel;
import entities.RootNode;

public enum ModelFactory {
	INSTANCE;
	RootNode rootNode;
	BookmarkTreeModel bookmarkTreeModel;

	public BookmarkTreeModel getBookmarkTreeModel() {
		if (rootNode == null)
			rootNode = new RootNode();
		if (bookmarkTreeModel == null)
			bookmarkTreeModel = new BookmarkTreeModel(rootNode);
		return bookmarkTreeModel;
	}
}
