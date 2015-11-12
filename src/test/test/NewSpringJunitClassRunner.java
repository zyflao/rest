/**
 * 
 */
package test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.internal.runners.model.ReflectiveCallable;
import org.junit.internal.runners.statements.Fail;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.TestContextManager;


/**
 * 解决spring2.5.2对于junit4.4的
 * org/junit/Assume$AssumptionViolatedException; nested exception is java.lang.NoClassDefFoundError: 
 * 问题
 * @author coldwater
 *
 */
public class NewSpringJunitClassRunner extends BlockJUnit4ClassRunner {
	private static final Log logger = LogFactory.getLog(NewSpringJunitClassRunner.class);

	private final TestContextManager testContextManager;


	/**
	 * Constructs a new <code>SpringJUnit4ClassRunner</code> and initializes a
	 * {@link TestContextManager} to provide Spring testing functionality to
	 * standard JUnit tests.
	 * @param clazz the test class to be run
	 * @see #createTestContextManager(Class)
	 */
	public NewSpringJunitClassRunner(Class<?> clazz) throws InitializationError {
		super(clazz);
		if (logger.isDebugEnabled()) {
			logger.debug("SpringJUnit4ClassRunner constructor called with [" + clazz + "].");
		}
		this.testContextManager = createTestContextManager(clazz);
	}

	/**
	 * Creates a new {@link TestContextManager} for the supplied test class and
	 * the configured <em>default <code>ContextLoader</code> class name</em>.
	 * Can be overridden by subclasses.
	 * @param clazz the test class to be managed
	 * @see #getDefaultContextLoaderClassName(Class)
	 */
	protected TestContextManager createTestContextManager(Class<?> clazz) {
		return new TestContextManager(clazz);
	}

	/**
	 * Get the {@link TestContextManager} associated with this runner.
	 */
	protected final TestContextManager getTestContextManager() {
		return this.testContextManager;
	}

	/**
	 * Get the name of the default <code>ContextLoader</code> class to use for
	 * the supplied test class. The named class will be used if the test class
	 * does not explicitly declare a <code>ContextLoader</code> class via the
	 * <code>&#064;ContextConfiguration</code> annotation.
	 * <p>The default implementation returns <code>null</code>, thus implying use
	 * of the <em>standard</em> default <code>ContextLoader</code> class name.
	 * Can be overridden by subclasses.
	 * @param clazz the test class
	 * @return <code>null</code>
	 */
	protected String getDefaultContextLoaderClassName(Class<?> clazz) {
		return null;
	}


	/**
	 * Delegates to the parent implementation for creating the test instance and
	 * then allows the {@link #getTestContextManager() TestContextManager} to
	 * prepare the test instance before returning it.
	 * @see TestContextManager#prepareTestInstance(Object)
	 */
	@Override
	protected Object createTest() throws Exception {
		Object testInstance = super.createTest();
		getTestContextManager().prepareTestInstance(testInstance);
		return testInstance;
	}

	/**
	 * Performs the same logic as
	 * {@link BlockJUnit4ClassRunner#runChild(FrameworkMethod, RunNotifier)},
	 * except that tests are determined to be <em>ignored</em> by
	 * {@link #isTestMethodIgnored(FrameworkMethod)}.
	 */
	@Override
	protected void runChild(FrameworkMethod frameworkMethod, RunNotifier notifier) {
		EachTestNotifier eachNotifier = springMakeNotifier(frameworkMethod, notifier);
		
		eachNotifier.fireTestStarted();
		try {
			methodBlock(frameworkMethod).evaluate();
		}
		catch (AssumptionViolatedException e) {
			eachNotifier.addFailedAssumption(e);
		}
		catch (Throwable e) {
			eachNotifier.addFailure(e);
		}
		finally {
			eachNotifier.fireTestFinished();
		}
	}

