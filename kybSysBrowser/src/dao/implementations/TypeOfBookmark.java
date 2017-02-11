package dao.implementations;

import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import entities.Bookmark;

public class TypeOfBookmark extends TypeAdapter<Bookmark> {

	@Override
	public void write(JsonWriter out, Bookmark value) throws IOException {

	}

	@Override
	public Bookmark read(JsonReader in) throws IOException {
		final Bookmark bookmark = new Bookmark();
		in.beginObject();
		while (in.hasNext()) {
			switch (in.nextName()) {
			case "name":
				bookmark.setName(in.nextString());
				break;
			case "url":
				bookmark.setUrl(in.nextString());
				break;
			}
		}
		return null;
	}
}
