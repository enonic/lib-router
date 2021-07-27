const t = require('/lib/xp/testing');

const lib = require('/lib/router');

exports.testGet_multiple_patterns = () => {
    const router = lib();

    let path;
    router.get(['/static','/static/{path:.*}'], (req) => {
        path = req.pathParams.path;
    });

    router.dispatch({
        method : 'GET',
        rawPath: '/c/static/',
        contextPath: '/c'
    });
    t.assertEquals('', path)

    router.dispatch({
        method : 'GET',
        rawPath: '/c/static/b/c',
        contextPath: '/c'
    });
    t.assertEquals('b/c', path)

    router.dispatch({
        method : 'GET',
        rawPath: '/c/static',
        contextPath: '/c'
    });
    t.assertEquals(undefined, path)
}
