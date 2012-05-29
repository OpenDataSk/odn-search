package sk.opendatanode.ui;

import java.io.IOException;
import java.util.Map;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.opendatanode.ui.search.FacetSearchPage;
import sk.opendatanode.ui.search.SearchResultPage;


/**
 * Homepage
 */
public class HomePage extends WebPage {
    private static final long serialVersionUID = -3362496726021637053L;
    private String errorLog = "";
    private Logger logger = LoggerFactory.getLogger(HomePage.class);
    
    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param params
	 *            Page parameters
	 */
    public HomePage(PageParameters parameters) {

        SearchResultPage srp = new SearchResultPage("searchResultPage");
        add(srp);
        
        Map<String, Integer> facetItems = null;
        try {
            facetItems = srp.search(parameters);
        } catch (IOException e) {
            logger.error("IOException error", e);
            setErrorLog("Error: "+e.getMessage());
        } catch (SolrServerException e) {
            logger.error("SolrServerException",e);
            setErrorLog("Sorl error: "+e.getMessage());
        }
        
        FacetSearchPage sp = new FacetSearchPage("searchPage", parameters, facetItems);
        add(sp);
        
        add(new Label("errorLog", new PropertyModel<String>(this, "errorLog")));
        
    }
    
    public void setErrorLog(String errorLog) {
        this.errorLog = errorLog;
    }
    
    public String getErrorLog() {
        return errorLog;
    }
}
