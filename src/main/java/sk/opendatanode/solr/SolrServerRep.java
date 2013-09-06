/* Copyright (C) 2012 Martin Virag <martin.virag@eea.sk>
 *
 * This file is part of Open Data Node.
 *
 * Open Data Node is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Open Data Node is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Open Data Node.  If not, see <http://www.gnu.org/licenses/>.
 */

package sk.opendatanode.solr;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrServerRep {
    private static final String SOLR_REPOSITORY = "http://localhost:8080/solr/"; // TODO take that from properties so that it can be changed during development or deployment without the need to touch the code
    public static final int MAX_RESULT_ROWS = 1000;
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
    
    public QueryResponse sendQuery(SolrQuery query) throws SolrServerException {
        return server.query(query);
    }
}
