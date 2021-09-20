package v2.server.criteries;

public class Message implements Criterion {
    private final String criter = "<message>";

    @Override
    public String getCriter() {
        return criter;
    }
}
