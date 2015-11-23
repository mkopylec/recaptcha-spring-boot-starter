package com.github.mkopylec.recaptcha.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionLoginFailuresManager implements LoginFailuresManager {

    public static final String LOGIN_FAILURES_COUNT_SESSION_ATTRIBUTE = "__login_failures_count__";

    protected final HttpServletRequest request;

    public SessionLoginFailuresManager(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public void addLoginFailure() {
        Object loginFailuresCount = getSession().getAttribute(LOGIN_FAILURES_COUNT_SESSION_ATTRIBUTE);
        int newFailuresCount = 1;
        if (loginFailuresCount != null) {
            newFailuresCount = ((int) loginFailuresCount) + 1;
        }
        getSession().setAttribute(LOGIN_FAILURES_COUNT_SESSION_ATTRIBUTE, newFailuresCount);
    }

    @Override
    public int getLoginFailuresCount() {
        return (int) getSession().getAttribute(LOGIN_FAILURES_COUNT_SESSION_ATTRIBUTE);
    }

    @Override
    public void resetLoginFailures() {
        getSession().removeAttribute(LOGIN_FAILURES_COUNT_SESSION_ATTRIBUTE);
    }

    private HttpSession getSession() {
        return request.getSession();
    }
}
