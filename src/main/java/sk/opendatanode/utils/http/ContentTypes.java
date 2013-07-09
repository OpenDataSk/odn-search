package sk.opendatanode.utils.http;

public enum ContentTypes {
    JSON("application/json"), HTML("text/html"), XML("application/xml"), PLAIN("text/plain");

    private String label;

    private ContentTypes(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
