package sk.opendatanode.ui;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.PropertyModel;

import sk.opendatanode.ui.search.SearchPage;
import sk.opendatanode.ui.search.SearchResultPage;


/**
 * Homepage
 */
public class HomePage extends WebPage {
    @SuppressWarnings("unused")
    private String errorLog = "";
    
    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    public HomePage(final PageParameters parameters) {
        final String query = parameters.getString("q", "").trim();
        final int page = parameters.getInt("page", 1);
        
        SearchPage sp = new SearchPage("searchPage", query);
        SearchResultPage srp = new SearchResultPage("searchResultPage", query, page);
        
        add(sp);
        add(srp);
        
        add(new Label("errorLog", new PropertyModel<String>(this, "errorLog")));
        
        try {
            srp.search(query, page);
        } catch (IOException e) {
            errorLog = "Error: "+e.getMessage();
            e.printStackTrace();
        } catch (SolrServerException e) {
            errorLog = "Sorl error: "+e.getMessage();
            e.printStackTrace();
        }
    }
    
}
