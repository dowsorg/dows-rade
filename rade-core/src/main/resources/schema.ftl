<#list schemas as schema>
    <#if schema.type == 'object'>
    "${schema.name!""}": {
        "title": "${schema.title!""}",
        "type": "object",
        "properties": {
        <#if schema.properties??>
            <#list schema.properties as propertie>
                "${propertie.propertieName!""}": {
                "title": "${propertie.propertieTitle!""}",
                "type": "${propertie.propertieTitle!""}",
                "format": "${propertie.propertieFormat!""}"
                },
            </#list>
        </#if>
        }
    }
    <#elseif schema.type == 'pageRequest'>
        "PageRequest": {
            "required": [
                "pageNo",
                "pageSize"
            ],
            "type": "object",
            "properties": {
                "pageNo": {
                    "title": "页数",
                    "minimum": 1,
                    "type": "integer",
                    "description": "页数",
                    "format": "int64"
                },
                "pageSize": {
                    "title": "页大小",
                    "maximum": 1000,
                    "minimum": 1,
                    "type": "integer",
                    "description": "页大小",
                    "format": "int64"
                },
                "queryObject": {
                    "$ref": "#/components/schemas/${schema.queryObject!""}"
                },
                "orderBy": {
                    "title": "排序字段",
                    "type": "object",
                    "additionalProperties": {
                        "title": "排序字段",
                        "type": "string",
                        "description": "排序字段"
                    },
                    "description": "排序字段"
                }
            }
        }
    <#elseif schema.type == 'pageResponse'>
        "PageResponse": {
            "type": "object",
            "properties": {
                "total": {
                    "title": "总数",
                    "type": "integer",
                    "description": "总数",
                    "format": "int64"
                },
                "pages": {
                    "title": "页数",
                    "type": "integer",
                    "description": "页数",
                    "format": "int64"
                },
                "size": {
                    "title": "页大小",
                    "type": "integer",
                    "description": "页大小",
                    "format": "int64"
                },
                "current": {
                    "title": "当前页",
                    "type": "integer",
                    "description": "当前页",
                    "format": "int64"
                },
                "list": {
                    "title": "数据",
                    "type": "array",
                    "description": "数据",
                    "items": {
                        "$ref": "#/components/schemas/${schema.queryObject!""}"
                    }
                }
            }
        }
    </#if>
</#list>