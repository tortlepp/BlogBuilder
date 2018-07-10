This Wiki page shows step by step how to use BlogBuilder.

# Setting up BlogBuilder
To run BlogBuilder, no special setup is required except Java must be present on the computer in at least version 8. To verify that BlogBuilder works properly, navigate in a console  to the directory where the downloaded JAR file is located. Run `java -jar BlogBuilder.jar`, which should print out usage instructions for BlogBuilder.

You can always run BlogBuilder as shown above. But to make things more comfortable it is possible to *install* BlogBuilder:

## Optional installation on Windows
Find a place to keep BlogBuilder, for example in `C:\BlogBuilder`. The downloaded JAR file has to be in that folder. Afterwards, create a new file `BlogBuilder.cmd` in this folder and fill it with this line:

    java.exe -jar C:\BlogBuilder\BlogBuilder.jar %*

Finally add `C:\BlogBuilder` to the `PATH` variable. In CMD and PowerShell BlogBuilder is now available, to try it, run `BlogBuilder` from the command-line.

## Optional installation on Linux
Create the directory `/opt/BlogBuilder` and copy the downloaded JAR file in this folder. In this folder, create a new shell script `BlogBuilder.sh` with this content:

    #!/bin/bash
    java -jar /opt/BlogBuilder/BlogBuilder.jar "$@"

Now make the shell script executable and link it to `/usr/bin`:

    chmod +x /opt/BlogBuilder/BlogBuilder.sh
    ln -s /opt/BlogBuilder/BlogBuilder.sh /usr/bin/BlogBuilder

BlogBuilder is now available, to try it, run `BlogBuilder` from the command-line.


# Creating a new project
The best way to start a new project is to let BlogBuilder do all the setup. When BlogBuilder is launched with the argument `--init`, a new project is created.

    java -jar BlogBuilder.jar --init MyBlog

creates a new project *MyBlog* in the current directory. Alternatively

    java -jar BlogBuilder.jar --init C:/Users/example/MyBlog

creates a new project *MyBlog* in the personal folder of the user *example*. BlogBuilder accepts absolute and relative directory paths as long as the project directory itself does not yet exist.

When the initialization action runs, it creates the project directory, some sub-directories and a bunch of sample files. After the creation is done, go to the project directory - there you will find these directories:

| Directory / File | Usage                                                               |
|:-----------------|:--------------------------------------------------------------------|
| blog             | Contains the built blog, *empty when a new project is started*      |
| content          | Contains all Markdown files (blog posts and pages)                  |
| resources        | Contains additional resources (CSS & JavaScript files, images, ...) |
| templates        | Contains templates for the Markdown to HTML processing              |
| blog.properties  | The configuration file for the project / blog                       |

Do not rename or delete these directories - BlogBuilder expects them to exist and will fail to build the blog if a directory is missing.

## The directory "blog"
This directory contains the build blog after the build action run. Upload the contents of this directory to you web space to publish the blog.

## The directory "content"
This directory contains all blog posts and content pages in Markdown format. When the build action runs, it will process these files to HTML pages. Only files with the extension `.md` are processed to HTML, all other files are ignored.

It is possible to create sub-directories; created sub-directories will become part of the URL of the HTML page, for example the directory `2017` is created and the blog post `hello_world.md` is placed in that directory, then the URL of the blog post will be `[BLOG-URL]/2017/hello_world.html`.

## The directory "resources"
This directory contains additional resources for the blog, e.g. CSS files, custom JavaScript libraries, images, fonts, ... All files are copied to the `blog` directory when the blog is built. It is also possible to create sub-folders and place resource files in these sub-directories. All files are copied by keeping their path, for example the file `resources/style/style.css` is copied to `blog/style/style.css` and its URL will be `[BLOG-URL]/style/style.css`.

## The directory "templates"
This directory contains the FreeMarker template files that are used to create the HTML pages. You can edit these files to change the layout of the blog (see [[Advanced Features]]). But do not rename or delete the template files that are starting with the `page_` prefix as BlogBuilder expects these to exist in the template directory.

## The file "blog.properties"
This file configures your blog. For a detailed description of all options see "Configuring the blog" below.


