Router Library for Enonic XP
============================

[![Build Status](https://travis-ci.org/enonic/lib-router.svg?branch=master)](https://travis-ci.org/enonic/lib-router)
[![codecov](https://codecov.io/gh/enonic/lib-router/branch/master/graph/badge.svg)](https://codecov.io/gh/enonic/lib-router)
[![License](https://img.shields.io/github/license/enonic/lib-router.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

<img align="right" style="margin-top:10px;" alt="Router Library" src="https://rawgithub.com/enonic/lib-router/master/lib-router-icon.svg" width="200">

This library implements a simple HTTP router that can be used in XP app's main controller.

See documentation here: https://enonic-docs.s3.amazonaws.com/com.enonic.lib/lib-router/index.html

## Compatibility

| Version | XP Version  | Download |
|---------|-------------| -------- |
| 1.0.0   | 6.11.x      | [Download](http://repo.enonic.com/public/com/enonic/lib/lib-router/1.0.0/lib-router-1.0.0.jar) |
| 1.0.1   | 6.11.x      | [Download](http://repo.enonic.com/public/com/enonic/lib/lib-router/1.0.1/lib-router-1.0.1.jar) |

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
