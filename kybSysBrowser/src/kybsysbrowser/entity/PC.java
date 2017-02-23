package kybsysbrowser.entity;

import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.TreeNode;

import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.ModelFactory;

public class PC implements TreeNode {

	private int id = 0;
	private String name = null;
	private String ip = null;
	private String connectionType = null;

	public PC(String name, String ip, String connectionType) throws FileNotFoundException {
		this.id = generateID();
		this.name = name;
		this.ip = ip;
		this.connectionType = connectionType;
	}

	private int generateID() throws FileNotFoundException {
		int biggestID = 0;
		List<Bookmark> bookmarks = DAOFactory.INSTANCE.getBookmarkDao().getBookmarkAll();
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

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public Enumeration<Object> children() {
		return null;
	}

	@Override
	public boolean getAllowsChildren() {
		return false;
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return null;
	}

	@Override
	public int getChildCount() {
		return 0;
	}

	@Override
	public int getIndex(TreeNode node) {
		return -1;
	}

	@Override
	public TreeNode getParent() {
		while (((RootNode) ModelFactory.INSTANCE.getBookmarkTreeModel().getRoot()).children().hasMoreElements()) {
			Bookmark bm = ((RootNode) ModelFactory.INSTANCE.getBookmarkTreeModel().getRoot()).children().nextElement();
			if (bm.getIndex(this) != -1) {
				continue;
			} else {
				return bm;
			}
		}
		return null;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((connectionType == null) ? 0 : connectionType.hashCode());
		result = prime * result + id;
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PC other = (PC) obj;
		if (id != other.id)
			return false;
		if (connectionType == null) {
			if (other.connectionType != null)
				return false;
		} else if (!connectionType.equals(other.connectionType))
			return false;
		if (id != other.id)
			return false;
		if (ip == null) {
			if (other.ip != null)
				return false;
		} else if (!ip.equals(other.ip))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}