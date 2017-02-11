package entities;

import java.util.LinkedList;

public class Bookmark {

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

}