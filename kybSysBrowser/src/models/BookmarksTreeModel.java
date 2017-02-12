package models;

import java.io.IOException;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import entities.Bookmark;
import entities.PC;
import factories.DAOFactory;

public class BookmarksTreeModel implements TreeModel {

	Bookmark invisibleRoot = new Bookmark("invisibleRoot", "invisible bookmark (rootnode) for the tree");

	@Override
	public void addTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getChild(Object parent, int index) {
		Bookmark bm = new Bookmark();
		if (parent.getClass() != bm.getClass())
			return null;

		PC computer = null;
		try {
			computer = DAOFactory.INSTANCE.getBookmarkDao().getBookmark((Bookmark) parent).getComputerList().get(index);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return computer;

	}

	@Override
	public int getChildCount(Object parent) {
		Bookmark bm = new Bookmark();
		if (parent.getClass() == bm.getClass())
			return ((Bookmark) parent).getComputerList().size();

		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child) {
		Bookmark bm = new Bookmark();
		if (parent.getClass() != bm.getClass())
			return -1;

		return ((Bookmark) parent).getComputerList().indexOf(child);
	}

	@Override
	public Object getRoot() {
		return invisibleRoot;
	}

	@Override
	public boolean isLeaf(Object node) {
		Bookmark bm = new Bookmark();
		if (node.getClass() == bm.getClass())
			return false;
		return true;
	}

	@Override
	public void removeTreeModelListener(TreeModelListener l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub

	}

}
