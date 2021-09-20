package v2.client.model.criteries;

public class Message implements Criterion {
    private final String criter = "<message>";

    @Override
    public String getCriter() {
        return criter;
    }
}
