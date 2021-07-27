package com.enonic.xp.lib.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jdk.nashorn.api.scripting.JSObject;

@SuppressWarnings("WeakerAccess")
public final class Router
{
    private final List<Route> list = new ArrayList<>();

    public void add( final String method, final String pattern, final JSObject handler )
    {
        final RoutePattern routePattern = RoutePattern.compile( pattern );
        this.list.add( new Route( method, routePattern, handler ) );
    }

    public RouteMatch matches( final String method, final String path, final String contextPath )
    {
        for ( final Route route : this.list )
        {
            final Optional<Map<String, String>> matches = route.match( method, path, contextPath );
            if ( matches.isPresent() )
            {
                return new RouteMatchImpl( matches.get(), route.getHandler() );
            }
        }

        if ( "HEAD".equalsIgnoreCase( method ) )
        {
            return matches( "GET", path, contextPath );
        }

        return null;
    }
}
