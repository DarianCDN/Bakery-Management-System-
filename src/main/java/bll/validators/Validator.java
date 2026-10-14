package bll.validators;

/**
 * @author Technical University of Cluj-Napoca, Romania Distributed Systems
 *          Research Laboratory
 * Source: http://dsrl.coned.utcluj.ro/
 * @since  Apr 03, 2017
 */
public interface Validator<T> {

	public void validate(T t);
}
