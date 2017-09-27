Router Library for Enonic XP
============================

[![Build Status](https://travis-ci.org/enonic/lib-router.svg?branch=master)](https://travis-ci.org/enonic/lib-router)
[![codecov](https://codecov.io/gh/enonic/lib-router/branch/master/graph/badge.svg)](https://codecov.io/gh/enonic/lib-router)
[![License](https://img.shields.io/github/license/enonic/lib-router.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

This library implements a simple HTTP router that can be used in XP app's main controller.

See documentation here: https://enonic-docs.s3.amazonaws.com/com.enonic.lib/lib-router/index.html


## Building

To build this project, execute the following:

```
./gradlew clean build
```

## Publishing

To release this project, execute the following:

```
./gradlew clean build uploadArchives
```

## Documentation

Building the documentation is done executing the following:

```
./gradlew buildDoc
```

And publishing the docs to S3:

```
./gradlew publishDoc
```
