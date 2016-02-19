/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.archivesportaleurope.dashboard.regressiontest;

import eu.archivesportaleurope.dashboard.regressiontest.opendata.EnableOpenDataTest;
import eu.archivesportaleurope.dashboard.regressiontest.simple.GoogleTest;
import eu.archivesportaleurope.dashboard.regressiontest.simple.LoginTest;
import eu.archivesportaleurope.dashboard.regressiontest.util.StopOnFirstFailureSuite;
import eu.archivesportaleurope.dashboard.test.utils.SolrUtils;
import java.util.logging.Logger;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Mahbub
 */
@RunWith(Suite.class)
//@RunWith (StopOnFirstFailureSuite.class) : if stop on first fail needed
@Suite.SuiteClasses({GoogleTest.class, LoginTest.class, EnableOpenDataTest.class})
public class TestSuite001 {

    private static final Logger LOGGER = Logger.getLogger(EnableOpenDataTest.class.getName());

    @BeforeClass
    public static void setUpClass() {
        LOGGER.info("::: Removing Solr index :::");
        SolrUtils.getSolrUtil().clearAllCore();
    }
}
