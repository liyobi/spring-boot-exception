# spring-boot-exception

##xml format
curl -X GET http://127.0.0.1:8080/ex1 -H "Accept:application/xml"

<response xmlns="com.hello">
    <message >Not Found</message>
    <code >404</code>
    <errors >
        <error>
            <![CDATA[No handler found for1 GET /ex1]]>
        </error>
    </errors>
</response>

##json format

curl -X GET http://127.0.0.1:8080/ex1 -H "Accept:application/json"
{
    "message": "Not Found",
    "code": 404,
    "errors": [
        "No handler found for1 GET /ex1"
    ]
}
