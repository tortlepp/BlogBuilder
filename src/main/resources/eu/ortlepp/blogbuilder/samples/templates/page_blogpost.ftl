<!DOCTYPE html>
<html lang="${blog.language}">
 <head>
  <meta charset="UTF-8">
  <meta name="author" content="${blog.author}">
  <title>${blog.title}: ${post.title}</title>
  <link rel="stylesheet" href="${basedir}style.css">
 </head>
 <body>
  <#include "include_header.ftl">

  <article>
   <h1>${post.title}</h1>
   <p id="pubdate">Published ${post.created?datetime} by ${blog.author}<br>
    Categories:&nbsp;
    <#list post.categories as cat>
     <a href="${cat.relativePath}">${cat.name}</a>&nbsp;&nbsp;
    </#list>
   </p>
   ${post.content}
  </article>

  <p id="nav">
   <#if post.next?has_content><a href="${post.next}">Newer Post</a><#else>Newer Post</#if>
   &nbsp;&nbsp;-&nbsp;&nbsp;
   <#if post.previous?has_content><a href="${post.previous}">Older Post</a><#else>Older Post</#if>
  </p>

  <#include "include_footer.ftl">
 </body>
</html>