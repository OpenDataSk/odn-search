package sk.opendatanode.ui.search.paging;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;

import sk.opendatanode.ui.HomePage;

public class PagingNavigator extends Panel {

    private static final long serialVersionUID = -7379172012971859112L;
    private static final int MARGIN_SIZE = 4;
    
    private String query;
    private List<Integer> pages = new ArrayList<Integer>(20);
    
    private int aktPage = 1;

    public PagingNavigator(String id, String query) {
        super(id);
        this.query = query;
        
        add(getHref("first", 1, false));
        add(getHref("prev", 1, false));
        add(getHref("next", 1, false));
        add(getHref("last", 1, false));
        
        // add ostatnte stranky
        generatePagesList(0, 1);
        add(new PagingListView("navigation", pages));
    }

    private void generatePagesList(int pagesNumber, int aktPage) {        
        if(pagesNumber == 0) {
            pages.clear();
            return;
        }
        
        this.aktPage = aktPage;
        
        int start = MARGIN_SIZE>=aktPage ? 1 : aktPage - MARGIN_SIZE;
        int end = (aktPage + MARGIN_SIZE) > pagesNumber ? pagesNumber : aktPage + MARGIN_SIZE;

        for (int i = start; i <= end; i++) {
            pages.add(i);
        }
    }
    
    private Component getHref(String id, int page, boolean enabled) {
        return getHref(id, page, null, null).setEnabled(enabled);
    }

    private Component getHref(String id, int page, String idLabel, String label) {
        
        BookmarkablePageLink<HomePage> hp = new BookmarkablePageLink<HomePage>(id, HomePage.class);
        hp.setParameter("q", query);
        hp.setParameter("page", page);
        if(idLabel != null)
            hp.add(new Label(idLabel, label));
        return hp;
    }
    
    private class PagingListView extends ListView<Integer> {
        private static final long serialVersionUID = -1886885629846242156L;

        public PagingListView(String id, List<? extends Integer> list) {
            super(id, list);
        }

        @Override
        protected void populateItem(ListItem<Integer> item) {
            Integer page = item.getModelObject();
            Component comp = getHref("pageLink", page, "pageNumber", new Integer(page).toString());
            
            if(page == aktPage)
                comp.setEnabled(false);
            item.add(comp);
        }
        
    }
    
    /**
     * musim manualne refreshnut, lebo sa to obnovi az po nacitani listu
     * @param pagesNumber
     * @param aktPage
     */

    public void refreshPaging(int pagesNumber, int aktPage) {
        boolean isAktPageFirst = aktPage == 1;
        boolean isAktPageLast = aktPage == pagesNumber;
        
        replace(getHref("first", 1, !isAktPageFirst));
        replace(getHref("prev", isAktPageFirst ? 1 : aktPage-1, !isAktPageFirst));
        replace(getHref("next", isAktPageLast ? pagesNumber : aktPage+1, !isAktPageLast));
        replace(getHref("last", pagesNumber, !isAktPageLast));
        
        generatePagesList(pagesNumber, aktPage);
    }
}
