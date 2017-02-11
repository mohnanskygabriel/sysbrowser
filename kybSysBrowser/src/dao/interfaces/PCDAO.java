package dao.interfaces;

import java.io.IOException;

import entities.Bookmark;
import entities.PC;

public interface PCDAO {

	public void insertPC(PC pc);

	public void deletePC(PC pc) throws IOException;
	
	public void editPC(PC pcOld, PC pcNew);

	public PC getAllPCOfBookmark(Bookmark bookmark);

}
