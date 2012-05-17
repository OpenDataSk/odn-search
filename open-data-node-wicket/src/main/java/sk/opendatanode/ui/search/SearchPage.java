package sk.opendatanode.ui.search;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;

import sk.opendatanode.solr.SolrType;

public class SearchPage extends Panel{
    
    private static final long serialVersionUID = -8616657813069104962L;
    private String query = null;
    
    private static final List<String> TYPES = Arrays
            .asList(new String[] {SolrType.ALL.getReadabeString(),
                    SolrType.ORGANIZATION.getReadabeString(),
                    SolrType.PROCUREMENT.getReadabeString(),
                    SolrType.POLITICAL_PARTY_DONATION.getReadabeString()});
    
    private String selected = SolrType.ALL.getReadabeString();
    
    
    public SearchPage(String id, ValueMap params) {
        super(id);
        this.query = params.getString("q", "").trim();
        
        SearchForm form = new SearchForm("searchForm");
        form.add(new TextField<String>("query", new PropertyModel<String>(this, "query")));
        
        RadioChoice<String> fieldType = new RadioChoice<String>("type",
                new PropertyModel<String>(this, "selected"), TYPES);
        form.add(fieldType);
        add(form);
        
        selected = SolrType.getTypeForTypeString(params.getString("type")).getReadabeString();
    }

    private final class SearchForm extends Form<Void> {
        private static final long serialVersionUID = 3514316289841517621L;

        public SearchForm(String id) {
            super(id);
        }
        
        @Override
        protected void onSubmit() {
            PageParameters params = new PageParameters();
            
            if (query != null && !query.isEmpty())
                params.add("q", query);
            params.put("type", SolrType.getTypeForReadableString(selected).getTypeString());
            
            setResponsePage(getApplication().getHomePage(), params);
        }
    }
}
