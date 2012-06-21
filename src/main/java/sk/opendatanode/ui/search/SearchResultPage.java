package sk.opendatanode.ui.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sk.opendatanode.solr.SolrServerRep;
import sk.opendatanode.solr.SolrType;
import sk.opendatanode.ui.search.facet.FacetPanel;

public class SearchResultPage extends Panel {
    
    private static final long serialVersionUID = 1965617550606209510L;
    private List<SolrDocument> resultList = new ArrayList<SolrDocument>();

    public SearchResultPage(String id, PageParameters parameters, QueryResponse response) {
        super(id);
        
        resultList.addAll(response.getResults());
        
        add(new FacetPanel("facet", parameters, response.getFacetQuery()));
        add(new Label("searchHeader", "VÝSLEDKY VYH¼ADÁVANIA"));
        
        MyPageableListView listView = new MyPageableListView("resultList", resultList, SolrServerRep.RESULTS_PER_PAGE);
        add(listView);
        
        add(new CustomPagingNavigator("paginator", listView));
    }
    
    private class MyPageableListView extends PageableListView<SolrDocument> {

        private static final long serialVersionUID = 578459835138276328L;

        public MyPageableListView(String id, List<SolrDocument> resultList, int rowsPerPage) {
            super(id, resultList, rowsPerPage);
        }

        @Override
        protected void populateItem(ListItem<SolrDocument> item) {
            final SolrDocument solrResultItem = item.getModelObject();
            int index = item.getIndex() + 1;
            
            item.add(new Label("itemNumber", index+". "));
            item.add(new ExternalLink("itemUrl",
                    "http://www.opendata.sk/item/"+ solrResultItem.get("id"),
                    getLabel(solrResultItem)));
        }

        private String getLabel(SolrDocument solrResultItem) {
            String type = (String)solrResultItem.get("type");
            // TODO document type as link on the document
            // TODO 1-2 lines from content and highlighting searched query
            if(SolrType.ORGANIZATION.getTypeString().equals(type)) {
                return (String)solrResultItem.get("name");
            } else if(SolrType.PROCUREMENT.getTypeString().equals(type)) {
                return "from: "
                        +solrResultItem.get("customer_ico")
                        +"(ICO), to:"
                        +solrResultItem.get("supplier_ico")
                        +"(ICO)";
            } else if(SolrType.POLITICAL_PARTY_DONATION.getTypeString().equals(type)) {
                return (String) solrResultItem.get("name") != null
                        ? (String) solrResultItem.get("name")
                        : solrResultItem.get("donor_name")
                          +" "
                          +solrResultItem.get("donor_surname");
            }
            return null;
        }
        
    }
}
