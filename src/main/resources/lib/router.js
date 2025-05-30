/* global exports */

/**
 * Router javascript functions.
 *
 * @example
 * // Creates a new router.
 * var router = require('/lib/router')();
 *
 * @module router
 */

/**
 * Creates a new router.
 *
 * @constructor
 */
function Router() {
    this.router = __.newBean('com.enonic.xp.lib.router.Router');
    this.filters = [];
    this.routes = [];
}

/**
 * Adds a route to this router.
 *
 * @example
 * // Add a route.
 * router.route('GET', '/persons', function(req) {
 *  ...
 * });
 *
 * @param {String} method Method to match. '*' for everyone.
 * @param {String|String[]} pattern Path pattern to match.
 * @param handler Handler to execute on match.
 */
Router.prototype.route = function (method, pattern, handler) {
    const router = this.router;
    const routes = this.routes;
    [].concat(pattern || '').forEach(p => routes.push([router.newRoute(method, p), handler]));
};

/**
 * Adds a route that matches GET method.
 *
 * @example
 * // Add a route.
 * router.get('/persons', function(req) {
 *  ...
 * });
 *
 * @param {String|String[]} pattern Path pattern to match.
 * @param handler Handler to execute on match.
 */
Router.prototype.get = function (pattern, handler) {
    this.route('GET', pattern, handler);
};

/**
 * Adds a route that matches POST method.
 *
 * @example
 * // Add a route.
 * router.post('/persons', function(req) {
 *  ...
 * });
 *
 * @param {String|String[]} pattern Path pattern to match.
 * @param handler Handler to execute on match.
 */
Router.prototype.post = function (pattern, handler) {
    this.route('POST', pattern, handler);
};

/**
 * Adds a route that matches DELETE method.
 *
 * @example
 * // Add a route.
 * router.delete('/persons/{id}', function(req) {
 *  ...
 * });
 *
 * @param {String|String[]} pattern Path pattern to match.
 * @param handler Handler to execute on match.
 */
Router.prototype.delete = function (pattern, handler) {
    this.route('DELETE', pattern, handler);
};

/**
 * Adds a route that matches PUT method.
 *
 * @example
 * // Add a route.
 * router.put('/persons/{id}', function(req) {
 *  ...
 * });
 *
 * @param {String|String[]} pattern Path pattern to match.
 * @param handler Handler to execute on match.
 */
Router.prototype.put = function (pattern, handler) {
    this.route('PUT', pattern, handler);
};

/**
 * Adds a route that matches HEAD method.
 *
 * @example
 * // Add a route.
 * router.head('/persons', function(req) {
 *  ...
 * });
 *
 * @param {String|String[]} pattern Path pattern to match.
 * @param handler Handler to execute on match.
 */
Router.prototype.head = function (pattern, handler) {
    this.route('HEAD', pattern, handler);
};

/**
 * Adds a route that matches PATCH method.
 *
 * @example
 * // Add a route.
 * router.patch('/persons', function(req) {
 *  ...
 * });
 *
 * @param {String|String[]} pattern Path pattern to match.
 * @param handler Handler to execute on match.
 */
Router.prototype.patch = function (pattern, handler) {
    this.route('PATCH', pattern, handler);
};

/**
 * Adds a route that matches all methods.
 *
 * @example
 * // Add a route.
 * router.all('/persons/{id}', function(req) {
 *  ...
 * });
 *
 * @param {String|String[]} pattern Path pattern to match.
 * @param handler Handler to execute on match.
 */
Router.prototype.all = function (pattern, handler) {
    this.route('*', pattern, handler);
};

/**
 * Adds a filter to this router.
 *
 * @example
 * // Add a filter.
 * router.filter(function(req, next) {
 *  return next(req);
 * });
 *
 * @param filter Filter handler to execute.
 */
Router.prototype.filter = function (filter) {
    this.filters.push(filter);
};

function handleRoute(scope, req) {
    let result = matches(scope.routes, req.method, req.rawPath, req.contextPath);
    if (result) {
        req.pathParams = {};
        for (const property in result.pathParams) {
            req.pathParams[property] = result.pathParams[property];
        }

        return result.handler(req);
    } else {
        return {
            status: 404
        };
    }
}

function matches(routes, method, path, contextPath) {
    for (const route of routes) {
        let match = route[0].match(method, path, contextPath);
        if (match) {
            return {pathParams: match, handler: route[1]};
        }
    }
    if (method.toUpperCase() === 'HEAD') {
        return matches(routes, 'GET', path, contextPath);
    }
}

function nextInChain(scope, filters) {
    var last = function (req) {
        return handleRoute(scope, req);
    };

    if (filters.length === 0) {
        return last;
    } else {
        return function (req) {
            var next = nextInChain(scope, filters.slice(1));
            return filters[0](req, next);
        }
    }
}

/**
 * Dispatch the request to this router.
 *
 * @example
 * // Delegate to the router.
 * exports.service = function(req) {
 *   return router.dispatch(req);
 * };
 *
 * @param req Actual request..
 * @returns Response output.
 */
Router.prototype.dispatch = function (req) {
    return nextInChain(this, this.filters)(req);
};

/**
 * Creates a new router instance.
 *
 * @return {Router} a new router instance.
 */
module.exports = function () {
    return new Router();
};
