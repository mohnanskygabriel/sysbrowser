package dao.implementations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.swt.widgets.TreeItem;

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
	private WindowBrowser browser = WindowBrowserFactory.INSTANCE.getWindow_Browser();
	private final File fileOfBookmarks = browser.getFileOfBookmarks();

	/* TreeItem selectedTreeItem = browser.getCurrentSelection(); */

	@Override
	public void insertBookmark(Bookmark bookmark) throws FileNotFoundException, IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		FileWriter fileW = new FileWriter(fileOfBookmarks, true);
		JsonWriter writer = gson.newJsonWriter(fileW);
		writer.jsonValue(gson.toJson(bookmark));
		writer.close();
	}

	@Override
	public void deleteBookmark(Bookmark bookmark) throws IOException {
		FileWriter fw = null;
		File copyOfBookmarks = new File("newFileOfBookmarks.txt");
		try (Scanner sc = new Scanner(fileOfBookmarks)) {
			fw = new FileWriter(copyOfBookmarks);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (!line.equals(bookmark.getName())) {
					fw.write(line);
					if (sc.hasNextLine()) {
						fw.write("\n");
					}
				} else {
					while (!line.equals("---")) {
						line = sc.nextLine();
					}
				}
			}
			if (fw != null)
				fw.close();
			if (sc != null)
				sc.close();
			boolean deleted = false;
			for (int i = 0; i < 20; i++) {
				deleted = fileOfBookmarks.delete();
				if (deleted)
					break;
				System.gc();
				Thread.yield();
			}
			boolean renamed = false;
			if (deleted) {
				for (int i = 0; i < 20; i++) {
					renamed = copyOfBookmarks.renameTo(fileOfBookmarks);
					if (renamed)
						break;
					System.gc();
					Thread.yield();
				}
			}
		}
	}

	@Override
	public void editBookmark(Bookmark oldBookmark, Bookmark newBookmark) throws IOException {
		FileWriter fw = null;
		File copyOfBookmarks = new File("copyOfBookmarks.txt");
		Scanner lineScanner = null;
		try (Scanner sc = new Scanner(fileOfBookmarks)) {
			fw = new FileWriter(copyOfBookmarks);
			while (sc.hasNextLine()) {
				lineScanner = new Scanner(sc.nextLine());
				lineScanner.useDelimiter("\t");
				String next = lineScanner.next();
				if (!next.equals(oldBookmark.getName())) {
					fw.write(next);
					while (lineScanner.hasNext()) {
						fw.write("\t");
						fw.write(lineScanner.next());
					}
					fw.write("\n");
				} else {
					fw.write(newBookmark.getName());
					fw.write("\n");
					fw.write(newBookmark.getUrl());
					fw.write("\n");
					sc.nextLine();
				}
			}
			if (fw != null)
				fw.close();
			if (sc != null)
				sc.close();
			if (lineScanner != null)
				lineScanner.close();

			boolean deleted = false;
			for (int i = 0; i < 20; i++) {
				deleted = fileOfBookmarks.delete();
				if (deleted)
					break;
				System.gc();
				Thread.yield();
			}
			boolean renamed = false;
			if (deleted) {
				for (int i = 0; i < 20; i++) {
					renamed = copyOfBookmarks.renameTo(fileOfBookmarks);
					if (renamed)
						break;
					System.gc();
					Thread.yield();
				}
			}
		}
	}

	@Override
	public Bookmark getBookmark(Bookmark bookmark) throws FileNotFoundException, IOException {
		FileReader reader = new FileReader(fileOfBookmarks);
		Gson gson = new GsonBuilder().create();
		JsonReader jReader = new JsonReader(reader);
		jReader.setLenient(true);
		while (jReader.hasNext()) {
			// if the Reader is in the end of file it doesn't found the bookmark
			if (jReader.peek() == JsonToken.END_DOCUMENT)
				return null;
			Bookmark bookmarkFromReader = gson.fromJson(jReader, Bookmark.class);
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
}
