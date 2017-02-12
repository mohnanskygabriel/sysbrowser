package entities;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.tree.TreeNode;

import factories.ModelFactory;

public class Bookmark implements TreeNode {
	private String name = null;
	private String url = null;
	private LinkedList<PC> computerList = null;

	public Bookmark(String name, String url) {
		this.name = name;
		this.url = url;
		this.computerList = new LinkedList<PC>();
	}

	public Bookmark() {

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

	public void setComputerList(LinkedList<PC> computerList) {
		this.computerList = computerList;
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
		return (PC) computerList.get(childIndex);
	}

	@Override
	public int getChildCount() {
		return computerList.size();
	}

	@Override
	public int getIndex(TreeNode node) {
		return computerList.indexOf((PC)node);
	}

	@Override
	public TreeNode getParent() {
		return (Bookmark) ModelFactory.INSTANCE.getBookmarksTreeModel().getRoot();
	}

	@Override
	public boolean isLeaf() {
		return false;
	}

}