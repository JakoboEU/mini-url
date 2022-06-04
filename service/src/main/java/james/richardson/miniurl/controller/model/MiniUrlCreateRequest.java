package james.richardson.miniurl.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

public class MiniUrlCreateRequest {
    @NotBlank
    private final String longUrl;

    public MiniUrlCreateRequest(@JsonProperty("longUrl") String longUrl) {
        this.longUrl = longUrl;
    }

    public String getLongUrl() {
        return longUrl;
    }
}
