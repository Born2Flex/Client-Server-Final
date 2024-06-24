package ua.edu.ukma.http.routing;

import java.util.Objects;

public class Request {
    private final String uri;
    private final String method;

    public Request(String uri, String method) {
        this.uri = uri;
        this.method = method;
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Request request)) return false;

        return method.equals(request.method) && uri.matches(request.uri);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(method);
    }
}
