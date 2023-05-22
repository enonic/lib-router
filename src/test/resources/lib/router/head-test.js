const t = require('/lib/xp/testing');

const lib = require('/lib/router');


exports.testHead = () => {
    const router = lib();

    let result;
    router.get(['/static','/static/{path:.*}'], (req) => {
        return req.pathParams.path;
    });

    result = router.dispatch({
        method : 'HEAD',
        rawPath: '/c/static/b/c',
        contextPath: '/c'
    });
    t.assertEquals('b/c', result)
}
