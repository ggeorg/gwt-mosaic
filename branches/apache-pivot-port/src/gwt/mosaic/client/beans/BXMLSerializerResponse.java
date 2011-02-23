package gwt.mosaic.client.beans;

import gwt.mosaic.client.collections.HashMap;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.collections.immutable.ImmutableMap;

import java.io.Serializable;
import java.util.Iterator;

@SuppressWarnings("serial")
public class BXMLSerializerResponse implements Serializable {

	private Serializable root;

	private Map<String, Serializable> namespace = new HashMap<String, Serializable>();

	BXMLSerializerResponse() {
	}

	public BXMLSerializerResponse(Serializable root,
			Map<String, ? extends Serializable> namespace) {
		if (root == null) {
			throw new IllegalArgumentException();
		}
		if (!(root instanceof Serializable)) {
			throw new IllegalArgumentException();
		}
		this.root = root;

		Iterator<String> it = namespace.iterator();
		while (it.hasNext()) {
			String key = it.next();
			this.namespace.put(key, (Serializable) namespace.get(key));
		}
	}

	public Serializable getRoot() {
		return root;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, ? extends Serializable> getNamespace() {
		//return new ImmutableMap(new MapAdapter(this.namespace));
		return new ImmutableMap(this.namespace);
	}

}
