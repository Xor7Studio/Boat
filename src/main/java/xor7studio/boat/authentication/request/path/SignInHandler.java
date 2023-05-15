package xor7studio.boat.authentication.request.path;

public class SignInHandler extends RequestPathHandler {
    @Override
    public String parse(String body) {
        System.out.println(body);
        return "AAA";
    }
}
