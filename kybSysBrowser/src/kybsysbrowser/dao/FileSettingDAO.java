package kybsysbrowser.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
		try (JsonWriter writer = gson.newJsonWriter(new FileWriter(getSettingsFile(), true))) {
			writer.beginObject();
			writer.name(name);
			writer.jsonValue("\"" + setting + "\"");
			writer.endObject();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
	}

	@Override
	public String getSetting(String name) {
		try (JsonReader jReader = new JsonReader(new FileReader(getSettingsFile()))) {
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
		try (JsonReader jReader = new JsonReader(new FileReader(getSettingsFile()));
				JsonWriter jWriter = gson.newJsonWriter(new FileWriter(getSettingsFile(), false))) {

			jReader.setLenient(true);
			while (jReader.hasNext()) {
				if (jReader.peek() == JsonToken.END_DOCUMENT) {
					break;
				}
				jReader.beginObject();
				settings.put(jReader.nextName(), jReader.nextString());
				jReader.endObject();
			}

			jWriter.setLenient(true);
			for (String settingName : settings.keySet()) {
				jWriter.beginObject();
				jWriter.name(settingName);
				if (settingName.equals(name)) {
					jWriter.jsonValue("\"" + newSetting + "\"");
				} else {
					jWriter.jsonValue("\"" + settings.get(settingName) + "\"");
				}
				jWriter.endObject();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioEx) {
			ioEx.printStackTrace();
		}

	}

	private File getSettingsFile() {
		return settingsFile;
	}

}
