Log4j2 to JUL
=============

A set of tools to redirect Log4j2 log messages to java.util.logging in several different ways.

Copyright (c) 2013, Wolf480pl <<wolf480@interia.pl>>

[![Build Status](https://travis-ci.org/Wolf480pl/log4j2-to-jul.png?branch=master)](https://travis-ci.org/Wolf480pl/log4j2-to-jul)

Source
------
The latest and greatest source can be found on [GitHub].

License
-------
Log4j2 to JUL is licensed under the [MIT License][License].

Compiling
---------
Log4j2 to JUL uses Maven to handle its dependencies.

* Install [Maven 2 or 3](http://maven.apache.org/download.html)
* Checkout this repo and run: `mvn clean install`

Using with your own project
---------------------------
If you're using [Maven](http://maven.apache.org/download.html) to manage project dependencies, simply include the following in your `pom.xml`:
```xml
    <dependency>
        <groupId>com.github.wolf480pl</groupId>
        <artifactId>log4j2-to-jul</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```

### As Log4j2 implementation ###
If you want to use it as a replacement of Log4j2 implementation, and redirect all the logging done with Log4j2 api, that's all you need to do.
Just make sure you don't have other Log4j2 implementation with higher priority in your classpath. 

### As Log4j2 Appender ###
If you want to use the JULAppender from this library with Log4j2 core to redirect some of the log4j2 messages, make sure you have both this library and log4j-core in your dependencies, and the `JUL` appender to your log4j2 configuration.
For example:
```xml
  <Appenders>
    ...
    <JUL name="JUL" logger="test.log4j2-to-jul">
      <PatternLayout pattern="[%t - %marker] %msg%rEx{0}"/>
    </JUL>
    ...
  </Appenders>
```
Note that the layout here is used to produce the content of the message field in LogRecord of java.util.logging . All the fields from the LogEvent except thread and marker are passed to the corresponding fields in LogRecord, and if you add them to the layout here, and it's also in the java Logger's formatter, it will be printed twice. The default layout of this appender just prints the message without the exception.

### As Log4j2 Logger ###
If you have a class to which you can pass a Logger from Log4j2 api, and you want to redirect its logging, you can pass it an instance of JULLogger (`com.github.wolf480pl.log4j2_to_jul.context.JULLogger`) which will redirect all the logging done through that logger to a specified java.util.logging.Logger


[GitHub]: https://github.com/Wolf480pl/log4j2-to-jul
[License]: http://opensource.org/licenses/MIT
