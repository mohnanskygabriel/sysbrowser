package kybsysbrowser.dao;

import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;

public interface PCDAO {

	public void insertPC(PC pc, int parentId);

	public void deletePC(PC pc);
	
	public void editPC(PC pcNew);

	public PC getAllPCOfBookmark(Bookmark bookmark);

	public int getPCCount();

}
