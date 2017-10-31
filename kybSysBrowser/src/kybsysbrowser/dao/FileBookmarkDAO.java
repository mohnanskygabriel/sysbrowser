package kybsysbrowser.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.SWT;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import kybsysbrowser.dialog.exceptionSolving.IOExceptionFileWriterErrorDialog;
import kybsysbrowser.entity.Bookmark;
import kybsysbrowser.entity.PC;
import kybsysbrowser.factory.DAOFactory;
import kybsysbrowser.factory.WindowFactory;

public class FileBookmarkDAO implements BookmarkDAO {
	private final static File bookmarksFile = new File(
			DAOFactory.INSTANCE.getSettingDAO().getSetting("File of bookmarks"));

	@Override
	public void insertBookmark(Bookmark bookmark) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (JsonWriter writer = gson
				.newJsonWriter(new OutputStreamWriter(new FileOutputStream(getBookmarksfile(), true), "UTF-8"))) {
			writer.jsonValue(gson.toJson(bookmark));
		} catch (IOException ioEx) {
			popupIOException(ioEx);
		}

	}

	@Override
	public void deleteBookmark(Bookmark bookmark) {
		List<Bookmark> bookmarkList = getBookmarkAll();
		deleteFileOfBookmarksAndCreateNew();
		for (Bookmark bookmarkOld : bookmarkList) {
			if (bookmarkOld.getId() != bookmark.getId()) {
				insertBookmark(bookmarkOld);
			}
		}
	}

	@Override
	public Bookmark getBookmark(Bookmark bookmark) {
		if (getBookmarksfile().length() < 1) {
			return null;
		}
		Gson gson = new GsonBuilder().create();
		try (JsonReader jReader = new JsonReader(
				new InputStreamReader(new FileInputStream(getBookmarksfile()), "UTF-8"))) {
			jReader.setLenient(true);
			while (jReader.hasNext()) {
				// if the Reader is in the end of file it doesn't found the
				// bookmark
				if (jReader.peek() == JsonToken.END_DOCUMENT)
					return null;
				Bookmark bookmarkFromReader = gson.fromJson(jReader, Bookmark.class);
				if (bookmarkFromReader == bookmark)
					return bookmarkFromReader;
			}
		} catch (IOException ioEx) {
			popupIOException(ioEx);
		}
		return null;

	}

	@Override
	public List<Bookmark> getBookmarkAll() {
		List<Bookmark> bookmarkList = new LinkedList<Bookmark>();
		try (JsonReader jReader = new JsonReader(
				new InputStreamReader(new FileInputStream(getBookmarksfile()), "UTF-8"))) {
			if (getBookmarksfile().length() < 1) {
				return bookmarkList;
			}
			jReader.setLenient(true);
			Gson gson = new GsonBuilder().create();
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT)
					return bookmarkList;
				bookmarkList.add(gson.fromJson(jReader, Bookmark.class));
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ioEx) {
			popupIOException(ioEx);
		}
		return bookmarkList;
	}

	@Override
	public void editBookmark(Bookmark newBookmark) {
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
	public Bookmark getBookmarkById(int bookmarkId) {
		try (JsonReader jReader = new JsonReader(
				new InputStreamReader(new FileInputStream(getBookmarksfile()), "UTF-8"))) {
			Gson gson = new GsonBuilder().create();
			jReader.setLenient(true);
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT) {
					return null;
				}
				Bookmark bookmark = gson.fromJson(jReader, Bookmark.class);
				if (bookmark.getId() == bookmarkId) {
					return bookmark;
				}
			}
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return null;
	}

	@Override
	public Bookmark getBookmarkContainingPC(PC pc) {
		Bookmark bookmark = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (JsonReader jReader = new JsonReader(
				new InputStreamReader(new FileInputStream(getBookmarksfile()), "UTF-8"))) {
			jReader.setLenient(true);
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT) {
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
		popupIOEx = new IOExceptionFileWriterErrorDialog(WindowFactory.INSTANCE.getWindow_Browser().getShell(),
				SWT.DIALOG_TRIM, getBookmarksfile().getAbsolutePath());
		popupIOEx.open();
	}

	private void deleteFileOfBookmarksAndCreateNew() {
		boolean deleted = false;
		try {
			for (int i = 0; i < 20; i++) {
				deleted = getBookmarksfile().delete();
				if (deleted)
					break;
				System.gc();
				Thread.yield();
			}

			if (!getBookmarksfile().createNewFile()) {
				throw new IOException();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static File getBookmarksfile() {

		return bookmarksFile;
	}

}
