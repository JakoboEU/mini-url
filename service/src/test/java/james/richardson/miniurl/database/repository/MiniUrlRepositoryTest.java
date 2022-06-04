package james.richardson.miniurl.database.repository;

import james.richardson.miniurl.configuration.DatabaseConfiguration;
import james.richardson.miniurl.database.repository.entity.MiniUrlEntity;
import james.richardson.miniurl.service.model.MiniUrl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@TestPropertySource("classpath:application.yaml")
class MiniUrlRepositoryTest {

    @Autowired
    private MiniUrlRepository testSubject;

    @Test
    void savesNewMiniUrl() {
        // Given
        final String miniUrlCode = "1234";
        final String longUrl = "http://www.google.co.uk/longUrl";

        // When
        final MiniUrl stored = testSubject.save(new MiniUrlEntity(miniUrlCode, longUrl)).toModel();

        // Then
        assertThat(stored.getMiniUrlCode()).isNotNull();
        assertThat(stored.getLongUrl()).isEqualTo(longUrl);
        assertThat(stored.getOpenCount()).isEqualTo(0);
    }

    @Test
    void returnsEmptyWhenNoUrlForCode() {
        // When
        final Optional<MiniUrlEntity> result = testSubject.findById("DoesNotExist");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void returnsMiniUrlByCode() {
        // Given
        final String miniUrlCode = "abed";
        final String longUrl = "http://www.google.co.uk/longUrl2";

        testSubject.save(new MiniUrlEntity(miniUrlCode, longUrl));

        // When
        final Optional<MiniUrlEntity> result = testSubject.findById(miniUrlCode);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(new MiniUrlEntity(miniUrlCode, longUrl, 0));
    }

    @Test
    void canUpdateOpenCount() {
        // Given
        final String miniUrlCode = "124ererer";
        final String longUrl = "http://www.google.co.uk/longUrl3";

        testSubject.save(new MiniUrlEntity(miniUrlCode, longUrl)).toModel();

        final int newCount = 3;

        // When
        testSubject.save(new MiniUrlEntity(miniUrlCode, longUrl, newCount));

        // Then
        assertThat(testSubject.findById(miniUrlCode).get()).usingRecursiveComparison().isEqualTo(new MiniUrlEntity(miniUrlCode, longUrl, newCount));
    }

    @Test
    void canFetchUrlsInPages() {
        // Given
        testSubject.save(new MiniUrlEntity("99234324", "http://www.url.com/"));
        testSubject.save(new MiniUrlEntity("998213213", "http://another.com/"));

        // When
        final Page<MiniUrlEntity> result = testSubject.findAll(PageRequest.of(1, 1, Sort.by("longUrl")));

        // Then
        assertThat(result.getTotalElements()).isGreaterThan(0);
        assertThat(result.getNumberOfElements()).isEqualTo(1);
    }
}