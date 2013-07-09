package sk.opendatanode.utils.http;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.apache.wicket.request.http.flow.AbortWithHttpErrorCodeException;
import org.junit.Test;

public class ContentNegotiationTest {

    private ArrayList<ContentTypes> avaliableTypes;
    private String httpContext;
    private ContentTypes expResult;

    /**
     * Basic test
     */
    @Test
    public void test1() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.JSON);
        avaliableTypes.add(ContentTypes.HTML);

        httpContext = "text/plain,text/html,application/json;q=0.8";
        expResult = ContentTypes.HTML;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Quality parameter check
     */
    @Test
    public void test2() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.JSON);
        avaliableTypes.add(ContentTypes.HTML);

        httpContext = "text/plain,text/html,application/json;q=1.0";
        expResult = ContentTypes.JSON;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Quality priority check
     */
    @Test
    public void test3() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.HTML);

        httpContext = "text/plain,text/html;q=0.2,application/json;q=0.8";
        expResult = ContentTypes.HTML;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Extended data in context
     */
    @Test
    public void test4() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.JSON);
        avaliableTypes.add(ContentTypes.HTML);

        httpContext = "text/plain;level=1,text/html;q=1;level=1,application/json;q=0.8;level=4";
        expResult = ContentTypes.HTML;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Asterisk subtype simple test
     */
    @Test
    public void test5() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.JSON);
        avaliableTypes.add(ContentTypes.HTML);

        httpContext = "text/*,application/json;q=0.8";
        expResult = ContentTypes.HTML;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Asterisk subtype comparison test
     */
    @Test
    public void test6() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.JSON);
        avaliableTypes.add(ContentTypes.HTML);

        httpContext = "text/*;q=0.8,application/*;q=0.8";
        expResult = ContentTypes.JSON;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Asterisk subtype non-existing test
     */
    @Test
    public void test7() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.JSON);

        httpContext = "text/*,application/json;q=0.8";
        expResult = ContentTypes.JSON;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Asterisk type simple test
     */
    @Test
    public void test8() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.JSON);
        avaliableTypes.add(ContentTypes.HTML);

        httpContext = "*/html,application/json;q=0.8";
        expResult = ContentTypes.HTML;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Asterisk type comparison test
     */
    @Test
    public void test9() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.JSON);
        avaliableTypes.add(ContentTypes.HTML);

        httpContext = "*/html;q=0.8,*/json;q=0.8";
        expResult = ContentTypes.JSON;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Asterisk type non-existing test
     */
    @Test
    public void test10() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.JSON);

        httpContext = "*/html,application/json;q=0.8";
        expResult = ContentTypes.JSON;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Asterisk type and subtype search
     */
    @Test
    public void test11() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.XML);

        httpContext = "*/html,*/*;q=0.8";
        expResult = ContentTypes.XML;

        try {
            assertEquals(expResult, ContentNegotiation.parseHttpContext(httpContext, avaliableTypes));
        } catch (AbortWithHttpErrorCodeException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Unavailable content
     */
    @Test
    public void test12() {
        avaliableTypes = new ArrayList<ContentTypes>();
        avaliableTypes.add(ContentTypes.XML);

        httpContext = "text/plain,text/html,application/json;q=0.8";
        expResult = null;

        try {
            ContentNegotiation.parseHttpContext(httpContext, avaliableTypes);
            fail("Exception should be thrown");
        } catch (AbortWithHttpErrorCodeException e) {
            AbortWithHttpErrorCodeException expExc = new AbortWithHttpErrorCodeException(406,
                    "No such content available: " + httpContext);
            assertTrue((e.getClass().equals(expExc.getClass()) && (e.getMessage().equals(expExc.getMessage()))));
        }

    }
}
