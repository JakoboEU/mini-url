package james.richardson.miniurl.service;

import james.richardson.miniurl.database.repository.MiniUrlRepository;
import james.richardson.miniurl.database.repository.entity.MiniUrlEntity;
import james.richardson.miniurl.service.model.MiniUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public class MiniUrlService {
    private static final int ADMIN_PAGE_SIZE = 10;

    private final MiniUrlRepository repository;
    private final MiniUrlCodeGenerator codeGenerator;

    @Autowired
    public MiniUrlService(MiniUrlRepository repository, MiniUrlCodeGenerator codeGenerator) {
        this.repository = repository;
        this.codeGenerator = codeGenerator;
    }

    public MiniUrl createNewFromLongUrl(String longUrl) {
        final String miniUrlCode = generateNewCode();
        return save(miniUrlCode, longUrl, 0);
    }

    public Optional<MiniUrl> findById(String miniUrl) {
        return repository.findById(miniUrl).map(MiniUrlEntity::toModel);
    }

    public MiniUrl save(String miniUriCode, String longUrl, int openCount) {
        return repository.save(new MiniUrlEntity(miniUriCode, longUrl, openCount)).toModel();
    }

    private String generateNewCode() {
        String code;
        do {
            code = codeGenerator.generateNewCode();
        } while(repository.findById(code).isPresent());
        return code;
    }

    public Stream<MiniUrl> fetchAllMiniUrlsOnPage(int page) {
        return repository
                .findAll(PageRequest.of(page, ADMIN_PAGE_SIZE, Sort.by("longUrl")))
                .stream()
                .map(m -> m.toModel());
    }
}
