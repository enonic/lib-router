package com.enonic.xp.lib.router;

import java.util.Locale;
import java.util.Map;

public final class Router
{
    public Route newRoute( final String method, final String pattern )
    {
        return new Route( method, pattern );
    }

    public static final class Route
    {
        private final String method;

        private final RoutePattern pattern;

        Route( final String method, final String pattern )
        {
            this.method = "*".equals( method ) ? null : method.toUpperCase( Locale.ROOT );
            this.pattern = RoutePattern.compile( pattern );
        }

        public Map<String, String> match( final String method, final String path, final String contextPath )
        {
            final boolean matchesMethod = this.method == null || this.method.equalsIgnoreCase( method );
            if ( matchesMethod )
            {
                return this.pattern.match( path, contextPath );
            }
            else
            {
                return null;
            }
        }
    }
}
