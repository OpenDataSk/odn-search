package sk.opendatanode.ui.results;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.handler.TextRequestHandler;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import sk.opendatanode.solr.SolrServerRep;
import sk.opendatanode.ui.HomePage;
import sk.opendatanode.ui.search.SearchQueryPage;
import sk.opendatanode.utils.http.ContentNegotiablePage;
import sk.opendatanode.utils.http.ContentTypes;

public class ResultDocumentPage extends ContentNegotiablePage {

    private static final long serialVersionUID = 1L;
    private Logger logger = LoggerFactory.getLogger(HomePage.class);

    
    
    public ResultDocumentPage(PageParameters parameters) {
        super(parameters);
        add(new SearchQueryPage("searchPage", parameters));
    }

    @Override
    public ArrayList<ContentTypes> defineAvailableContent(ArrayList<ContentTypes> contentTypes) {
        contentTypes.add(ContentTypes.JSON);
        contentTypes.add(ContentTypes.HTML);
        return contentTypes;
    }

    @Override
    public void generateContent(ContentTypes contentType) {
        String param = "";
        String url = ((WebRequest) RequestCycle.get().getRequest()).getUrl().toString();
        if (url.startsWith("item/")) {
            if (url.contains("?")) {
                param = url.substring("item/".length(), url.indexOf("?"));
            } else {
                param = url.substring("item/".length());
            }
        }

        Panel resultPanel = new EmptyPanel("resultPanel");
        
        try {
            List<SolrDocument> resultList = SolrServerRep.getInstance().sendQuery(new SolrQuery("id:" + param))
                    .getResults();
            
            switch (contentType) {
                case HTML:
                    switch(resultList.size()) {
                        case 0:
                            logger.info("No results for id " + param);
                            //TODO handle no results
                            break;
                        case 1:
                            resultPanel = new GenericResultPanel("resultPanel", resultList.get(0));
                            break;
                        default:
                        logger.info("Multiple results for id " + param);
                        //TODO handle multiple results - should not happen    
                    }
                    break;
                case JSON:
//                    SolrQuery query = new SolrQuery("id:" + param);
//                    QueryResponse response = SolrServerRep.getInstance().sendQuery(query);

                    Gson gson = new Gson();
                    RequestCycle.get().scheduleRequestHandlerAfterCurrent(
                            (new TextRequestHandler(ContentTypes.JSON.getLabel(), "utf-8", gson.toJson(resultList))));
                    break;
                default:
                    // Unspecified content
                    throw new AbortWithHttpErrorCodeException(406);
            }
        } catch (SolrServerException e) {
            logger.error("SolrServerException", e);
        } catch (IOException e) {
            logger.error("IOException error", e);
        }
        
        add(resultPanel);

    }

}
