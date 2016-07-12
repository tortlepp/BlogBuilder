<!DOCTYPE html>
<html lang="${blog.language}">
 <head>
  <meta charset="UTF-8">
  <meta name="author" content="${blog.author}">
  <title>${blog.title}: ${page.title}</title>
  <link rel="stylesheet" href="${basedir}style.css">
 </head>
 <body>
  <#include "include_header.ftl">

  <article>
   <h1>${page.title}</h1>
   ${page.content}
  </article>

  <#include "include_footer.ftl">
 </body>
</html>