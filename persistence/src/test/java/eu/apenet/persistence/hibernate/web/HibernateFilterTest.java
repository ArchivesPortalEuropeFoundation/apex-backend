package eu.apenet.persistence.hibernate.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import eu.apenet.persistence.hibernate.AbstractHibernateTestCase;
import eu.apenet.persistence.hibernate.HibernateUtil;

public class HibernateFilterTest extends AbstractHibernateTestCase{

	@Test
	public void testFilterOpenSession() throws IOException, ServletException {
		HibernateFilter filter = new HibernateFilter();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain(){

			@Override
			public void doFilter(ServletRequest request, ServletResponse response) {
				HibernateUtil.getDatabaseSession();
			}
			
		};
		filter.doFilter(request, response, chain);
		Assert.assertEquals(true, HibernateUtil.sessionClosed());
	}

	@Test
	public void testFilterCloseOpenTransaction() throws IOException, ServletException {
		HibernateFilter filter = new HibernateFilter();
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain(){

			@Override
			public void doFilter(ServletRequest request, ServletResponse response) {
				HibernateUtil.beginDatabaseTransaction();
			}
			
		};
		filter.doFilter(request, response, chain);
		Assert.assertEquals(true, HibernateUtil.sessionClosed());
		Assert.assertEquals(true, HibernateUtil.noTransaction());
	}
	@Test
	public void testConnection(){
			HibernateUtil.beginDatabaseTransaction();
			HibernateUtil.rollbackDatabaseTransaction();
	}

}
