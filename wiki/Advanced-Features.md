This Wiki page shows some advanced features of BlogBuilder.

# Editing FreeMarker templates.
During the build process, BlogBuilder reads the Markdown files in the `content` directory, processes their content and finally writes HTML files. To write the HTML files, FreeMarker templates are used to control the output.

To get an introduction into FreeMarker and its usage I recommend to have a look on these sites:

* https://en.wikipedia.org/wiki/FreeMarker
* http://freemarker.org/docs/dgui_quickstart.html
* http://www.vogella.com/tutorials/FreeMarker/article.html

There are four templates (these files are required during the build process, don't rename or delete them!):

|      Template       |       Usage      |
|:-------------------:|:-----------------|
| `page_blogpost.ftl` | Blog posts       |
| `page_category.ftl` | Category pages   |
| `page_index.ftl`    | Blog index pages |
| `page_page.ftl`     | Content pages    |

In the templates, a number of variables can be used to retrieve data / information from the Java objects / data structures. But not all variables are available in all templates:

|     Variable      |                           Content                           | `page_blogpost.ftl` | `page_category.ftl` | `page_index.ftl` | `page_page.ftl` |
|:-----------------:|:------------------------------------------------------------|:-------------------:|:-------------------:|:----------------:|:---------------:|
| `basedir`         | The path from the file to the base directory                | x                   | x                   | x                | x               |
| `blog.author`     | The author of the blog (from the configuration)             | x                   | x                   | x                | x               |
| `blog.language`   | The language of the blog (from the configuration)           | x                   | x                   | x                | x               |
| `blog.title`      | The title of the blog (from the configuration)              | x                   | x                   | x                | x               |
| `index_newer`     | The filename of the next index page with newer blog posts   |                     |                     | x                |                 |
| `index_older`     | The filename of the next index page with older blog posts   |                     |                     | x                |                 |
| `category`        | The name / title of the category                            |                     | x                   |                  |                 |
| `page.content`    | The title of the page                                       |                     |                     |                  | x               |
| `page.title`      | The content of the page (in HTML)                           |                     |                     |                  | x               |
| `post.categories` | The categories of the blog post                             | x                   |                     |                  |                 |
| `post.content`    | The content of the blog post (in HTML)                      | x                   |                     |                  |                 |
| `post.created`    | The creation date and time of the blog post                 | x                   |                     |                  |                 |
| `post.next`       | The relative link to the next (newer) blog post             | x                   |                     |                  |                 |
| `post.previous`   | The relative link to the previous (older) blog post         | x                   |                     |                  |                 |
| `post.title`      | The title of the blog post                                  | x                   |                     |                  |                 |
| `posts`           | Contains the blog posts of the category / in the index page |                     | x                   | x                |                 |
