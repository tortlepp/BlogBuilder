<html>
<head>
<title>${blog.title} by ${blog.author}</title>
</head>
<body>

<ul>
<#list posts as post>
 <li><a href="${post.link}">${post.title} (${post.created?datetime})</a></li>
</#list>
</ul>

<p>
<#if index_newer?has_content>
<a href="${index_newer}">Newer posts</a>
<#else>
Newer posts
</#if>
&nbsp;&nbsp;-&nbsp;&nbsp;
<#if index_older?has_content>
<a href="${index_older}">Older posts</a>
<#else>
Older posts
</#if>
</p>
</body>
</html>