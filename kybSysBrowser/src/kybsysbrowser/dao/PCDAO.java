package kybsysbrowser.dao;

import java.io.FileNotFoundException;

import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;

public interface PCDAO {

	public void insertPC(PC pc, int parentId) throws FileNotFoundException;

	public void deletePC(PC pc) throws FileNotFoundException;
	
	public void editPC(PC pcOld, PC pcNew);

	public PC getAllPCOfBookmark(Bookmark bookmark);

}
