package main.providers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import main.exceptions.BadFormatPropertyException;
import main.exceptions.NotImplementedException;
import main.utils.Converter;

@Repository
public class DataStorage {
	
	private final String cacheLocation = "config" + File.separator + "StoredData.conf";
	
	HashMap<String, String> dataCacheMap;
	long lastModifiedDate = 0;
	private File storageFile;
	
	public DataStorage() {
		this.dataCacheMap = new HashMap<String, String>();
		this.storageFile = new File(cacheLocation);
	}
	
	public void refreshCache() throws BadFormatPropertyException, IOException {
		try {
			
			BufferedReader reader = new BufferedReader(new FileReader(this.storageFile));
			String lineData;
			int currentLine = 1;
			while((lineData = reader.readLine()) != null) {
				String [] lineSplitted = lineData.split("=");
				if(lineSplitted.length != 2)
					throw new BadFormatPropertyException("The data conf contains a bad format property at line " + currentLine);
				
				String key = lineSplitted[0];
				String value = lineSplitted[1];
				
				this.dataCacheMap.put(key, value);
				
				currentLine++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("WARN: When refreshing the cache, the file has not been found.");
		}
	}
	
	/**
	 * 
	 * @param key
	 * @param targetClass
	 * @return null if any data is found with the corresponding key, or returns the value data otherwise.
	 * @throws NotImplementedException
	 * @throws BadFormatPropertyException
	 * @throws IOException
	 */
	public Object getData(String key, Class<?> targetClass) throws NotImplementedException, BadFormatPropertyException, IOException {
		if(dataCacheMap.isEmpty() || this.storageFile.lastModified() > lastModifiedDate)
			refreshCache();
		String data = dataCacheMap.get(key);

		if(data == null)
			return null;

		try {
			Object dataResult;
			if(targetClass.equals(Integer.class)) {
				dataResult = new Integer( Integer.parseInt(data));
			}else if(targetClass.equals(String.class)) {
				dataResult = data;
			}else if(targetClass.equals(Time.class)) {
				dataResult = Converter.convertStringToTime(data);
			}else {
				throw new NotImplementedException("The type conversion of this class has not been implemented (class = "+targetClass.getSimpleName()+") ");
			}
			
			return dataResult;
		}catch(NotImplementedException nie) {
			throw nie;
		}catch(Exception e) {
			throw new BadFormatPropertyException(e);
		}
	}
	

	/**
	 * 
	 * @param key
	 * @param targetClass
	 * @param defaultValue A default value set if no value has already been stored.
	 * @return null if any data is found with the corresponding key and no defaultValue is provided, or returns the value data otherwise.
	 * @throws NotImplementedException
	 * @throws BadFormatPropertyException
	 * @throws IOException
	 */
	public Object getData(String key, Class<?> targetClass, Object defaultValue) throws NotImplementedException, BadFormatPropertyException, IOException {
		
		Object data = this.getData(key, targetClass);
		
		if(data == null && defaultValue != null) {
			this.setData(key, defaultValue);
			data = this.getData(key, targetClass);
		}
		
		return data;
	}
	
	public void setData(String key, Object data) throws IOException, NotImplementedException, BadFormatPropertyException {
		this.refreshCache();
		
		if(data.getClass().equals(String.class)) {
			this.dataCacheMap.put(key, (String) data);
		}else if(data.getClass().equals(Integer.class)) {
			this.dataCacheMap.put(key, String.valueOf((Integer) data));
		}else if(data.getClass().equals(Time.class)) {
			String timeString = Converter.convertTimeToString((Time) data);
			this.dataCacheMap.put(key, timeString);
		}else {
			throw new NotImplementedException("The type conversion of this class has not been implemented (class = "+ data.getClass().getSimpleName()+") ");
		}
				
		// Put in the file
		this.storageFile.createNewFile();
		BufferedWriter writer = new BufferedWriter(new FileWriter(this.storageFile));
		
		writer.write("");
		for (String keyMap : this.dataCacheMap.keySet()) {
			String value = this.dataCacheMap.get(keyMap);
			writer.append(keyMap + "=" + value);
			writer.newLine();
		}
		writer.flush();
		writer.close();
		
		// Update the last modified time (for cache)
		this.lastModifiedDate = this.storageFile.lastModified();
	}	
}
