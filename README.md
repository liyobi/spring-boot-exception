# spring-boot-exception

##xml format
<pre>
curl -X GET http://127.0.0.1:8080/ex1 -H "Accept:application/xml"

&lt;response xmlns="com.hello"&gt;
    &lt;message &gt;Not Found&lt;/message&gt;
    &lt;code &gt;404&lt;/code&gt;
    &lt;errors &gt;
        &lt;error&gt;
            &lt;![CDATA[No handler found for1 GET /ex1]]&gt;
        &lt;/error&gt;
    &lt;/errors&gt;
&lt;/response&gt;
</pre>

##json format
<pre>
curl -X GET http://127.0.0.1:8080/ex1 -H "Accept:application/json"
{
    "message": "Not Found",
    "code": 404,
    "errors": [
        "No handler found for1 GET /ex1"
    ]
}
</pre>
