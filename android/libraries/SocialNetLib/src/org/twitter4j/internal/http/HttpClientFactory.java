/*
 * Copyright 2007 Yusuke Yamamoto
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.twitter4j.internal.http;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Yusuke Yamamoto - yusuke at mac.com
 * @since org.twitter4j 2.1.2
 */
public final class HttpClientFactory {
    private static final Constructor HTTP_CLIENT_CONSTRUCTOR;
    private static final String HTTP_CLIENT_IMPLEMENTATION = "org.twitter4j.http.httpClient";

    static {
        Class clazz = null;
        //-Dorg.twitter4j.http.httpClient=org.twitter4j.internal.http.HttpClient
        String httpClientImpl = System.getProperty(HTTP_CLIENT_IMPLEMENTATION);
        if (httpClientImpl != null) {
            try {
                clazz = Class.forName(httpClientImpl);
            } catch (ClassNotFoundException ignore) {
            }
        }
        if (null == clazz) {
            try {
                clazz = Class.forName("org.twitter4j.internal.http.alternative.HttpClientImpl");
            } catch (ClassNotFoundException ignore) {
            }
        }
        if (null == clazz) {
            try {
                clazz = Class.forName("org.twitter4j.internal.http.HttpClientImpl");
            } catch (ClassNotFoundException cnfe) {
                throw new AssertionError(cnfe);
            }
        }
        try {
            HTTP_CLIENT_CONSTRUCTOR = clazz.getConstructor(HttpClientConfiguration.class);
        } catch (NoSuchMethodException nsme) {
            throw new AssertionError(nsme);
        }
    }

    public static HttpClient getInstance(HttpClientConfiguration conf) {
        try {
            return (HttpClient) HTTP_CLIENT_CONSTRUCTOR.newInstance(conf);
        } catch (InstantiationException e) {
            throw new AssertionError(e);
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        } catch (InvocationTargetException e) {
            throw new AssertionError(e);
        }
    }
}
