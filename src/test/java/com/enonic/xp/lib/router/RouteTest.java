package com.enonic.xp.lib.router;

import java.util.Map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class RouteTest
{
    @Test
    void param()
    {
        final Router.Route route = new Router.Route( "*", "/persons/{id}" );
        final Map<String, String> match = route.match( "GET", "context/persons/1", "context" );
        assertNotNull( match );
        assertEquals( "1", match.get( "id" ) );
    }

    @Test
    void matchesEverything()
    {
        final Router.Route route = new Router.Route( "*", "/match/everything/{path:.+}" );
        final Map<String, String> match = route.match( "GET", "context/match/everything/extra", "context" );
        assertNotNull( match );
        assertEquals( "extra", match.get( "path" ) );

        final Map<String, String> matchWithSlash = route.match( "GET", "context/match/everything/extra/", "context" );
        assertNotNull( matchWithSlash );
        assertEquals( "extra/", matchWithSlash.get( "path" ) );
    }

    @Test
    void noTrailingSlash()
    {
        final Router.Route route = new Router.Route( "*", "/match" );
        assertNotNull( route.match( "GET", "context/match", "context" ) );
        assertNull( route.match( "GET", "context/match/", "context" ) );
    }

    @Test
    void trailingSlash()
    {
        final Router.Route route = new Router.Route( "*", "/match/" );
        assertNull( route.match( "GET", "context/match", "context" ) );
        assertNotNull( route.match( "GET", "context/match/", "context" ) );
    }

    @Test
    void optionalTrailingSlash()
    {
        final Router.Route route = new Router.Route( "*", "/match/?" );
        assertNotNull( route.match( "GET", "context/match", "context" ) );
        assertNotNull( route.match( "GET", "context/match/", "context" ) );
    }

    @Test
    void specialRegexCharInContext()
    {
        final Router.Route route = new Router.Route( "*", "/match/everything/{path:.+}" );
        assertNotNull( route.match( "GET", "context()/match/everything/anything", "context()" ) );
    }

    @Test
    void emptyPattern()
    {
        final Router.Route route = new Router.Route( "*", "" );
        assertNotNull( route.match( "GET", "context", "context" ) );
        assertNull( route.match( "GET", "context/", "context" ) );
    }

    @Test
    void pathAsPattern()
    {
        final Router.Route route = new Router.Route( "*", "/a{path:/test1|/test2}" );
        final Map<String, String> match = route.match( "GET", "context/a/test1", "context" );
        assertNotNull( match );
        assertEquals( "/test1", match.get( "path" ) );
    }
}
