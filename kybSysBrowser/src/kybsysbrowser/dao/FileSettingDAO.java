package kybsysbrowser.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class FileSettingDAO implements SettingDAO {

	private static final File settingsFile = new File("src/settings.txt");

	@Override
	public void insertSetting(String name, String setting) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		try (JsonWriter jWriter = gson
				.newJsonWriter(new OutputStreamWriter(new FileOutputStream(getSettingsFile(), true), "UTF-8"))) {
			jWriter.beginObject();
			jWriter.name(name);
			jWriter.jsonValue("\"" + setting + "\"");
			jWriter.endObject();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}

	@Override
	public String getSetting(String name) {
		try (JsonReader jReader = new JsonReader(
				new InputStreamReader(new FileInputStream(getSettingsFile()), "UTF-8"))) {
			jReader.setLenient(true);
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT) {
					/*
					 * the Reader is in the end of file and it doesn't found the
					 * setting
					 */
					return null;
				}

				jReader.beginObject();
				if (jReader.nextName().equals(name)) {
					String setting = jReader.nextString();

					return setting;
				} else {
					jReader.nextString();
					jReader.endObject();
				}
			}
		} catch (FileNotFoundException fnfEx) {
			fnfEx.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		return null;
	}

	@Override
	public void editSetting(String name, String newSetting) {
		Map<String, String> settings = new HashMap<String, String>();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try (JsonReader jReader = new JsonReader(
				new InputStreamReader(new FileInputStream(getSettingsFile()), "UTF-8"));) {

			jReader.setLenient(true);
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT) {
					break;
				}
				jReader.beginObject();
				settings.put(jReader.nextName(), jReader.nextString());
				jReader.endObject();
			}
			jReader.close();
			deleteSettingsFileAndCreateNew();
			try (JsonWriter jWriter = gson
					.newJsonWriter(new OutputStreamWriter(new FileOutputStream(getSettingsFile(), true), "UTF-8"))) {
				jWriter.setLenient(true);
				for (final Iterator<Entry<String, String>> iter = settings.entrySet().iterator(); iter.hasNext();) {
					jWriter.beginObject();
					Map.Entry<String, String> entry = iter.next();
					final String settingName = entry.getKey();
					final String value = entry.getValue();
					jWriter.name(settingName);
					if (settingName.equals(name)) {
						jWriter.jsonValue("\"" + newSetting + "\"");
					} else {
						jWriter.jsonValue("\"" + value + "\"");
					}
					jWriter.endObject();
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

	}

	private void deleteSettingsFileAndCreateNew() {
		boolean deleted = false;
		try {
			for (int i = 0; i < 20; i++) {
				deleted = getSettingsFile().delete();
				if (deleted)
					break;
				System.gc();
				Thread.yield();
			}
			if (!getSettingsFile().createNewFile()) {
				throw new IOException();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private File getSettingsFile() {
		return settingsFile;
	}

}
