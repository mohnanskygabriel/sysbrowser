package kybsysbrowser.dao;

import java.io.FileNotFoundException;

import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;

public interface PCDAO {

	public void insertPC(PC pc, int parentId) throws FileNotFoundException;

	public void deletePC(PC pc) throws FileNotFoundException;
	
	public void editPC(PC pcNew) throws FileNotFoundException;

	public PC getAllPCOfBookmark(Bookmark bookmark);

	public int getPCCount() throws FileNotFoundException;

}
