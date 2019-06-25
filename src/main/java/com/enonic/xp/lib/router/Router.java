package com.enonic.xp.lib.router;

import java.util.List;

import com.google.common.collect.Lists;

import jdk.nashorn.api.scripting.JSObject;

@SuppressWarnings("WeakerAccess")
public final class Router
{
    private final List<Route> list;

    public Router()
    {
        this.list = Lists.newArrayList();
    }

    public void add( final String method, final String pattern, final JSObject handler )
    {
        final RoutePattern routePattern = RoutePattern.compile( pattern );
        this.list.add( new Route( method, routePattern, handler ) );
    }

    public RouteMatch matches( final String method, final String path, final String contextPath )
    {
        for ( final Route route : this.list )
        {
            if ( route.matches( method, path, contextPath ) )
            {
                return newRouteMatch( route, path, contextPath );
            }
        }

        if ( "HEAD".equalsIgnoreCase( method ) )
        {
            return matches( "GET", path, contextPath );
        }

        return null;
    }

    private RouteMatch newRouteMatch( final Route route, final String path, final String contextPath )
    {
        return new RouteMatchImpl( route.getPathParams( path, contextPath ), route.getHandler() );
    }
}
