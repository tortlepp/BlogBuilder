The code of BlogBuilder is written in Java. In the code several features of Java 8 are used, therefore an up to date installation of Java 8 is required for development and building. Compatibility with Java 9 was not yet tested, so Java 8 is still recommended.


# Building
[Gradle](https://gradle.org/) is used as build system. If Gradle is set up correctly, simply run

    gradle jar

in the repository root to build the executable *BlogBuilder.jar* in the `build/libs` directory. During the build process Gradle will take care of everything, including fetching the required dependencies.


# Code quality
To ensure a reasonable standard of code quality, several well-known testing utilities are used. For each utility there is a Gradle task:

* [FindBugs](http://findbugs.sourceforge.net/): run `gradle findbugsMain`
* [PMD](https://pmd.github.io/) (see *build.gradle* for the used rule sets): run `gradle pmdMain`
* [Checkstyle](http://checkstyle.sourceforge.net/) with a rule set based on the [Google Style Guide](https://google.github.io/styleguide/javaguide.html): run `gradle checkstyleMain`

When these tests are run, HTML reports with the test results are created in the `build/reports` directory.

Unit tests with JUnit are planned for the future, but not yet available.


# Documentation
The general/user documentation (a.k.a. the handbook) is located in this Wiki.

The source code is documented with Javadoc comments (and additional comments where it is necessary). The Gradle task *javadocFull* creates the full documentation in `build/docs/javadoc`

All changes since the first public release are tracked in the [CHANGELOG.md](https://github.com/tortlepp/BlogBuilder/blob/master/CHANGELOG.md).