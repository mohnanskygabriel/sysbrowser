package kybsysbrowser.entity;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import kybsysbrowser.factory.DAOFactory;

public class RootNode implements TreeNode {

	@Override
	public TreeNode getChildAt(int childIndex) {
		return DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll().get(childIndex);

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
		return DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll().indexOf(node);
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
		return new Vector<Bookmark>(DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll()).elements();
	}

	@Override
	public String toString() {
		return "RootNode";
	}

}
