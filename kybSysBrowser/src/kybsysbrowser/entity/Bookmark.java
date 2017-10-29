package kybsysbrowser.entity;

import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.ModelFactory;

public class Bookmark implements TreeNode {
	private int id = 0;
	private String name = null;
	private String url = null;
	private LinkedList<PC> computerList = null;

	public Bookmark(String name, String url) throws FileNotFoundException {
		this.id = generateId();
		this.name = name;
		this.url = url;
		this.computerList = new LinkedList<PC>();
	}

	private int generateId() throws FileNotFoundException {
		int biggestID = 0;
		List<Bookmark> bookmarks = DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll();
		if (bookmarks.size() == 0)
			return 0;
		for (Bookmark bookmark : bookmarks) {
			if (bookmark.getId() > biggestID)
				biggestID = bookmark.getId();
			List<PC> computerList = bookmark.getComputerList();
			for (PC pc : computerList) {
				if (pc.getId() > biggestID)
					biggestID = pc.getId();
			}
		}
		return biggestID + 1;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LinkedList<PC> getComputerList() {
		return computerList;
	}

	public void addPC(PC newPC) {
		this.computerList.add(newPC);
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public Enumeration<PC> children() {
		return new Vector<PC>(computerList).elements();
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return computerList.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return computerList.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return computerList.indexOf((PC) node);
	}

	@Override
	public TreeNode getParent() {
		return (TreeNode) ModelFactory.INSTANCE.getBookmarkTreeModel().getRoot();
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bookmark other = (Bookmark) obj;
		if (id != other.id)
			return false;
		return true;
	}

}