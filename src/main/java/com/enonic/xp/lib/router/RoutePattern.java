package com.enonic.xp.lib.router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

final class RoutePattern
{
    private final static Pattern PARAM = Pattern.compile( "\\{(?<name>\\w+)(?::(?<regex>.+))?}" );

    private final String regexp;

    private final List<String> pathParams;

    private final Cache<String, Pattern> patternCache;

    private RoutePattern( final String regexp, final List<String> pathParams )
    {
        this.regexp = regexp;
        this.pathParams = pathParams;
        this.patternCache = CacheBuilder.newBuilder().maximumSize( 10 ).build();
    }

    Map<String, String> match( final String path, final String contextPath )
    {
        final Matcher matcher = pattern( contextPath ).matcher( path );

        if ( !matcher.matches() )
        {
            return null;
        }
        final Map<String, String> map = new LinkedHashMap<>();

        for ( int i = 0; i < matcher.groupCount(); i++ )
        {
            map.put( this.pathParams.get( i ), matcher.group( i + 1 ) );
        }

        return Collections.unmodifiableMap( map );
    }

    private Pattern pattern( final String contextPath )
    {
        final String regexp = Pattern.quote( contextPath ) + this.regexp;
        try
        {
            return this.patternCache.get( regexp, () -> Pattern.compile( regexp ) );
        }
        catch ( ExecutionException e )
        {
            throw new RuntimeException( "Error generating regex for route: " + regexp, e );
        }
    }

    static RoutePattern compile( final String pattern )
    {
        final List<String> pathParams = new ArrayList<>();
        final String regexp = PARAM.matcher( pattern ).replaceAll( matchResult -> {
            pathParams.add( matchResult.group( 1 ) );
            return  "(" + Objects.requireNonNullElse( matchResult.group( 2 ), "[^/]+" ) + ")";
        } );
        return new RoutePattern( regexp, pathParams );
    }
}
