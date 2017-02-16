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
import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.WindowBrowserFactory;

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
			boolean deleted = false;
			for (int i = 0; i < 20; i++) {
				deleted = getFileOfBookmarks().delete();
				if (deleted)
					break;
				System.gc();
				Thread.yield();
			}
			getFileOfBookmarks().createNewFile();
			for (Bookmark bookmarkOld : bookmarkList) {
				if (bookmarkOld.getId() != bookmark.getId()) {
					insertBookmark(bookmarkOld);
				}
			}
		} catch (IOException ioEx) {
			popupIOException(ioEx);
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
	public void editBookmark(Bookmark oldBookmark, Bookmark newBookmark) {
		// TODO Auto-generated method stub

	}

	private void popupIOException(IOException ioEx) {
		ioEx.printStackTrace();
		IOExceptionFileWriterErrorDialog popupIOEx = null;
		popupIOEx = new IOExceptionFileWriterErrorDialog(
				WindowBrowserFactory.INSTANCE.getWindow_Browser().getShell(),
				SWT.DIALOG_TRIM, getFileOfBookmarks().getAbsolutePath());
		popupIOEx.open();
	}

	public File getFileOfBookmarks() {
		return new File(DAOFactory.INSTANCE.getSettingDAO().getSetting(
				"File of bookmarks"));
	}
}
