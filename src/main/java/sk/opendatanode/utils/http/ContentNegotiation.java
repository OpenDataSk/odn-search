package sk.opendatanode.utils.http;

import java.util.ArrayList;

import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;

public class ContentNegotiation {

    /**
     * Parses data of Accept field in HTTP header and finds most suitable content form specified content list
     * 
     * @param content of Accept field in HTTP header
     * @param avalContentTypes supported content types
     * @return most suitable content type
     */
    protected static ContentTypes parseHttpContext(String content, ArrayList<ContentTypes> avalContentTypes) {

        String[] tokens = content.split(",");

        return matchContent(fillContentTree(tokens), fillContentTree(avalContentTypes), content);
    }

    private static ContentTree fillContentTree(ArrayList<ContentTypes> contentTypes) {
        String[] tokens = new String[contentTypes.size()];

        for (int i = 0; i < contentTypes.size(); i++) {
            tokens[i] = contentTypes.get(i).getLabel();
        }

        return fillContentTree(tokens);
    }

    private static ContentTree fillContentTree(String[] tokens) {
        ContentTree contentTree = new ContentTree();

        for (String token : tokens) {
            String type;
            String subType;
            Float quality = new Float(1);
            int index, index2, index3;

            index = token.indexOf("/");
            index2 = token.indexOf(";");
            index3 = token.indexOf("q=");

            if (index == -1) {
                // Illegal header data
                throw new AbortWithHttpErrorCodeException(406, "Illegal header format");
            }

            if (index2 == -1) {
                subType = token.substring(index + 1).trim();
            } else {
                subType = token.substring(index + 1, index2).trim();
            }

            if (index3 != -1) {
                index2 = token.indexOf(";", index3);
                if (index2 == -1) {
                    quality = Float.parseFloat(token.substring(index3 + 2));
                } else {
                    quality = Float.parseFloat(token.substring(index3 + 2, index2));
                }
            }

            if (quality > 1 || quality < 0) {
                throw new AbortWithHttpErrorCodeException(406, "Quality parameter out of range <0,1>");
            }

            type = token.substring(0, index).trim();
            contentTree.addContent(type, subType, quality);
        }

        return contentTree;
    }

    /*
     * Matches requested content with available
     */
    private static ContentTypes matchContent(ContentTree contentTree, ContentTree avalContentTypes, String content) {

        ContentTypes contentType = contentTree.searchFromRoots(avalContentTypes.getFirstRoot());
        if (contentType == null) {
            throw new AbortWithHttpErrorCodeException(406, "No such content available: " + content);
        }

        return contentType;
    }
}
