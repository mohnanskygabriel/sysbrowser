package models;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import dao.interfaces.BookmarkDAO;
import factories.DAOFactory;

public class BookmarksTreeModel implements TreeModel {

	BookmarkDAO BookmarkDAO = DAOFactory.INSTANCE.getBookmarkDao();

	@Override
	public Object getRoot() {
		return BookmarkDAO.getBookmarkAll().get(0);
	}

	@Override
	public Object getChild(Object parent, int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getChildCount(Object parent) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isLeaf(Object node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}

}
