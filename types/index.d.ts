import type { Request, Response } from "@enonic-types/core";

/**
 * A request received by a route handler.
 *
 * The router populates `pathParams` from the matched route's placeholders
 * (e.g. `/persons/{id}` → `req.pathParams.id`) before invoking the handler.
 */
export type RouterRequest = Request & {
    pathParams: Record<string, string | undefined>;
};

/**
 * Handler invoked when a route matches.
 */
export type RouteHandler = (req: RouterRequest) => Response;

/**
 * Filter invoked in the dispatch chain before route matching.
 * Call `next(req)` to continue, or return a response to short-circuit.
 */
export type Filter = (
    req: Request,
    next: (req: Request) => Response
) => Response;

/**
 * Path pattern to match. Either a single pattern or an array of patterns
 * that share the same handler.
 */
export type Pattern = string | string[];

export interface Router {
    /**
     * Adds a route to this router.
     *
     * @param method Method to match. `'*'` for any method.
     * @param pattern Path pattern (or patterns) to match.
     * @param handler Handler to execute on match.
     */
    route(method: string, pattern: Pattern, handler: RouteHandler): void;

    /**
     * Adds a route that matches the GET method.
     */
    get(pattern: Pattern, handler: RouteHandler): void;

    /**
     * Adds a route that matches the POST method.
     */
    post(pattern: Pattern, handler: RouteHandler): void;

    /**
     * Adds a route that matches the DELETE method.
     */
    delete(pattern: Pattern, handler: RouteHandler): void;

    /**
     * Adds a route that matches the PUT method.
     */
    put(pattern: Pattern, handler: RouteHandler): void;

    /**
     * Adds a route that matches the HEAD method.
     */
    head(pattern: Pattern, handler: RouteHandler): void;

    /**
     * Adds a route that matches the PATCH method.
     */
    patch(pattern: Pattern, handler: RouteHandler): void;

    /**
     * Adds a route that matches all methods.
     */
    all(pattern: Pattern, handler: RouteHandler): void;

    /**
     * Adds a filter to this router. Filters run in insertion order before
     * route matching.
     */
    filter(filter: Filter): void;

    /**
     * Dispatch the request through the filter chain and matched route.
     * Returns a 404 response when no route matches.
     */
    dispatch(req: Request): Response;
}

/**
 * Creates a new router instance.
 */
declare function newRouter(): Router;

export default newRouter;
