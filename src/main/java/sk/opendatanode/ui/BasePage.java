package sk.opendatanode.ui;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import sk.opendatanode.ui.search.SearchQueryPage;

public class BasePage extends WebPage {

	private static final long serialVersionUID = -4439295062431616848L;
	
	public BasePage(final PageParameters parameters) {
		SearchQueryPage sp = new SearchQueryPage("searchPage", parameters);
        add(sp);
        
		add(new BookmarkablePageLink<WebPage>("about", AboutPage.class));
		add(new BookmarkablePageLink<WebPage>("contact", ContactPage.class));
	}

}
