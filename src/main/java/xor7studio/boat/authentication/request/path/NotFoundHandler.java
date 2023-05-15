package xor7studio.boat.authentication.request.path;

public class NotFoundHandler extends RequestPathHandler{
    public static final NotFoundHandler INSTANCE = new NotFoundHandler();
    protected NotFoundHandler(){}
    @Override
    public String parse(String body) {
        return "Not Found";
    }
}
