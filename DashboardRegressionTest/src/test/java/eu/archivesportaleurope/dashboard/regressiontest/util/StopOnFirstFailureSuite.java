/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.regressiontest.util;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.RunnerBuilder;

/**
 *
 * @author kaisar
 */
public class StopOnFirstFailureSuite extends Suite {

    public StopOnFirstFailureSuite(Class<?> klass, RunnerBuilder builder) throws InitializationError {
        super(klass, builder);
    }

    public StopOnFirstFailureSuite(Class<?> klass, Class<?>[] suiteClasses) throws InitializationError {
        super(klass, suiteClasses);
    }

    public StopOnFirstFailureSuite(Class<?> klass) throws InitializationError {
        super(klass, klass.getAnnotation(SuiteClasses.class).value());
    }

    @Override
    public void run(RunNotifier notifier) {
        notifier.addListener(new FailFastListener(notifier));
        super.run(notifier);
    }
}
