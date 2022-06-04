package james.richardson.miniurl.controller;

import james.richardson.miniurl.controller.model.MiniUrlCreateRequest;
import james.richardson.miniurl.controller.model.MiniUrlResponse;
import james.richardson.miniurl.controller.model.MiniUrlUpdateCountRequest;
import james.richardson.miniurl.service.MiniUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/mini-url")
public class MiniUrlController {

    private final MiniUrlService service;

    @Autowired
    public MiniUrlController(MiniUrlService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    public MiniUrlResponse createMiniUrl(final @RequestBody @Valid MiniUrlCreateRequest longUrl) {
        return new MiniUrlResponse(service.createNewFromLongUrl(longUrl.getLongUrl()));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{miniUrlCode}")
    public MiniUrlResponse updateOpenCount(final @PathVariable String miniUrlCode, final @RequestBody @Valid MiniUrlUpdateCountRequest update) {
        return new MiniUrlResponse(service.save(miniUrlCode, update.getLongUrl(), update.getOpenCount()));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{miniUrlCode}")
    public MiniUrlResponse findUrlByMiniUrl(final @PathVariable String miniUrlCode) {
        return service.findById(miniUrlCode)
                .map(MiniUrlResponse::new)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cound not find mini url code for '" + miniUrlCode + "'."));
    }
}
