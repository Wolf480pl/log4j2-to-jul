Log4j2 to JUL
-------------

A set of tools to redirect Log4j2 log messages to java.util.logging in several different ways.

[![Build Status](https://travis-ci.org/Wolf480pl/log4j2-to-jul.png?branch=master)](https://travis-ci.org/Wolf480pl/log4j2-to-jul)

Compiling
---------
Log4j2 to JUL uses Maven to handle its dependencies.

* Install [Maven 2 or 3](http://maven.apache.org/download.html)
* Checkout this repo and run: `mvn clean install`

Versioning scheme and public API
--------------------------------
This project uses [Semantic Versioning][SemVer].

The public API is defined as:
* the documentation below, excluding repository information
* every class, method, field, or method argument in this project that has a non-empty description in [javadocs][JavaDocs], along with its description.
  If some class, method, or field has empty description in the javadocs, then it's not part of the public API.
  If the description of a class, method, field, or method argument specifically states that the behavior is undefined, then its existance and type is part of the public API, but its behavior is not.

Using with your own project
---------------------------
Add this project to your dependencies, as described in [Dependency Information](dependency-info.html).
This project is deployed to Sonatype OSSRH snapshots repo (https://oss.sonatype.org/content/repositories/snapshots). If you're using don't already have OSSRH in your repository list, you'll need to add it as well.
If you're using [Maven](http://maven.apache.org/download.html), simply add the following to your pom.xml:

    <repository>
        <id>sonatype-nexus-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        <snapshots>
            <enabled>true</enabled>
            <updatePolicy>always</updatePolicy>
        </snapshots>
    </repository>

### As Log4j2 implementation ###
If you want to use it as a replacement of Log4j2 implementation, and redirect all the logging done with Log4j2 api, that's all you need to do.
Just make sure you don't have other Log4j2 implementation with higher priority in your classpath. 

### As Log4j2 Appender ###
If you want to use the JULAppender from this library with Log4j2 core to redirect some of the log4j2 messages, make sure you have both this library and log4j-core in your dependencies, and the `JUL` appender to your log4j2 configuration.
For example:

    <Appenders>
      ...
      <JUL name="JUL" logger="test.log4j2-to-jul">
        <PatternLayout pattern="[%t - %marker] %msg%rEx{0}"/>
      </JUL>
      ...
    </Appenders>

Note that the layout here is used to produce the content of the message field in LogRecord of java.util.logging . All the fields from the LogEvent except thread and marker are passed to the corresponding fields in LogRecord, and if you add them to the layout here, and it's also in the java Logger's formatter, it will be printed twice. The default layout of this appender just prints the message without the exception.

### As Log4j2 Logger ###
If you have a class to which you can pass a Logger from Log4j2 api, and you want to redirect its logging, you can pass it an instance of JULLogger (`com.github.wolf480pl.log4j2_to_jul.context.JULLogger`) which will redirect all the logging done through that logger to a specified java.util.logging.Logger

Log level mapping
-----------------
    FATAL -> SEVERE
    ERROR -> SEVERE
    WARN  -> WARNING
    INFO  -> INFO
    DEBUG -> FINE
    TRACE -> FINER


[GitHub]: https://github.com/Wolf480pl/log4j2-to-jul
[SemVer]: http://semver.org/
[JavaDocs]: https://wolf480pl.github.com/log4j2-to-jul/apidocs/
