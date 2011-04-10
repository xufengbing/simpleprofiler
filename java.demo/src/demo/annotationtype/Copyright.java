package demo.annotationtype;

/**
 * Associates a copyright notice with the annotated API element.
 *
 * In annotations with a single element, the element should be named value, as
 * shown below:
 */
public @interface Copyright {
	String value();
}