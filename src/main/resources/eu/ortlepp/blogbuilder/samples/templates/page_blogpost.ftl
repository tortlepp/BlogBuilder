<html>
<head>
<title>Blog: ${post.title} (${post.created?datetime})</title>
</head>
<body>
${post.content}

<p>
<#if post.next?has_content>
<a href="${post.next}">Next post</a>
<#else>
Next post
</#if>
&nbsp;&nbsp;-&nbsp;&nbsp;
<#if post.previous?has_content>
<a href="${post.previous}">Previous post</a>
<#else>
Previous post
</#if>
</p>
</body>
</html>