package kybsysbrowser.entity;

import java.io.FileNotFoundException;
import java.util.Enumeration;

import javax.swing.tree.TreeNode;

import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.ModelFactory;

public class PC implements TreeNode {

	private int id = 0;
	private String name = null;
	private String ip = null;
	private String connectionType = null;

	public PC(String name, String ip, String connectionType) throws FileNotFoundException {
		this.id = DAOFactory.INSTANCE.getPCDAO().getPCCount() + 1;
		System.out.println(DAOFactory.INSTANCE.getPCDAO().getPCCount());
		this.name = name;
		this.ip = ip;
		this.connectionType = connectionType;
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
		while (((RootNode) ModelFactory.INSTANCE.getBookmarkTreeModel()
				.getRoot()).children().hasMoreElements()) {
			Bookmark bm = ((RootNode) ModelFactory.INSTANCE
					.getBookmarkTreeModel().getRoot()).children().nextElement();
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

}