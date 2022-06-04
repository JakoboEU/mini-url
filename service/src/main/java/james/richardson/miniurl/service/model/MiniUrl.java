package james.richardson.miniurl.service.model;

public class MiniUrl {
    private final String miniUrlCode;
    private final String longUrl;
    private final int openCount;

    public MiniUrl(String miniUrlCode, String longUrl, int openCount) {
        this.miniUrlCode = miniUrlCode;
        this.longUrl = longUrl;
        this.openCount = openCount;
    }

    public String getMiniUrlCode() {
        return miniUrlCode;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public int getOpenCount() {
        return openCount;
    }
}
