/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.regressiontest.util;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;

/**
 *
 * @author kaisar
 */
public class FailFastListener extends RunListener {

    private RunNotifier runNotifier;

    public FailFastListener(RunNotifier runNotifier) {
        super();
        this.runNotifier = runNotifier;
    }

    @Override
    public void testFailure(Failure failure) throws Exception {
        this.runNotifier.pleaseStop();
    }

}
