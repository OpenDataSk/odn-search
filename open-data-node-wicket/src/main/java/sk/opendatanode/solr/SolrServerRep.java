package sk.opendatanode.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrServerRep {
    private static final String SOLR_REPOSITORY = "http://ohio.in.eea.sk:8080/solr/"; // TODO z properties
    private static final int MAX_RESULT_ROWS = 200;
    public final static int RESULTS_PER_PAGE = 20;
    private SolrServer server = null;
        
    private static SolrServerRep instance = null;
    private static Logger logger = LoggerFactory.getLogger(SolrServerRep.class);
    
    public SolrServerRep() throws IOException {
        server = new CommonsHttpSolrServer(SOLR_REPOSITORY);
        logger.info(server+" initialized.");
    }
    
    public static SolrServerRep getInstance() throws IOException {
        if(instance == null) {
            instance = new SolrServerRep();
        }
        
        return instance;
    }
    
    public SolrDocumentList sendQuery(String query) throws SolrServerException {
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.set("defType", "dismax");
                
//        solrQuery.setStart(page * RESULTS_PER_PAGE);
//        solrQuery.setRows(RESULTS_PER_PAGE);
        solrQuery.setRows(MAX_RESULT_ROWS);
        
        QueryResponse resp = server.query(solrQuery);
        return resp.getResults();
    }
}
