BlogBuilder
===========
BlogBuilder is a generator for static blogs written in Java. The blog posts are written in [Markdown](http://daringfireball.net/projects/markdown/) and [FreeMarker](http://freemarker.org/) templates are used to generate the website. The creation of BlogBuilder was inspired by [JBake](http://jbake.org/).


Download
========
Executable JAR files are available from https://github.com/tortlepp/BlogBuilder/releases

*Notice: Java 8 or later is required to run the program.*


Usage
=====
Using BlogBuilder is very simple: Run the JAR file from the command-line and pass the action as first argument and the project as second argument:

* `java -jar BlogBuilder.jar --init MyBlog` initializes a new project "MyBlog" with some sample content
* `java -jar BlogBuilder.jar --build MyBlog` builds the existing project "MyBlog"; afterwards the project is ready to be published online

For detailed information, see [Getting started](https://github.com/tortlepp/BlogBuilder/wiki/Getting-Started) in the Wiki.


Development & Building
======================
The code of BlogBuilder is written in Java, [Gradle](https://gradle.org) is used as build system. To build the executable JAR file for distribution, run `gradle jar` in the repository root. This will build the *BlogBuilder.jar* in the `build/libs` folder.

For further information see [Development](https://github.com/tortlepp/BlogBuilder/wiki/Development) in the Wiki.


3rd party libraries
===================
BlogBuilder uses some 3rd party libraries:

* [flexmark-java](https://github.com/vsch/flexmark-java) to process Markdown to HTML
* [FreeMarker](http://freemarker.org/) as template engine


License
=======
BlogBuilder is developed and distributed under the terms of the MIT License. See [LICENSE.md](LICENSE.md) for more details.

