package gwt.mosaic.client.json;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

import gwt.mosaic.client.collections.HashMap;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.shared.serialization.SerializationException;

public class JSONSerializer {

	/**
	 * Converts a JSON value to a map.
	 * 
	 * @param json
	 *            The JSON value.
	 * 
	 * @return The parsed map.
	 */
	public static Map<String, ?> parseMap(String json)
			throws SerializationException {
		JSONValue jsonValue = JSONParser.parseLenient(json);
		JSONObject jsonObject = jsonValue.isObject();
		if (jsonObject == null) {
			return null;
		}

		HashMap<String, Object> result = new HashMap<String, Object>();
		for (String key : jsonObject.keySet()) {
			JSONValue value = jsonObject.get(key);

			if (value.isNull() != null) {
				continue;
			}
			
			if ((value.isArray() != null) || (value.isObject() != null)) {
				result.put(key, value.toString());
				continue;
			}
			
			if (value.isNumber() != null) {
				result.put(key, Double.valueOf(value.isNumber().doubleValue()));
				continue;
			}

			if (value.isBoolean() != null) {
				result.put(key,
						Boolean.valueOf(value.isBoolean().booleanValue()));
				continue;
			}

			if (value.isString() != null) {
				result.put(key, value.isString().stringValue());
				continue;
			}
		}
		return result;
	}

}
