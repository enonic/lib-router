package com.enonic.xp.lib.router;

import java.util.Map;

import jdk.nashorn.api.scripting.JSObject;

final class Route
{
    private final String method;

    private final RoutePattern pattern;

    private final JSObject handler;

    Route( final String method, final RoutePattern pattern, final JSObject handler )
    {
        this.method = "*".equals( method ) ? null : method;
        this.pattern = pattern;
        this.handler = handler;
    }

    boolean matches( final String method, final String path, final String contextPath )
    {
        if ( path.endsWith( "/" ) )
        {
            return matches( method, path.substring( 0, path.length() - 1 ), contextPath );
        }

        final boolean matchesMethod = ( this.method == null ) || this.method.equalsIgnoreCase( method );
        return matchesMethod && this.pattern.matches( contextPath, path );
    }

    Map<String, String> getPathParams( final String path, final String contextPath )
    {
        return this.pattern.getPathParams( path, contextPath );
    }

    JSObject getHandler()
    {
        return this.handler;
    }
}
