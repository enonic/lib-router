package com.enonic.xp.lib.router;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

final class RoutePattern
{
    private final static Pattern PARAM = Pattern.compile( "\\{(\\w+)(:(.+))?\\}" );

    private final String regexp;

    private final List<String> pathParams;

    private final Cache<String, Pattern> patternCache;

    private RoutePattern( final String regexp, final List<String> pathParams )
    {
        this.regexp = regexp;
        this.pathParams = pathParams;
        this.patternCache = CacheBuilder.newBuilder().
            maximumSize( 10 ).
            build();
    }

    boolean matches( final String contextPath, final String path )
    {
        return pattern( contextPath ).matcher( path ).matches();
    }

    Map<String, String> getPathParams( final String path, final String contextPath )
    {
        final Map<String, String> map = Maps.newHashMap();
        final Matcher matcher = pattern( contextPath ).matcher( path );

        if ( !matcher.matches() )
        {
            return map;
        }

        for ( int i = 0; i < matcher.groupCount(); i++ )
        {
            map.put( this.pathParams.get( i ), matcher.group( i + 1 ) );
        }

        return map;
    }

    private Pattern pattern( final String contextPath )
    {
        final String regexp = contextPath + this.regexp;
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
        final StringBuilder regexp = new StringBuilder();
        final List<String> pathParams = Lists.newArrayList();

        for ( final String part : Splitter.on( '/' ).omitEmptyStrings().trimResults().split( pattern ) )
        {
            final Matcher matcher = PARAM.matcher( part );
            if ( !matcher.matches() )
            {
                regexp.append( "/" ).append( part );
            }
            else
            {
                pathParams.add( matcher.group( 1 ) );
                final String partExpr = matcher.group( 3 );

                regexp.append( "/(" ).append( partExpr != null ? partExpr : "[^/]+" ).append( ")" );
            }
        }

        return new RoutePattern( regexp.toString(), pathParams );
    }
}
