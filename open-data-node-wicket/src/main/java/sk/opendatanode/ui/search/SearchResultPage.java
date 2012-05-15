package sk.opendatanode.ui.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrDocument;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PageableListView;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.value.ValueMap;

import sk.opendatanode.solr.SolrServerRep;

public class SearchResultPage extends Panel {
    
    private static final long serialVersionUID = 1965617550606209510L;
    private List<SolrDocument> resultList = new ArrayList<SolrDocument>();

    public SearchResultPage(String id, ValueMap parameters) {
        super(id);
        
        MyPageableListView listView = new MyPageableListView("resultList", resultList, SolrServerRep.RESULTS_PER_PAGE);
        add(listView);
        
        add(new PagingNavigator("navigator", listView));
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
                    (String)solrResultItem.get("name")));
        }
        
    }

    public void search(ValueMap parameters) throws IOException, SolrServerException {
        final String query = parameters.getString("q", "").trim();
        resultList.addAll(SolrServerRep.getInstance().sendQuery(query));
    }
}
