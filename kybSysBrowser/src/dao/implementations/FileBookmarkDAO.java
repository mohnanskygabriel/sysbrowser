package dao.implementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import windows.WindowBrowser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import dao.interfaces.BookmarkDAO;
import entities.Bookmark;
import factories.WindowBrowserFactory;

public class FileBookmarkDAO implements BookmarkDAO {
	private WindowBrowser browser = WindowBrowserFactory.INSTANCE
			.getWindow_Browser();
	private final File fileOfBookmarks = browser.getFileOfBookmarks();

	/* TreeItem selectedTreeItem = browser.getCurrentSelection(); */

	@Override
	public void insertBookmark(Bookmark bookmark) throws FileNotFoundException,
			IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fileW = new FileWriter(fileOfBookmarks, true);
		JsonWriter writer = gson.newJsonWriter(fileW);
		writer.jsonValue(gson.toJson(bookmark));
		writer.close();
	}

	@Override
	public void deleteBookmark(Bookmark bookmark) throws FileNotFoundException,
			IOException {
		List<Bookmark> bookmarkList = new LinkedList<Bookmark>();
		FileReader reader = new FileReader(fileOfBookmarks);
		Gson gson = new GsonBuilder().create();
		JsonReader jReader = new JsonReader(reader);
		jReader.setLenient(true);
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
			deleted = fileOfBookmarks.delete();
			if (deleted)
				break;
			System.gc();
			Thread.yield();
		}
		fileOfBookmarks.createNewFile();
		for (Bookmark bookmarkOld : bookmarkList) {
			if (bookmarkOld.getId() != bookmark.getId()) {
				insertBookmark(bookmarkOld);
			}

		}
	}

	@Override
	public Bookmark getBookmark(Bookmark bookmark)
			throws FileNotFoundException, IOException {
		FileReader reader = new FileReader(fileOfBookmarks);
		Gson gson = new GsonBuilder().create();
		JsonReader jReader = new JsonReader(reader);
		jReader.setLenient(true);
		while (jReader.hasNext()) {
			// if the Reader is in the end of file it doesn't found the bookmark
			if (jReader.peek() == JsonToken.END_DOCUMENT)
				return null;
			Bookmark bookmarkFromReader = gson
					.fromJson(jReader, Bookmark.class);
			if (bookmarkFromReader == bookmark)
				return bookmarkFromReader;
		}
		return null;

	}

	@Override
	public List<Bookmark> getBookmarkAll() {
		List<Bookmark> bookmarkList = new LinkedList<Bookmark>();
		try {
			FileReader reader = new FileReader(fileOfBookmarks);
			Gson gson = new GsonBuilder().create();
			JsonReader jReader = new JsonReader(reader);
			jReader.setLenient(true);
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT)
					return bookmarkList;
				bookmarkList.add(gson.fromJson(jReader, Bookmark.class));
			}

		} catch (FileNotFoundException fnfEx) {
			fnfEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return bookmarkList;
	}

	@Override
	public void editBookmark(Bookmark oldBookmark, Bookmark newBookmark) {
		// TODO Auto-generated method stub

	}
}
