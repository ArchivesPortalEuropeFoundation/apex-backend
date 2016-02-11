/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.regressiontest;

import eu.archivesportaleurope.dashboard.regressiontest.opendata.EnableOpenDataTest;
import eu.archivesportaleurope.dashboard.regressiontest.util.StopOnFirstFailureSuite;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Mahbub
 */
@RunWith(Suite.class)
//@RunWith (StopOnFirstFailureSuite.class) : if stop on first fail needed
@Suite.SuiteClasses({/*GoogleTest.class, LoginTest.class,*/EnableOpenDataTest.class})
public class TestSuite001 {

}
