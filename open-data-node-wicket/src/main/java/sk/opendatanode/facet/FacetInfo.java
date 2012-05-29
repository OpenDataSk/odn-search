package sk.opendatanode.facet;

import java.io.Serializable;
import java.util.List;

public class FacetInfo implements Serializable {

    private static final long serialVersionUID = -1556257426649356316L;
    private String name;
    private List<FacetItem>  itemList = null;
    
    public FacetInfo(String name, List<FacetItem> itemList) {
        super();
        this.name = name;
        this.itemList = itemList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FacetItem> getItemList() {
        return itemList;
    }

    public void setItemList(List<FacetItem> itemList) {
        this.itemList = itemList;
    }
}
