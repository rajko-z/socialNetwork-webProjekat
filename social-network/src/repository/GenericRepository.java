package repository;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Entity;

public abstract class GenericRepository<T extends Entity> {
	protected HashMap<Long, T> data;
	protected String filePath;
	
	public GenericRepository(String filePath) {
		this.filePath = filePath;
		this.data = new HashMap<Long, T>();
	}
	
	public void loadData() {
	    try (BufferedReader in = new BufferedReader(
	    		new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8)))
        {
            String line = null;
            
            while((line = in.readLine())!= null) {
            	
            	if (line.trim().startsWith("#"))
					continue;
            	
                String[] tokens = line.split("\\|");
                for(int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim();
                }
                T newEntity = createEntityFromTokens(tokens);
                this.data.put(newEntity.getId(), newEntity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	public void saveData(String fileHeader) {
		try (PrintWriter out = new PrintWriter(
				new OutputStreamWriter(new FileOutputStream(this.filePath), StandardCharsets.UTF_8)))
        {
        	out.println(fileHeader);
            this.data.forEach((key, value) -> out.println(value.formatEntityForFile()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

	}
	
	public T add(T entity) {
		Long newId = generateId();
		entity.setId(newId);
		this.data.put(entity.getId(), entity);
		return entity;
	}
	
	public T getById(Long id) {
		return this.data.get(id);
	}
	
	public List<T> getAll() {
		return new ArrayList<T>(this.data.values());
	}
	
	public Long generateId() {
		long maxx = 0L;
		for (Map.Entry<Long, T> e : this.data.entrySet()) {
			maxx = Math.max(e.getKey(), maxx);
		}
		return maxx + 1;
	}
	
	public void printData() {
		for (Map.Entry<Long, T> e : this.data.entrySet()) {
			System.out.println(e.getValue());
		}
	}
	
	
	protected abstract T createEntityFromTokens(String[] tokens);
	
}
