package sk.opendatanode.solr;

public enum SolrType {
    ORGANIZATION,
    PROCUREMENT,
    POLITICAL_PARTY_DONATION,
    ALL;
    
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
    
    public String getReadabeString() {
        switch (this) {
            case ORGANIZATION:
                return "organization";
            case PROCUREMENT:
                return "procurment";
            case POLITICAL_PARTY_DONATION:
                return "political party donation";
            default:
                return "all";
        }
    }
    
    public static SolrType getTypeForReadableString(String type) {
        if(ORGANIZATION.getReadabeString().equals(type))
            return ORGANIZATION;
        if(PROCUREMENT.getReadabeString().equals(type))
            return PROCUREMENT;
        if(POLITICAL_PARTY_DONATION.getReadabeString().equals(type))
            return POLITICAL_PARTY_DONATION;
        
        return ALL;
    }
    
    public static SolrType getTypeForTypeString(String type) {
        if(ORGANIZATION.getTypeString().equals(type))
            return ORGANIZATION;
        if(PROCUREMENT.getTypeString().equals(type))
            return PROCUREMENT;
        if(POLITICAL_PARTY_DONATION.getTypeString().equals(type))
            return POLITICAL_PARTY_DONATION;
        
        return ALL;
    }
}
