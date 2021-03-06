package flowtimer.parsing.json;


import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import flowtimer.parsing.TokenReader;

public class JSONObject extends JSONValue {
	public static JSONValue parse(TokenReader tokens, String token) throws IOException, ParseException {
		JSONObject result = new JSONObject();
		
		if((token = tokens.next()).equals("}")) {
			return result;
		}
		do {
			String key = JSONString.parse(tokens, token).toString();
			tokens.parseAssert((token = tokens.next()).equals(":"), "Separating ':' expected!");
			result.put(key, JSONValue.parse(tokens, tokens.next()));
			if(!(token = tokens.next()).equals(",")) {
				break;
			}
			token = tokens.next();
		} while(true);
		
		tokens.parseAssert(token.equals("}"), "Closing '}' expected!");
		return result;
	}
	
	private Map<String, JSONValue> map;
	
	public JSONObject() {
		map = new HashMap<String, JSONValue>();
	}
	
	public void put(String key, int value) {
		put(key, JSONValue.create(value));
	}
	
	public void put(String key, byte value) {
		put(key, JSONValue.create(value));
	}
	
	public void put(String key, short value) {
		put(key, JSONValue.create(value));
	}
	
	public void put(String key, long value) {
		put(key, JSONValue.create(value));
	}
	
	public void put(String key, float value) {
		put(key, JSONValue.create(value));
	}
	
	public void put(String key, double value) {
		put(key, JSONValue.create(value));
	}
	
	public void put(String key, boolean value) {
		put(key, JSONValue.create(value));
	}
	
	public void put(String key, char value) {
		put(key, JSONValue.create(value));
	}
	
	public void put(String key, String value) {
		put(key, JSONValue.create(value));
	}
	
	public void put(String key, Object value) {
		if(value instanceof Integer) {
			put(key, (int) value);
		} else if(value instanceof Byte) {
			put(key, (byte) value);
		} else if(value instanceof Short) {
			put(key, (short) value);
		} else if(value instanceof Long) {
			put(key, (long) value);
		} else if(value instanceof Float) {
			put(key, (float) value);
		} else if(value instanceof Double) {
			put(key, (double) value);
		} else if(value instanceof Boolean) {
			put(key, (boolean) value);
		} else if(value instanceof Character) {
			put(key, (char) value);
		} else if(value instanceof String) {
			put(key, (String) value);
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	public void put(String key, JSONValue value) {
		if(key == null) {
			throw new NullPointerException("Key cannot be null");
		}
		if(value == null) {
			throw new NullPointerException("value cannot be null");
		}
		map.put(key, value);
	}
	
	public void put(String key, JSONArray value) {
		if(key == null) {
			throw new NullPointerException("Key cannot be null");
		}
		if(value == null) {
			throw new NullPointerException("value cannot be null");
		}
		map.put(key, value);
	}
	
	@Override
	public boolean isObject() {
		return true;
	}
	
	@Override
	public Map<String, JSONValue> asObject() {
		return new HashMap<>(map);
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((map == null) ? 0 : map.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		JSONObject other = (JSONObject)obj;
		if(map == null) {
			if(other.map != null)
				return false;
		} else if(!map.equals(other.map))
			return false;
		return true;
	}
	
	@Override
	public void write(Writer writer) throws IOException {
		write(writer, 1);
	}
	
	private void writeNewLine(Writer writer, int tabLevel) throws IOException {
		writer.write('\n');
		for(int i = 0; i < tabLevel; i++) {
			writer.write('\t');
		}
	}
	
	private void write(Writer writer, int tabLevel) throws IOException {
		writer.write('{');
		writeNewLine(writer, tabLevel);
		
		Iterator<Map.Entry<String, JSONValue>> it = map.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry<String, JSONValue> current = it.next();
			new JSONString(current.getKey()).write(writer);
			writer.write(':');
			writer.write(' ');
			
			JSONValue value = current.getValue();
			if(value.isObject()) {
				((JSONObject)value).write(writer, tabLevel + 1);
			} else {
				value.write(writer);
			}
			
			if(it.hasNext()) {
				writer.write(", ");
				writeNewLine(writer, tabLevel);
			}
		}
		writeNewLine(writer, tabLevel - 1);
		writer.write('}');
	}
}
