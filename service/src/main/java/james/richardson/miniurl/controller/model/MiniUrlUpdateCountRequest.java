package james.richardson.miniurl.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class MiniUrlUpdateCountRequest {
    @Min(0)
    private final int openCount;
    @NotBlank
    private final String longUrl;

    public MiniUrlUpdateCountRequest(@JsonProperty("openCount") int openCount, @JsonProperty("longUrl") String longUrl) {
        this.openCount = openCount;
        this.longUrl = longUrl;
    }

    public int getOpenCount() {
        return openCount;
    }

    public String getLongUrl() {
        return longUrl;
    }
}
