const t = require('/lib/xp/testing');

const lib = require('/lib/router');


exports.testPatch = () => {
    const router = lib();

    let result;
    router.patch(['/static','/static/{path:.*}'], (req) => {
        return req.pathParams.path;
    });

    result = router.dispatch({
        method : 'PATCH',
        rawPath: '/c/static/b/c',
        contextPath: '/c'
    });
    t.assertEquals('b/c', result)
}
