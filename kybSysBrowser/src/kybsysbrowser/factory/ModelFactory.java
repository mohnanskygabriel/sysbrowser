package kybsysbrowser.factory;

import kybsysbrowser.entity.RootNode;
import kybsysbrowser.model.BookmarkTreeModel;

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
