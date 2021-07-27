package com.enonic.xp.lib.router;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteTest
{
    @Test
    void matchesEverything()
    {
        final Route route = new Route( "*", RoutePattern.compile( "/match/everything/{path:.+}" ), null );
        final Optional<Map<String, String>> match = route.match( "GET", "context/match/everything/extra", "context" );
        assertTrue( match.isPresent() );
        assertEquals( "extra", match.get().get( "path" ) );

        final Optional<Map<String, String>> matchWithSlash = route.match( "GET", "context/match/everything/extra/", "context" );
        assertTrue( matchWithSlash.isPresent() );
        assertEquals( "extra/", matchWithSlash.get().get( "path" ) );
    }

    @Test
    void trailingSlash_no_match()
    {
        final Route route = new Route( "*", RoutePattern.compile( "/match" ), null );
        assertTrue( route.match( "GET", "context/match", "context" ).isPresent() );
        assertFalse( route.match( "GET", "context/match/", "context" ).isPresent() );
    }

    @Test
    void trailingSlash_explicit_match()
    {
        final Route route = new Route( "*", RoutePattern.compile( "/match/?" ), null );
        assertTrue( route.match( "GET", "context/match", "context" ).isPresent() );
        assertTrue( route.match( "GET", "context/match/", "context" ).isPresent() );
    }

    @Test
    void specialRegexCharInContext()
    {
        final Route route = new Route( "*", RoutePattern.compile( "/match/everything/{path:.+}" ), null );
        assertTrue( route.match( "GET", "context()/match/everything/anything", "context()" ).isPresent() );
    }

    @Test
    void emptyPatern()
    {
        final Route route = new Route( "*", RoutePattern.compile( "" ), null );
        assertTrue( route.match( "GET", "context", "context" ).isPresent() );
        assertFalse( route.match( "GET", "context/", "context" ).isPresent() );
    }
}
