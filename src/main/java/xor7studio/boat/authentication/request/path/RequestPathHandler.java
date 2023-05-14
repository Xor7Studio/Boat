package xor7studio.boat.authentication.request.path;

import xor7studio.boat.authentication.request.HttpRequest;

public abstract class RequestPathHandler {
    public abstract String parse(HttpRequest request);
}
