package eu.archivesportaleurope.database.mock;

import javax.naming.CompositeName;
import javax.naming.InitialContext;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DatabaseContext extends InitialContext {
	private DataSource dataSource;

    public DatabaseContext(DataSource dataSource) throws NamingException {
    	
    	this.dataSource = dataSource;
    }
    @Override
    public Object lookup(String name) throws NamingException
    {
    	return dataSource;
       
    }

	@Override
	public NameParser getNameParser(String name) throws NamingException {
		NameParser nameParser = new NameParser() {
			
			@Override
			public Name parse(String name) throws NamingException {
				return new CompositeName(name);
			}
		};
		return nameParser;
	}

	@Override
	public Object lookup(Name name) throws NamingException {
    	return dataSource;
	}  

    
}
