# Types for Router library

Type definitions for the [Enonic XP Router library](https://github.com/enonic/lib-router).

## Install

```bash
npm i --save-dev @enonic-types/lib-router
```

## Setup

Add lib-router to `compilerOptions.paths` in the `tsconfig.json` file used for your application's server-side TypeScript code:

```json
{
  "compilerOptions": {
    "paths": {
      "/lib/router": ["./node_modules/@enonic-types/lib-router"]
    }
  }
}
```

## Use

```ts
import type {Request, Response} from '@enonic-types/core';
import newRouter from '/lib/router';

const router = newRouter();

router.filter((req, next) => {
    return next(req);
});

router.get('/persons', (req) => ({
    body: {persons: []},
    contentType: 'application/json',
}));

router.get('/persons/{id:[0-9]+}', (req) => {
    const id = req.pathParams.id;
    return {
        body: {id},
        contentType: 'application/json',
    };
});

router.route('GET', ['/static', '/static/{path:.*}'], (req) => ({
    body: req.pathParams.path,
}));

export function all(req: Request): Response {
    return router.dispatch(req);
}
```

All imports from `/lib/router` should now be typed.

## API

### `newRouter(): Router`

Creates a new router instance. This is the default export of `/lib/router`.

### `Router`

| Method                                                                 | Description                                                                                       |
|------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------|
| `route(method: string, pattern: Pattern, handler: RouteHandler): void` | Adds a route matching `method` and `pattern`. Use `'*'` as method to match any method.            |
| `get(pattern: Pattern, handler: RouteHandler): void`                   | Adds a route matching the `GET` method.                                                           |
| `post(pattern: Pattern, handler: RouteHandler): void`                  | Adds a route matching the `POST` method.                                                          |
| `put(pattern: Pattern, handler: RouteHandler): void`                   | Adds a route matching the `PUT` method.                                                           |
| `delete(pattern: Pattern, handler: RouteHandler): void`                | Adds a route matching the `DELETE` method.                                                        |
| `head(pattern: Pattern, handler: RouteHandler): void`                  | Adds a route matching the `HEAD` method.                                                          |
| `patch(pattern: Pattern, handler: RouteHandler): void`                 | Adds a route matching the `PATCH` method.                                                         |
| `all(pattern: Pattern, handler: RouteHandler): void`                   | Adds a route matching all methods.                                                                |
| `filter(filter: Filter): void`                                         | Adds a filter. Filters run in insertion order before route matching.                              |
| `dispatch(req: Request): Response`                                     | Dispatches the request through the filter chain and the matched route. Returns `404` if no match. |

### `Pattern`

`string \| string[]` — a path pattern, or several patterns sharing the same handler. Placeholders use the `{name}` notation and can be restricted with a regular expression, e.g. `{id:[0-9]+}` or `{path:.+}`.

### `RouterRequest`

| Property     | Type                                    | Description                                                                 |
|--------------|-----------------------------------------|-----------------------------------------------------------------------------|
| `pathParams` | `Record<string, string \| undefined>`   | Path parameters populated from the matched route's placeholders.            |

All other properties are inherited from `Request`.

### `RouteHandler`

`(req: RouterRequest) => Response` — handler invoked when a route matches.

### `Filter`

`(req: Request, next: (req: Request) => Response) => Response` — call `next(req)` to continue the chain, or return a response to short-circuit.

`Request` and `Response` are imported from [`@enonic-types/core`](https://www.npmjs.com/package/@enonic-types/core).

## Documentation

[Router library](https://developer.enonic.com/docs/router-library)
