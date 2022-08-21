package util;

import java.util.ArrayList;
import java.util.List;

import model.Entity;

public class CSVFormatUtil {


	public List<Long> createListOfIdsFromString(String str) {
		List<Long> retVal = new ArrayList<>();
		if (str.equals("null") || str.trim().isEmpty())
			return retVal;
		String[] tokens = str.split(",");
		for (String token : tokens) {
			retVal.add(Long.parseLong(token.trim()));
		}
		return retVal;
	}
	
	public <T extends Entity> String getFormattedStringOfIdsFromListOfEntities(List<T> list) {
		if (list == null || list.size() == 0)
			return "null";
		
		StringBuilder sb = new StringBuilder("");
        for (Entity e : list) {
            sb.append(e.getId()).append(',');
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
	}


	
}
