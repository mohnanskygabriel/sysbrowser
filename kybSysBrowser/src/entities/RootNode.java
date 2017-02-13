package entities;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import factories.DAOFactory;

public class RootNode implements TreeNode {

	@Override
	public TreeNode getChildAt(int childIndex) {
		return DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll()
				.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll().size();
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		List<Bookmark> list = DAOFactory.INSTANCE.getBookmarkDao()
				.getBookmarkAll();
		return list.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public Enumeration<Bookmark> children() {
		List<Bookmark> list = DAOFactory.INSTANCE.getBookmarkDao()
				.getBookmarkAll();
		return new Vector<Bookmark>(list).elements();
	}

	@Override
	public String toString() {
		return "RootNode";
	}

}
