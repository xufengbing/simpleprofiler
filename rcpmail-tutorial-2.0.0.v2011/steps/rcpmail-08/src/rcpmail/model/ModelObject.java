package rcpmail.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

// FG I'd like to reduce the visibility but it's used by the NavigatorContentProvider. Any chance to make it package-private?
public abstract class ModelObject {

	private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(
			this);

	public final void addPropertyChangeListener(PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public final void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public final void removePropertyChangeListener(
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public final void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		propertyChangeSupport.removePropertyChangeListener(propertyName,
				listener);
	}

	final void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		propertyChangeSupport.firePropertyChange(propertyName, oldValue,
				newValue);
	}

}
