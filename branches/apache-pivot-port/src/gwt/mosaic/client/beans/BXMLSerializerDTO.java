package gwt.mosaic.client.beans;

import gwt.mosaic.client.collections.HashMap;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.collections.immutable.ImmutableMap;

import java.io.Serializable;
import java.util.Iterator;

@SuppressWarnings("serial")
public class BXMLSerializerDTO implements Serializable {

	private Serializable root;

	private Map<String, Object> namespace = new HashMap<String, Object>();

	BXMLSerializerDTO() {
	}

	public BXMLSerializerDTO(Serializable root,
			Map<String, ? extends Object> namespace) {
		if (root == null) {
			throw new IllegalArgumentException();
		}
		if (!(root instanceof Object)) {
			throw new IllegalArgumentException();
		}
		this.root = root;

		Iterator<String> it = namespace.iterator();
		while (it.hasNext()) {
			String key = it.next();
			this.namespace.put(key, (Object) namespace.get(key));
		}
	}

	public Serializable getRoot() {
		return root;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> getNamespace() {
		return new ImmutableMap(this.namespace);
	}

}
