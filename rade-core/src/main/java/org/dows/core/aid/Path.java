package org.dows.core.aid;

import lombok.Data;

import java.util.List;

/**
 * @description: </br>
 * @author: lait.zhang@gmail.com
 * @date: 11/13/2024 2:46 PM
 * @history: </br>
 * <author>      <time>      <version>    <desc>
 * 修改人姓名      修改时间        版本号       描述
 */
@Data
public class Path {

    private String uri;
    private HttpMethod get;
    private HttpMethod post;
    private HttpMethod put;
    private HttpMethod delete;
    /*private HttpMethod patch;
    private HttpMethod head;
    private HttpMethod options;
    private HttpMethod trace;
    private HttpMethod connect;
    private HttpMethod tracee;*/

}


@Data
class HttpMethod {
    //private String uri;
    private List<String> tags;
    private String summary;
    private String operationId;
    private List<Parameter> parameters;
    private Request requestBody;
    private Response responses;
}


@Data
class Parameter {

}

class Request {
    private String description;
    private String content;
}

class Response {
    private String description;
    private String content;
}