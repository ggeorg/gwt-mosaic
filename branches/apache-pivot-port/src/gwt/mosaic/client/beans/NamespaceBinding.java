package gwt.mosaic.client.beans;

import gwt.mosaic.client.collections.Dictionary;
import gwt.mosaic.client.collections.Map;
import gwt.mosaic.client.collections.MapListener;
import gwt.mosaic.client.collections.Sequence;
import gwt.mosaic.client.json.JSON;

/**
 * Represents a binding relationship between a source and a target property
 * within a namespace.
 */
public class NamespaceBinding {

	/**
	 * Namespace bind mapping interface.
	 */
	public interface BindMapping {
		/**
		 * Transforms a source value during a bind operation.
		 * 
		 * @param value
		 */
		public Object evaluate(Object value);
	}

	private Map<String, Object> namespace;

	private String sourcePath;
	private Object source;
	private Map<String, Object> sourceMap;
	private BeanMonitor sourceMonitor;
	private String sourceKey;

	private String targetPath;
	private Object target;
	private Dictionary<String, Object> targetDictionary;
	private String targetKey;

	private BindMapping bindMapping;

	private boolean updating = false;

	private MapListener<String, Object> sourceMapListener = new MapListener.Adapter<String, Object>() {
		@Override
		public void valueUpdated(Map<String, Object> map, String key,
				Object previousValue) {
			if (key.equals(sourceKey) && !updating) {
				updating = true;
				targetDictionary.put(targetKey, getTransformedSourceValue());
				updating = false;
			}
		}
	};

	private PropertyChangeListener sourcePropertyChangeListener = new PropertyChangeListener() {
		@Override
		public void propertyChanged(Object bean, String propertyName) {
			if (propertyName.equals(sourceKey) && !updating) {
				updating = true;
				targetDictionary.put(targetKey, getTransformedSourceValue());
				updating = false;
			}
		}
	};

	public NamespaceBinding(Map<String, Object> namespace, String sourcePath,
			String targetPath) {
		this(namespace, sourcePath, targetPath, null);
	}

	@SuppressWarnings("unchecked")
	public NamespaceBinding(Map<String, Object> namespace, String sourcePath,
			String targetPath, BindMapping bindMapping) {
		if (namespace == null) {
			throw new IllegalArgumentException();
		}

		if (sourcePath == null) {
			throw new IllegalArgumentException();
		}

		if (targetPath == null) {
			throw new IllegalArgumentException();
		}

		// Set the namespace
		this.namespace = namespace;

		// Set the source properties
		this.sourcePath = sourcePath;

		Sequence<String> sourceKeys = JSON.parse(sourcePath);
		sourceKey = sourceKeys.remove(sourceKeys.getLength() - 1, 1).get(0);
		source = JSON.get(namespace, sourceKeys);

		if (source instanceof Map<?, ?>) {
			sourceMap = (Map<String, Object>) source;
			sourceMonitor = null;
		} else {
			sourceMap = BeanAdapterFactory.createFor(source);
			sourceMonitor = new BeanMonitor(source);
		}

		if (!sourceMap.containsKey(sourceKey)) {
			throw new IllegalArgumentException("Source property \""
					+ sourcePath + "\" does not exist.");
		}

		if (sourceMonitor != null && !sourceMonitor.isNotifying(sourceKey)) {
			throw new IllegalArgumentException("\"" + sourceKey
					+ "\" is not a notifying property.");
		}
		
        // Set the target properties
        this.targetPath = targetPath;

        Sequence<String> targetKeys = JSON.parse(targetPath);
        targetKey = targetKeys.remove(targetKeys.getLength() - 1, 1).get(0);
        target = JSON.get(namespace, targetKeys);

        if (target instanceof Dictionary<?, ?>) {
            targetDictionary = (Dictionary<String, Object>)target;
        } else {
            targetDictionary = BeanAdapterFactory.createFor(target);
        }

        if (!targetDictionary.containsKey(targetKey)) {
            throw new IllegalArgumentException("Target property \"" + targetPath
                + "\" does not exist.");
        }

		// Set the bind mapping
		this.bindMapping = bindMapping;

		// Perform the initial set from source to target
		targetDictionary.put(targetKey, getTransformedSourceValue());
	}

	/**
	 * Returns the namespace.
	 */
	public Map<String, Object> getNamespace() {
		return namespace;
	}

	/**
	 * Returns the path to the source property.
	 */
	public String getSourcePath() {
		return sourcePath;
	}

	/**
	 * Returns the source object.
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * Returns the name of the source property.
	 */
	public String getSourceKey() {
		return sourceKey;
	}

	/**
	 * Returns the path to the target property.
	 */
	public String getTargetPath() {
		return targetPath;
	}

	/**
	 * Returns the target object.
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * Returns the name of the target property.
	 */
	public String getTargetKey() {
		return targetKey;
	}

	/**
	 * Returns the bind mapping.
	 * 
	 * @return The bind mapping to use during binding, or <tt>null</tt> if no
	 *         bind mapping is specified.
	 */
	public BindMapping getBindMapping() {
		return bindMapping;
	}

	/**
	 * Returns the current source value with any bind mapping applied.
	 */
	public Object getTransformedSourceValue() {
		Object sourceValue = sourceMap.get(sourceKey);
		return (bindMapping == null) ? sourceValue : bindMapping
				.evaluate(sourceValue);
	}

	/**
	 * Binds the source property to the target property.
	 */
	public void bind() {
		if (source instanceof Map<?, ?>) {
			sourceMap.getMapListeners().add(sourceMapListener);
		} else {
			sourceMonitor.getPropertyChangeListeners().add(
					sourcePropertyChangeListener);
		}
	}

	/**
	 * Unbinds the source property from the target property.
	 */
	public void unbind() {
		if (source instanceof Map<?, ?>) {
			sourceMap.getMapListeners().remove(sourceMapListener);
		} else {
			sourceMonitor.getPropertyChangeListeners().remove(
					sourcePropertyChangeListener);
		}
	}

	@Override
	public boolean equals(Object o) {
		boolean equals = false;

		if (o instanceof NamespaceBinding) {
			NamespaceBinding namespaceBinding = (NamespaceBinding) o;

			equals = (source == namespaceBinding.source
					&& sourceKey.equals(namespaceBinding.sourceKey)
					&& target == namespaceBinding.target && targetKey
					.equals(namespaceBinding.targetKey));
		}

		return equals;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + source.hashCode();
		result = prime * result + sourceKey.hashCode();
		result = prime * result + target.hashCode();
		result = prime * result + targetKey.hashCode();
		return result;
	}
}
