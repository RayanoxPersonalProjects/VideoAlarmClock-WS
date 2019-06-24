package main.providers;

import java.io.File;
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
	
	private final String cacheLocation = "config" + File.pathSeparator + "StoredData.conf";
	
	HashMap<String, String> dataCacheMap;
	long lastModifiedDate = 0;
	private File storageFile;
	
	public DataStorage() {
		this.dataCacheMap = new HashMap<String, String>();
		this.storageFile = new File(cacheLocation);
	}
	
	public void refreshCache() {
		//TODO Implement the refresh (file reading)
	}
	
	public Object getData(String key, Class<?> targetClass) throws NotImplementedException, BadFormatPropertyException {
		if(dataCacheMap.isEmpty() || this.storageFile.lastModified() > lastModifiedDate)
			refreshCache();
		String data = dataCacheMap.get(key);
		

		try {
			Object dataResult;
			if(targetClass.equals(Integer.class)) {
				dataResult = new Integer( Integer.parseInt(data));
			}else if(targetClass.equals(String.class)) {
				dataResult = data;
			}else if(targetClass.equals(Time.class)) {
				dataResult = Converter.convertStringToCalendar(data);
			}else {
				throw new NotImplementedException("The type conversion of this class has not been implemented (class = "+targetClass.getSimpleName()+") ");
			}
			
			return data == null ? null : dataResult;
		}catch(NotImplementedException nie) {
			throw nie;
		}catch(Exception e) {
			throw new BadFormatPropertyException(e);
		}
	}
	
	public void setData(Object data) {
		//dataCacheMap.
	}	
}
