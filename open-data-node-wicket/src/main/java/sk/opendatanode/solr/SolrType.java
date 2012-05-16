package sk.opendatanode.solr;

public enum SolrType {
    ORGANIZATION,
    PROCUREMENT,
    POLITICAL_PARTY_DONATION;
    
    public String getTypeString() {
        switch (this) {
            case ORGANIZATION:
                return "ORGANIZATION_RECORD";
            case PROCUREMENT:
                return "PROCUREMENT_RECORD";
            case POLITICAL_PARTY_DONATION:
                return "POLITICAL_PARTY_DONATION_RECORD";
            default:
                return null;
        }
    }
}
