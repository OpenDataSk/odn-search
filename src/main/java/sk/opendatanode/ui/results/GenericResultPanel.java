package sk.opendatanode.ui.results;

import java.util.ArrayList;

import org.apache.solr.common.SolrDocument;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

public class GenericResultPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public GenericResultPanel(String id, final SolrDocument solr) {
        super(id);
       
        ListView<String> listView = new ListView<String>("listView1") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<String> item) {
                //no data to show
            }
        };
        
        if(solr != null) {        
            listView = new ListView<String>("listView1", new ArrayList<String>(solr.getFieldNames())) {
    
                private static final long serialVersionUID = 1L;
    
                @Override
                protected void populateItem(ListItem<String> item) {
                    item.add(new Label("name", item.getModel().getObject()));
                    item.add(new Label("value", solr.get(item.getModel().getObject()).toString()));
                }
            };
        }
        
        add(listView);
    }

}
