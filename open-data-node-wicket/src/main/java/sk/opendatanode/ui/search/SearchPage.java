package sk.opendatanode.ui.search;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class SearchPage extends Panel{
    
    private static final long serialVersionUID = -8616657813069104962L;
    private String query = null;
    
    public SearchPage(String id, String query) {
        super(id);
        this.query = query;
        
        SearchForm form = new SearchForm("searchForm");
        add(form);
        form.add(new TextField<String>("query", new PropertyModel<String>(this, "query")));
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
            
            setResponsePage(getApplication().getHomePage(), params);
        }
    }
}
