package eu.archivesportaleurope.database.mock;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.sql.DataSource;

public class DatabaseContextFactory implements  InitialContextFactory, InitialContextFactoryBuilder {

	private DatabaseContext databaseContext;
	private DataSource dataSource;
	public DatabaseContextFactory( DataSource dataSource){
		this.dataSource = dataSource;
	}
	public Context getInitialContext(Hashtable<?, ?> environment)
            throws NamingException
    {
    	if (databaseContext == null){
    		databaseContext =  new DatabaseContext(dataSource);
    	}
        return databaseContext;
    }

    public InitialContextFactory createInitialContextFactory(
            Hashtable<?, ?> environment) throws NamingException
    {
        return new DatabaseContextFactory(dataSource);
    }
    
}
