package sk.opendatanode.ui.search;

import java.util.Map;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sk.opendatanode.ui.search.facet.SearchFacetPanel;


public class FacetSearchPage extends Panel{

    private static final long serialVersionUID = 9058238555708124775L;
    private String query = null;

    public FacetSearchPage(String id, PageParameters params, Map<String, Integer> facetItems) {
        super(id);
        this.query  = params.get("q").toString("").trim();
        
        SearchForm form = new SearchForm("searchForm");
        form.add(new TextField<String>("query", new PropertyModel<String>(this, "query")));
        
        form.add(new SearchFacetPanel("facet", params, facetItems));
        
        add(form);
    }

    private final class SearchForm extends Form<Void> {

        private static final long serialVersionUID = 7442088284418137882L;

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
