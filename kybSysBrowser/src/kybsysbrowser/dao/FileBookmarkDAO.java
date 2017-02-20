package kybsysbrowser.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import kybsysbrowser.dialog.exceptionSolving.IOExceptionFileWriterErrorDialog;
import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;
import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.WindowFactory;

import org.eclipse.swt.SWT;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class FileBookmarkDAO implements BookmarkDAO {

	@Override
	public void insertBookmark(Bookmark bookmark) throws FileNotFoundException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try {
			FileWriter fileW = new FileWriter(getFileOfBookmarks(), true);
			JsonWriter writer = gson.newJsonWriter(fileW);
			writer.jsonValue(gson.toJson(bookmark));
			writer.close();
		} catch (IOException ioEx) {
			popupIOException(ioEx);
		}

	}

	@Override
	public void deleteBookmark(Bookmark bookmark) throws FileNotFoundException {
		List<Bookmark> bookmarkList = new LinkedList<Bookmark>();
		FileReader reader = new FileReader(getFileOfBookmarks());
		Gson gson = new GsonBuilder().create();
		JsonReader jReader = new JsonReader(reader);
		jReader.setLenient(true);
		try {
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT) {
					break;
				} else {
					bookmarkList.add(gson.fromJson(jReader, Bookmark.class));
				}
			}
			reader.close();
			jReader.close();
			deleteFileOfBookmarksAndCreateNew();
			for (Bookmark bookmarkOld : bookmarkList) {
				if (bookmarkOld.getId() != bookmark.getId()) {
					insertBookmark(bookmarkOld);
				}
			}
		} catch (IOException ioEx) {
			popupIOException(ioEx);
		}
	}

	private void deleteFileOfBookmarksAndCreateNew() {
		boolean deleted = false;
		for (int i = 0; i < 20; i++) {
			deleted = getFileOfBookmarks().delete();
			if (deleted)
				break;
			System.gc();
			Thread.yield();
		}
		try {
			getFileOfBookmarks().createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Bookmark getBookmark(Bookmark bookmark) throws FileNotFoundException {
		if (getFileOfBookmarks().length() < 1) {
			return null;
		}
		FileReader reader = new FileReader(getFileOfBookmarks());
		Gson gson = new GsonBuilder().create();
		JsonReader jReader = new JsonReader(reader);
		jReader.setLenient(true);
		try {
			while (jReader.hasNext()) {
				// if the Reader is in the end of file it doesn't found the
				// bookmark
				if (jReader.peek() == JsonToken.END_DOCUMENT)
					return null;
				Bookmark bookmarkFromReader = gson.fromJson(jReader,
						Bookmark.class);
				if (bookmarkFromReader == bookmark)
					return bookmarkFromReader;
			}
		} catch (IOException ioEx) {
			popupIOException(ioEx);
		}
		return null;

	}

	@Override
	public List<Bookmark> getBookmarkAll() throws FileNotFoundException {
		List<Bookmark> bookmarkList = new LinkedList<Bookmark>();
		try {
			if (getFileOfBookmarks().length() < 1) {
				return bookmarkList;
			}
			FileReader reader = new FileReader(getFileOfBookmarks());
			Gson gson = new GsonBuilder().create();
			JsonReader jReader = new JsonReader(reader);
			jReader.setLenient(true);
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT)
					return bookmarkList;
				bookmarkList.add(gson.fromJson(jReader, Bookmark.class));
			}

		} catch (IOException ioEx) {
			popupIOException(ioEx);
		}
		return bookmarkList;
	}

	@Override
	public void editBookmark(Bookmark newBookmark) throws FileNotFoundException {
		List<Bookmark> bookmarkList = getBookmarkAll();
		deleteFileOfBookmarksAndCreateNew();
		for (Bookmark bookmark : bookmarkList) {
			if (bookmark.getId() == newBookmark.getId())
				insertBookmark(newBookmark);
			else
				insertBookmark(bookmark);
		}
	}

	@Override
	public Bookmark getBookmarkById(int bookmarkId)
			throws FileNotFoundException {
		try {
			Gson gson = new GsonBuilder().create();
			JsonReader jReader = new JsonReader(new FileReader(
					getFileOfBookmarks()));
			jReader.setLenient(true);
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT) {
					jReader.close();
					break;
				}
				Bookmark bookmark = gson.fromJson(jReader, Bookmark.class);
				if (bookmark.getId() == bookmarkId) {
					jReader.close();
					return bookmark;
				}
			}
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return null;
	}

	@Override
	public Bookmark getBookmarkContainingPC(PC pc) {
		Bookmark bookmark = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		JsonReader jReader = null;
		try {
			jReader = new JsonReader(new FileReader(getFileOfBookmarks()));
			jReader.setLenient(true);
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT) {
					jReader.close();
					return null;
				}
				bookmark = gson.fromJson(jReader, Bookmark.class);
				if (bookmark.getComputerList().contains(pc)) {
					jReader.close();
					return bookmark;
				}
			}
		} catch (FileNotFoundException fnfEx) {
			fnfEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return bookmark;
	}

	private void popupIOException(IOException ioEx) {
		ioEx.printStackTrace();
		IOExceptionFileWriterErrorDialog popupIOEx = null;
		popupIOEx = new IOExceptionFileWriterErrorDialog(WindowFactory.INSTANCE
				.getWindow_Browser().getShell(), SWT.DIALOG_TRIM,
				getFileOfBookmarks().getAbsolutePath());
		popupIOEx.open();
	}

	public File getFileOfBookmarks() {
		return new File(DAOFactory.INSTANCE.getSettingDAO().getSetting(
				"File of bookmarks"));
	}

}
