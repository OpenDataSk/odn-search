package sk.opendatanode.utils;

import org.apache.solr.client.solrj.SolrQuery;

public abstract class SolrQueryHelper {
    
    public static SolrQuery setQuery(String query, String type) {
        SolrQuery solrQuery = new SolrQuery(query);
        solrQuery.set("defType", "edismax");
        
        // zadane vsetky "all" => nie je co riesit
        if(type == null) {
            return solrQuery;
        } else {            
            // nastavime to ako filter
            solrQuery.add("fq", "type:"+type);
        }
        
        return solrQuery;
    }
}
