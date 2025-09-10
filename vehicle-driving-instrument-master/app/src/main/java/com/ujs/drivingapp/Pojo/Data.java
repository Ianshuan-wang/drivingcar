/**
 * Copyright 2022 bejson.com
 */
package com.ujs.drivingapp.Pojo;

/**
 * Auto-generated: 2022-02-06 18:33:15
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Data {

    private String token;
    private Body body;

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Data{" +
                "token='" + token + '\'' +
                ", body=" + body +
                '}';
    }
}