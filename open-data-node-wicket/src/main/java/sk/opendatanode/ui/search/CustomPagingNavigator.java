package sk.opendatanode.ui.search;

import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;

public class CustomPagingNavigator extends PagingNavigator {

    private static final long serialVersionUID = -2132040635771027481L;

    public CustomPagingNavigator(String id, IPageable pageable) {
        super(id, pageable);
    }
}
