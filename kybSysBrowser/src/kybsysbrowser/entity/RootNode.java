package kybsysbrowser.entity;

import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import kybsysbrowser.factory.DAOFactory;

public class RootNode implements TreeNode {

	@Override
	public TreeNode getChildAt(int childIndex) {
		try {
			return DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll()
					.get(childIndex);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getChildCount() {
		try {
			return DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll().size();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public TreeNode getParent() {
		return null;
	}

	@Override
	public int getIndex(TreeNode node) {
		List<Bookmark> list = new LinkedList<Bookmark>();
		try {
			list = DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		List<Bookmark> list = new LinkedList<Bookmark>();
		try {
			list = DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Vector<Bookmark>(list).elements();
	}

	@Override
	public String toString() {
		return "RootNode";
	}

}
