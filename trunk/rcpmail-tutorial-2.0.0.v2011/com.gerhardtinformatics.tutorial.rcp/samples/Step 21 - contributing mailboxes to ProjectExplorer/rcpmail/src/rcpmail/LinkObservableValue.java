/**
 * 
 */
package rcpmail;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.jface.internal.databinding.provisional.swt.AbstractSWTObservableValue;
import org.eclipse.swt.widgets.Link;

public class LinkObservableValue extends AbstractSWTObservableValue {
	private final Link link;

	public LinkObservableValue(Link link) {
		super(link);
		this.link=link;
	}
	protected void doSetValue(final Object value) {
		String oldValue = link.getText();
		String newValue = value == null ? "" : value.toString(); //$NON-NLS-1$
		// make sure it is a link
		if(!newValue.startsWith("<a>"))
			newValue="<a>"+value+"</a>";
		link.setText(newValue);

		if (!newValue.equals(oldValue)) {
			fireValueChange(Diffs.createValueDiff(oldValue, newValue));
		}
	}

	protected Object doGetValue() {
		return link.getText();
	}

	public Object getValueType() {
		return String.class;
	}
}