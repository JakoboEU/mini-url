package james.richardson.miniurl.controller;

import james.richardson.miniurl.controller.model.MiniUrlResponse;
import james.richardson.miniurl.service.MiniUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/audit")
public class AdminAuditController {

    private final MiniUrlService miniUrlService;

    @Autowired
    public AdminAuditController(MiniUrlService miniUrlService) {
        this.miniUrlService = miniUrlService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<MiniUrlResponse> search(final @RequestParam("page") int page) {
        return miniUrlService.fetchAllMiniUrlsOnPage(page).map(MiniUrlResponse::new).collect(Collectors.toList());
    }
}
