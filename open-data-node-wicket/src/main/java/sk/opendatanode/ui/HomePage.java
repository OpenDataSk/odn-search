package sk.opendatanode.ui;

import java.util.Set;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.util.value.ValueMap;

/**
 * Homepage
 */
public class HomePage extends WebPage {

	private static final long serialVersionUID = 1L;

    /**
	 * Constructor that is invoked when page is invoked without a session.
	 * 
	 * @param parameters
	 *            Page parameters
	 */
    public HomePage(final PageParameters parameters) {
        add(new SearchForm("searchForm"));
        add(new Label("results", "tu budu vysledky"));
    }
    
    public final class SearchForm extends Form {
        private static final long serialVersionUID = 3514316289841517621L;

        public SearchForm(String id) {
            super(id, new CompoundPropertyModel(new ValueMap()));
            add(new TextField("query").setType(String.class));
        }
        
        @Override
        protected void onSubmit() {
            ValueMap values = (ValueMap) getModelObject();
            
            String zadanaQuery = (String) values.get("query");
        }
        
    }
}
