package james.richardson.miniurl.controller.model;

import james.richardson.miniurl.service.model.MiniUrl;

public class MiniUrlResponse {

    private final String miniUrlCode;
    private final String longUrl;
    private final int openCount;

    public MiniUrlResponse(MiniUrl miniUrl) {
        this(miniUrl.getMiniUrlCode(), miniUrl.getLongUrl(), miniUrl.getOpenCount());
    }

    MiniUrlResponse(String miniUrlCode, String longUrl, int openCount) {
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
