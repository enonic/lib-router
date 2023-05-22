const t = require('/lib/xp/testing');

const lib = require('/lib/router');

exports.testGet_multiple_patterns = () => {
    const router = lib();

    let result;
    router.get(['/static','/static/{path:.*}'], (req) => {
        return req.pathParams.path;
    });

    result = router.dispatch({
        method : 'GET',
        rawPath: '/c/static/b/c',
        contextPath: '/c'
    });
    t.assertEquals('b/c', result)

    result = router.dispatch({
        method : 'GET',
        rawPath: '/c/static/',
        contextPath: '/c'
    });
    t.assertEquals('', result)

    result = router.dispatch({
        method : 'GET',
        rawPath: '/c/static',
        contextPath: '/c'
    });
    t.assertEquals(undefined, result)
}
