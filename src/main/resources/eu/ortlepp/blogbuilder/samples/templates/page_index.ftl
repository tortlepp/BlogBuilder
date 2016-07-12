<!DOCTYPE html>
<html lang="${blog.language}">
 <head>
  <meta charset="UTF-8">
  <meta name="author" content="${blog.author}">
  <title>${blog.title}</title>
  <link rel="stylesheet" href="${basedir}style.css">
 </head>
 <body>
  <#include "include_header.ftl">

  <#list posts as post>
   <article>
    <h1><a href="${post.link}">${post.title}</a></h1>
    ${post.content?keep_before("</p>")} [...]</p>
    <p><a href="${post.link}">Continue Reading</a></p>
   </article>
  </#list>

  <p id="nav">
   <#if index_newer?has_content><a href="${index_newer}">Newer Posts</a><#else>Newer Posts</#if>
   &nbsp;&nbsp;-&nbsp;&nbsp;
   <#if index_older?has_content><a href="${index_older}">Older Posts</a><#else>Older Posts</#if>
  </p>

  <#include "include_footer.ftl">
 </body>
</html>