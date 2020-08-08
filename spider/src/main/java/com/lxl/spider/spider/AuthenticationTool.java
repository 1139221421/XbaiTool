package com.lxl.spider.spider;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import javax.servlet.http.Cookie;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationTool {

    private static ConcurrentHashMap<Long, String> USERS = new ConcurrentHashMap<>();

    public static final AuthenticationTool ME = new AuthenticationTool();

    private AuthenticationTool() {
    }

    public void putUser(long id, String token) {
        USERS.put(id, token);
    }

    public String getToken(long id) {
        return USERS.get(id);
    }

    public String getToken(String user) {
        if (!StringUtils.isBlank(user) && StringUtils.isNumeric(user)) {
            long userId = Long.parseLong(user);
            return USERS.get(userId);
        }
        return null;
    }

    public Pair<String, String> getTokenFromCookie(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equalsIgnoreCase("user")) { //获取键
                    String user = cookie.getValue();
                    String token = getToken(user);
                    return StringUtils.isBlank(token) ? null : Pair.of(user, token);
                }
            }
        }
        return null;
    }

}
