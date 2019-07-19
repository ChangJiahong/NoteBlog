# 前后端分离请求问题
- 后端服务器设置http请求包含Cookie

Access-Control-Allow-Credentials： <br>
该字段可选。它的值是一个布尔值，表示是否允许发送Cookie。默认情况下，Cookie不包括在CORS请求之中。设为true，即表示服务器明确许可，Cookie可以包含在请求中，一起发给服务器。这个值也只能设为true，如果服务器不要浏览器发送Cookie，删除该字段即可。

需要注意的是，如果要发送Cookie，Access-Control-Allow-Origin就不能设为星号，必须指定明确的、与请求网页一致的域名。同时，Cookie依然遵循同源政策，只有用服务器域名设置的Cookie才会上传，其他域名的Cookie并不会上传，且（跨源）原网页代码中的document.cookie也无法读取服务器域名下的Cookie！
！

```
response.setHeader("Access-Control-Allow-Origin",request.getHeader("Origin"));
response.setHeader("Access-Control-Allow-Credentials","true");
```

- 前端请求携带cookie
CORS请求默认不发送Cookie和HTTP认证信息。如果要把Cookie发到服务器，一方面要服务器同意，指定Access-Control-Allow-Credentials: true，另一方面，开发者必须在AJAX请求中打开 withCredentials 属性。

```
this.$axios.default.withCredentials = true
```