# Writing blog posts and creating pages
All Markdown files in the directory `content` (including sub-directories) are treated as Blog posts or content pages. To edit or create these files, you can either use a common text editor (e.g. [Notepad++](https://notepad-plus-plus.org/)) or a special Markdown editor (e.g. [MarkdownPad](http://markdownpad.com/)).

Each file has a header and a content part. Each line of the header starts with `;;` and contains a key-value-pair. These headers are supported by BlogBuilder:

| Header (key) |    Example value   | Obligatory |                     Explanation                     | Additional comment                                               |
|:------------:|:-------------------|:----------:|:----------------------------------------------------|:-----------------------------------------------------------------|
| `title`      | `Hello World`      | yes        | The title of the blog post or page                  | Just plain text, Markdown is not supported                       |
| `created`    | `2017-07-28 23:14` | yes        | The creation date and time of the blog post or page | Has to be formatted as shown<br>Do not change after publishing   |
| `modified`   | `2016-07-29 15:53` | no         | The last modification of the blog post or page      | Has to be formatted as shown                                     |
| `category`   | `General, Blog`    | no         | The categories of a blog post                       | Separate multiple categories with `,`<br>Only used in blog posts |
| `noblog`     | *(no value)*       | no         | Indicator for content pages                         | Does not have a value                                            |

The obligatory headers have to be in a file, otherwise it will be ignored in the build process. It is recommended not to change the creation date and time after the blog post or page has been published; instead use the modification date and time to indicate updates or changes.

All lines that don't start with `;;` are treated as content lines. During the build process the Markdown formatting is parsed and transformed into HTML.

When a file is saved, pay attention that its encoding is set to UTF-8. If not, there might occur problems while reading the file in the build process.


# Configuring the blog
To configure the blog, open the `blog.properties` in a text editor:

|       Option        |       Default value       |                        Explanation                         |                                   Additional comment                                    |
|:-------------------:|:-------------------------:|:-----------------------------------------------------------|:----------------------------------------------------------------------------------------|
| `blog.title`        | `My Blog`                 | The title of the blog                                      |                                                                                         |
| `blog.author`       | `John Doe`                | The author of the blog                                     |                                                                                         |
| `blog.baseurl`      | `http://blog.example.com` | The base URL of the blog                                   | A URL like `https://example.com/blog` is possible as well                               |
| `blog.locale`       | `en-US`                   | The locale to use for number and date formats              |                                                                                         |
| `index.filename`    | `index`                   | The filename of the index file(s)                          | Only the prefix, optional page number and the extension `.html` are added automatically |
| `index.posts`       | `3`                       | The number of blog posts on each index page                |                                                                                         |
| `feed.filename`     | `feed.xml`                | The filename of the feed                                   |                                                                                         |
| `feed.posts`        | `3`                       | The number of blog posts in the feed                       |                                                                                         |
| `category.filename` | `category_`               | The filenames of the category pages                        | Only the prefix, category names and the extension `.html` are added automatically       |
| `sitemap.filename`  | `sitemap.xml`             | The filename of the sitemap                                |                                                                                         |
| `clean.ignore`      | `.gitkeep`                | Files in the `blog` folder that are ignored while cleaning | Separate multiple files with `;` (without spaces)                                       |

If an option is missing (or misspelled) in the configuration file, its default value is used. The file itself is an ordinary Java properties file. Empty lines are ignored, lines that start with `#` are treated as comments.


# Building the blog
To publish the blog, it has to be built first. Building the blog means, that all blog posts and content pages are processed to HTML pages, additional files (index and category pages, the feed and the sitemap) are generated and all resources are copied into the blog. To build the blog, launch BlogBuilder with the argument `--build`. For example, run

    java -jar BlogBuilder.jar --build MyBlog

to build the project *MyBlog* (*MyBlog* is the project directory of the blog).  Like the `--init` action, an absolute path to the project directory is sufficient as well.

When the build process is done, the blog can be uploaded to a webserver in order to get published.

*Note: When the build process runs, it first deletes all files in the `blog` directory and then rebuilds the entire blog. To keep certain files, use the option `clean.ignore` in the configuration file.*