	/**
	 * <code>springMakeNotifier()</code> is an exact copy of
	 * {@link BlockJUnit4ClassRunner BlockJUnit4ClassRunner's}
	 * <code>makeNotifier()</code> method, but we have decided to prefix it with
	 * "spring" and keep it <code>private</code> in order to avoid the
	 * compatibility clashes that were introduced in JUnit between versions 4.5,
	 * 4.6, and 4.7.
	 */
	private EachTestNotifier springMakeNotifier(FrameworkMethod method, RunNotifier notifier) {
		Description description = describeChild(method);
		return new EachTestNotifier(notifier, description);
	}

	/**
	 * Augments the default JUnit behavior
	 * {@link #withPotentialRepeat(FrameworkMethod, Object, Statement) with
	 * potential repeats} of the entire execution chain.
	 * <p>Furthermore, support for timeouts has been moved down the execution chain
	 * in order to include execution of {@link org.junit.Before &#064;Before}
	 * and {@link org.junit.After &#064;After} methods within the timed
	 * execution. Note that this differs from the default JUnit behavior of
	 * executing <code>&#064;Before</code> and <code>&#064;After</code> methods
	 * in the main thread while executing the actual test method in a separate
	 * thread. Thus, the end effect is that <code>&#064;Before</code> and
	 * <code>&#064;After</code> methods will be executed in the same thread as
	 * the test method. As a consequence, JUnit-specified timeouts will work
	 * fine in combination with Spring transactions. Note that JUnit-specific
	 * timeouts still differ from Spring-specific timeouts in that the former
	 * execute in a separate thread while the latter simply execute in the main
	 * thread (like regular tests).
	 * @see #possiblyExpectingExceptions(FrameworkMethod, Object, Statement)
	 * @see #withBefores(FrameworkMethod, Object, Statement)
	 * @see #withAfters(FrameworkMethod, Object, Statement)
	 * @see #withPotentialTimeout(FrameworkMethod, Object, Statement)
	 * @see #withPotentialRepeat(FrameworkMethod, Object, Statement)
	 */
	@SuppressWarnings("deprecation")
	@Override
	protected Statement methodBlock(FrameworkMethod frameworkMethod) {
		Object testInstance;
		try {
			testInstance = new ReflectiveCallable() {
				@Override
				protected Object runReflectiveCall() throws Throwable {
					return createTest();
				}
			}.run();
		}
		catch (Throwable ex) {
			return new Fail(ex);
		}

		Statement statement = methodInvoker(frameworkMethod, testInstance);
		statement = possiblyExpectingExceptions(frameworkMethod, testInstance, statement);
		statement = withBefores(frameworkMethod, testInstance, statement);
		statement = withAfters(frameworkMethod, testInstance, statement);
		statement = withPotentialTimeout(frameworkMethod, testInstance, statement);

		return statement;
	}




	/**
	 * Wraps the {@link Statement} returned by the parent implementation with a
	 * {@link RunBeforeTestMethodCallbacks} statement, thus preserving the
	 * default functionality but adding support for the Spring TestContext
	 * Framework.
	 * @see RunBeforeTestMethodCallbacks
	 */
	@Override
	@SuppressWarnings("deprecation")
	protected Statement withBefores(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
		Statement junitBefores = super.withBefores(frameworkMethod, testInstance, statement);
		return new RunBeforeTestMethodCallbacks(junitBefores, testInstance, frameworkMethod.getMethod(),
			getTestContextManager());
	}

	/**
	 * Wraps the {@link Statement} returned by the parent implementation with a
	 * {@link RunAfterTestMethodCallbacks} statement, thus preserving the
	 * default functionality but adding support for the Spring TestContext
	 * Framework.
	 * @see RunAfterTestMethodCallbacks
	 */
	@Override
	@SuppressWarnings("deprecation")
	protected Statement withAfters(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
		Statement junitAfters = super.withAfters(frameworkMethod, testInstance, statement);
		return new RunAfterTestMethodCallbacks(junitAfters, testInstance, frameworkMethod.getMethod(),
			getTestContextManager());
	}

	
}
