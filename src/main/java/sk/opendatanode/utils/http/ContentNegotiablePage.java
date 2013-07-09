package sk.opendatanode.utils.http;

import java.util.ArrayList;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public abstract class ContentNegotiablePage extends WebPage {

    private static final long serialVersionUID = 1L;
    
    public ContentNegotiablePage() {
        super();
        generateContentCheck(negotiateContent());
    }

    public ContentNegotiablePage(IModel<?> model) {
        super(model);
        generateContentCheck(negotiateContent());
    }

    public ContentNegotiablePage(PageParameters parameters) {
        super(parameters);
        generateContentCheck(negotiateContent());
    }

    /**
     * This method should define available content for implemented page.
     * @param contentTypes empty list
     * @return list of available content sorted by preference on server side
     */
    abstract public ArrayList<ContentTypes> defineAvailableContent(ArrayList<ContentTypes> contentTypes);

    /**
     * Handles content generation according to content type parsed form HTTP header
     * @param contentType 
     */
    abstract public void generateContent(ContentTypes contentType);

    private ContentTypes negotiateContent() {
        RequestCycle requestCycle = RequestCycle.get();
        ServletWebRequest request = (ServletWebRequest) requestCycle.getRequest();

        return ContentNegotiation.parseHttpContext(request.getHeader("Accept"),
                defineAvailableContent(new ArrayList<ContentTypes>()));
    }

    private void generateContentCheck(ContentTypes contentType) {
        if (contentType == null)
            throw new AbortWithHttpErrorCodeException(406);
        generateContent(contentType);
    }

}
