package sk.opendatanode.ui.results;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.apache.solr.common.SolrDocument;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import sk.opendatanode.utils.SolrFactory;

public class GenericResultPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public GenericResultPanel(String id, SolrDocument solr) {
        super(id);


        add(new ResultMenuPanel("menu"));
        
        //Creates empty list view
        ListView<String> listView = new ListView<String>("listView1") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<String> item) {
                // no data to show
            }
        };

        //Parses solr document to map
        final Map<String, Object> map = SolrFactory.solrToMap(solr);

        if (map != null) {
            //fill list view with data
            listView = new ListView<String>("listView1", new ArrayList<String>(map.keySet())) {

                private static final long serialVersionUID = 1L;

                @Override
                protected void populateItem(ListItem<String> item) {
                    String key = item.getModel().getObject();

                    String display = "";
                    Object value = map.get(key);

                    if (value != null) {
                        display = value.toString();
                        if(value instanceof Date) {
                            display = new SimpleDateFormat("dd.MM.yyyy").format((Date) value);
                        } else if(value instanceof Boolean) {
                            display = getString(value.toString());
                        } else if(value instanceof Float) {
                            display = new DecimalFormat("#.##").format(value);
                        }
                    }

                    item.add(new Label("name", getString(key)));
                    item.add(new Label("value", display));
                }
            };
        }

        add(listView);
    }

}
