<#list paths as path>
    <#if path.post??>
    "${path.uri!""}": {
        "post": {
            "tags": [
        <#list path.post.tags as tag>
                "${tag!""}"<#if!tag?is_last>,</#if>
        </#list>
            ],
            "summary": "${path.post.summary!""}",
            "operationId": "${path.post.operationId!""}",
        <#if path.post.requestBody??>
            "requestBody": {
                "content": {
                    "${path.post.requestBody.mideType!"application/json"}": {
                        "schema": {
                            "$ref": "#/components/schemas/${path.post.requestBody.type!""}"
                        }
                    }
                },
                "required": ${path.post.required!""}
            },
        </#if>
        <#if path.post.responses??>
            "responses": {
                "200": {
                    "description": "OK",
                    "content": {
                        "*/*": {
                            <#if path.post.response.type!"" == "list">
                            "schema": {
                                "type": "array",
                                "items": {
                                    "$ref": "#/components/schemas/${path.post.response.schemaName!""}"
                                }
                            }
                            <#else >
                            "schema": {
                                "$ref": "#/components/schemas/${path.post.response.schemaName!""}"
                            }
                            </#if>
                        }
                    }
                }
            }
        </#if>
        }
    }
    </#if>
</#list>