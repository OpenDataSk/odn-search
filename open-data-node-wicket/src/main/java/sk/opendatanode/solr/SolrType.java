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
    
    public String getReadableString() {
        switch (this) {
            case ORGANIZATION:
                return "Organiz·cie";
            case PROCUREMENT:
                return "Obstar·vanie";
            case POLITICAL_PARTY_DONATION:
                return "Sponzori politick˝ch str·n";
            default:
                return "Prehladaù vöetky datasety";
        }
    }
    
    public static SolrType getTypeFromReadableString(String text) {
        if (text == null || text.isEmpty()) {
            return null;            
        }
        
        if(text.equals(ORGANIZATION.getReadableString()))
            return ORGANIZATION;
        else if(text.equals(PROCUREMENT.getReadableString()))
            return PROCUREMENT;
        else if(text.equals(POLITICAL_PARTY_DONATION.getReadableString()))
            return POLITICAL_PARTY_DONATION;
        else if(text.equals(ALL.getReadableString()))
            return ALL;
        
        return null;
    }
}
