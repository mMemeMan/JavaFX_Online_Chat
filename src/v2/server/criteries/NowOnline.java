package v2.server.criteries;

public class NowOnline implements Criterion {
    private final String criter = "<nowOnline>";

    @Override
    public String getCriter() {
        return criter;
    }
}
