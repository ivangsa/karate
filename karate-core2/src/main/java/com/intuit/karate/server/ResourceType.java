/*
 * The MIT License
 *
 * Copyright 2020 Intuit Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.intuit.karate.server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author pthomas3
 */
public enum ResourceType {

    JS("text/javascript", vals("javascript"), vals("js")),
    JSON("application/json", vals("json"), vals("json")),
    CSS("text/css", vals("css"), vals("css")),
    ICO("image/x-icon", vals("x-icon"), vals("ico")),
    PNG("image/png", vals("png"), vals("png")),
    GIF("image/gif", vals("gif"), vals("gif")),
    JPEG("image/jpeg", vals("jpeg", "jpg"), vals("jpeg", "jpg")),
    HTML("text/html", vals("html"), vals("html", "htm")),
    XML("application/xml", vals("xml"), vals("xml")),
    TEXT("text/plain", vals("plain"), vals("txt")),
    NONE(null, vals(), vals());

    private static String[] vals(String... values) {
        return values;
    }

    public final String contentType;
    public final String[] contentLike;
    public final String[] extensions;

    ResourceType(String contentType, String[] contentLike, String[] extensions) {
        this.contentType = contentType;
        this.contentLike = contentLike;
        this.extensions = extensions;
    }

    private static final Map<String, ResourceType> EXTENSION_MAP = new HashMap();

    static {
        for (ResourceType rt : ResourceType.values()) {
            for (String ext : rt.extensions) {
                EXTENSION_MAP.put(ext, rt);
            }
        }
    }

    public static ResourceType fromFileExtension(String path) {
        if (path == null) {
            return NONE;
        }
        int pos = path.lastIndexOf('.');
        if (pos == -1 || pos == path.length() - 1) {
            return NONE;
        }
        String extension = path.substring(pos + 1).trim().toLowerCase();
        ResourceType rt = EXTENSION_MAP.get(extension);
        return rt == null ? NONE : rt;
    }

    public boolean isStatic() {
        return this != NONE;
    }

    public boolean isHtml() {
        return this == HTML;
    }

    public boolean isJson() {
        return this == JSON;
    }

    public boolean isBinary() {
        switch (this) {
            case ICO:
            case PNG:
            case GIF:
            case JPEG:
                return true;
            default:
                return false;
        }
    }

    public static ResourceType fromContentType(String ct) {
        if (ct == null) {
            return NONE;
        }
        ct = ct.toLowerCase();
        for (ResourceType rt : ResourceType.values()) {
            for (String like : rt.contentLike) {
                if (ct.contains(like)) {
                    return rt;
                }
            }
        }
        return NONE;
    }
    
    public static ResourceType fromObject(Object o) {
        if (o instanceof List) {
            return JSON;
        } else if (o instanceof Map) {
            return JSON;
        } else if (o instanceof String) {
            return TEXT;
        } else {
            return NONE;
        }
    }

}
