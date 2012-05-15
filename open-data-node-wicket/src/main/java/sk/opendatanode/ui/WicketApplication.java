package sk.opendatanode.ui;

import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

import sk.opendatanode.utils.AppProperties;

/**
 * Application object for your web application. If you want to run this application without deploying, run the Start class.
 * 
 * @see wicket.myproject.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{    
    /**
     * Constructor
     */
	public WicketApplication() {
	    // loading properties
        AppProperties.getInstance("solr-query.properties");
	}
	
	/**
	 * @see wicket.Application#getHomePage()
	 */
	public Class<? extends Page> getHomePage() {
		return HomePage.class;
	}

}
