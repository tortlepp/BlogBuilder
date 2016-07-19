<!DOCTYPE html>
<html lang="${blog.language}">
 <head>
  <meta charset="UTF-8">
  <meta name="author" content="${blog.author}">
  <title>${blog.title}: ${category}</title>
  <link rel="stylesheet" href="${basedir}style.css">
 </head>
 <body>
  <#include "include_header.ftl">
  
  <h1>Category: ${category}</h1>

  <#list posts as post>
   <article>
    <h1><a href="${post.link}">${post.title}</a></h1>
    ${post.content?keep_before("</p>")} [...]</p>
    <p><a href="${post.link}">Continue Reading</a></p>
   </article>
  </#list>

  <#include "include_footer.ftl">
 </body>
</html>