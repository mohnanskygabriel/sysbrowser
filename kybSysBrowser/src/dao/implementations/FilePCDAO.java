package dao.implementations;

import java.io.FileNotFoundException;

import dao.interfaces.PCDAO;
import entities.Bookmark;
import entities.PC;

public class FilePCDAO implements PCDAO {

	@Override
	public void insertPC(PC pc) {

	}

	@Override
	public void deletePC(PC pc) throws FileNotFoundException {
		
	}

	@Override
	public void editPC(PC pcOld, PC pcNew) {
		
	}

	@Override
	public PC getAllPCOfBookmark(Bookmark bookmark) {
		
		return null;
	}

}
