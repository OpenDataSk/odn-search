package sk.opendatanode.ui.search;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;

public class SearchPage extends Panel{
    
    private static final long serialVersionUID = -8616657813069104962L;
//    private ValueMap parameters = null;
    private String query = null;
    
    @SuppressWarnings("unused")
    private boolean all = false;            // 1
    private boolean organization = false;   // 2
    private boolean procurement = false;    // 3
    private boolean polParSpo = false;      // 4 political party sponsor
    
    public SearchPage(String id, ValueMap params) {
        super(id);
        this.query = params.getString("q", "").trim();
        
        SearchForm form = new SearchForm("searchForm");
        add(form);
        form.add(new TextField<String>("query", new PropertyModel<String>(this, "query")));
        
//        addCheckbox(form, "all");
//        addCheckbox(form, "organization");
//        addCheckbox(form, "procurement");
//        addCheckbox(form, "polParSpo");
//        
//        setCheckBoxes(params.getString("type","1"));
    }

    private void setCheckBoxes(String type) {
        if (type == null || type.isEmpty())
            return;
        
        if(type.contains("1"))
            all = true;
        if(type.contains("2"))
            organization = true;
        if(type.contains("3"))
            procurement = true;
        if(type.contains("4"))
            polParSpo = true;
    }
    
    private String getType() {
        String type = "";
        if(organization)
            type+="2";
        if(procurement)
            type+="3";
        if(polParSpo)
            type+="4";
        boolean onlyAll = type.isEmpty()
                          || "234".equals(type)
                          ||"1234".equals(type);
        return onlyAll ? "1" : type;
    }

    private void addCheckbox(SearchForm form, String checkboxName) {
        form.add(new CheckBox(checkboxName, new PropertyModel<Boolean>(this, checkboxName)));        
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
            params.put("type", getType());
            
            setResponsePage(getApplication().getHomePage(), params);
        }

    }
}
