package com.enonic.xp.lib.router;

import java.util.Map;
import java.util.Optional;

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

    Optional<Map<String, String>> match( final String method, final String path, final String contextPath )
    {
        final boolean matchesMethod = this.method == null || this.method.equalsIgnoreCase( method );
        if ( matchesMethod )
        {
            return this.pattern.match( path, contextPath ) ;
        }
        else
        {
            return Optional.empty();
        }
    }

    JSObject getHandler()
    {
        return this.handler;
    }
}
