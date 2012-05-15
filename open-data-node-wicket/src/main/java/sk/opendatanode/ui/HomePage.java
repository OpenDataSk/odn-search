package sk.opendatanode.ui;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.opendatanode.ui.search.SearchPage;
import sk.opendatanode.ui.search.SearchResultPage;


/**
 * Homepage
 */
public class HomePage extends WebPage {
    private String errorLog = "";
    private Logger logger = LoggerFactory.getLogger(HomePage.class);
    
    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param params
	 *            Page parameters
	 */
    public HomePage(PageParameters parameters) {
        ValueMap params = new ValueMap(parameters);
        
        SearchPage sp = new SearchPage("searchPage", params);
        SearchResultPage srp = new SearchResultPage("searchResultPage", params);
        
        add(sp);
        add(srp);
        
        add(new Label("errorLog", new PropertyModel<String>(this, "errorLog")));
        
        try {
            srp.search(params);
        } catch (IOException e) {
            logger.error("IOException error", e);
            setErrorLog("Error: "+e.getMessage());
        } catch (SolrServerException e) {
            logger.error("SolrServerException",e);
            setErrorLog("Sorl error: "+e.getMessage());
        }
    }
    
    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }
    
    public String getErrorLog() {
        return errorLog;
    }
}
