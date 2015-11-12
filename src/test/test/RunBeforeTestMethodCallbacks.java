/**
 * 
 */
package test;

import java.lang.reflect.Method;

import org.junit.runners.model.Statement;
import org.springframework.test.context.TestContextManager;

/**
 * @author coldwater
 *
 */
public class RunBeforeTestMethodCallbacks extends Statement {
	private final Statement next;

	private final Object testInstance;

	private final Method testMethod;

	private final TestContextManager testContextManager;

	/**
	 * Constructs a new <code>RunBeforeTestMethodCallbacks</code> statement.
	 * 
	 * @param next the next <code>Statement</code> in the execution chain
	 * @param testInstance the current test instance (never <code>null</code>)
	 * @param testMethod the test method which is about to be executed on the
	 * test instance
	 * @param testContextManager the TestContextManager upon which to call
	 * <code>beforeTestMethod()</code>
	 */
	public RunBeforeTestMethodCallbacks(Statement next, Object testInstance, Method testMethod,
			TestContextManager testContextManager) {
		this.next = next;
		this.testInstance = testInstance;
		this.testMethod = testMethod;
		this.testContextManager = testContextManager;
	}

	/**
	 * Calls {@link TestContextManager#beforeTestMethod(Object, Method)} and
	 * then invokes the next {@link Statement} in the execution chain (typically
	 * an instance of {@link org.junit.internal.runners.statements.RunBefores
	 * RunBefores}).
	 */
	@Override
	public void evaluate() throws Throwable {
		this.testContextManager.beforeTestMethod(this.testInstance, this.testMethod);
		this.next.evaluate();
	}

}
