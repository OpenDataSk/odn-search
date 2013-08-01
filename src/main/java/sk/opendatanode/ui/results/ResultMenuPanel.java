package sk.opendatanode.ui.results;

import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.util.string.StringValue;

public class ResultMenuPanel extends Panel {

    private static final long serialVersionUID = 1L;
    
    public ResultMenuPanel(String id) {
        super(id);
        
        Url url = ((WebRequest) RequestCycle.get().getRequest()).getUrl(); 
        //In case of using external reference, fill base address as needed - default set to local
        System.out.println("URL: " + url.getPath());
        String base = url.getProtocol() + "://" + url.getHost() + ":" + url.getPort() + "/";
//        add(new ExternalLink("backLink", 
//                buildLinkParameters(base), 
//                getString("back")));
        add(new ExternalLink("backLink",
        		  "javascript:history.go(-1)"));
    }

    
    /**
     * Builds query part of url from object representation of parameters
     * @return url query parameters
     */
    private String buildLinkParameters(String base) {
        StringBuilder result = new StringBuilder(base + "?");
        IRequestParameters queryParameters = ((WebRequest) RequestCycle.get().getRequest()).getQueryParameters();
        for (String name : queryParameters.getParameterNames()) {
            for (StringValue value : queryParameters.getParameterValues(name)) {
                result.append(name).append("=").append(value).append("&");
            }
        }
        result.deleteCharAt(result.length() - 1);
        
        return result.toString();
    }
    
}
