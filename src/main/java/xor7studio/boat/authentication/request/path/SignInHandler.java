package xor7studio.boat.authentication.request.path;

import xor7studio.boat.authentication.request.HttpRequest;

public class SignInHandler extends RequestPathHandler {
    @Override
    public String parse(HttpRequest request) {
        System.out.println("SIGN_IN");
        return "AAA";
    }
}
