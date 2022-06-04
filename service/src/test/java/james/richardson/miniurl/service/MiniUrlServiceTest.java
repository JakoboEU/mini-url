package james.richardson.miniurl.service;

import james.richardson.miniurl.database.repository.MiniUrlRepository;
import james.richardson.miniurl.database.repository.entity.MiniUrlEntity;
import james.richardson.miniurl.service.model.MiniUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MiniUrlServiceTest {

    @Mock
    private MiniUrlRepository repository;

    @Mock
    private MiniUrlCodeGenerator codeGenerator;

    private MiniUrlService testSubject;

    @BeforeEach
    void createTestSubject() {
        testSubject = new MiniUrlService(repository, codeGenerator);
    }

    @Nested
    class CreateNewFromLongUrl {
        @BeforeEach
        void repositoryReturnsSavedItem() {
            when(repository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        }

        @Test
        void generatesCode() {
            // When
            testSubject.createNewFromLongUrl("anyLongUrl");

            // Then
            verify(codeGenerator).generateNewCode();
        }

        @Test
        void storesUrlWithNewCode() {
            // Given
            final String newCode = "someNewCode";
            final String longUrl = "someLongUrl";

            when(codeGenerator.generateNewCode()).thenReturn(newCode);

            // When
            testSubject.createNewFromLongUrl(longUrl);

            // Then
            final ArgumentCaptor<MiniUrlEntity> storedEntityArg = ArgumentCaptor.forClass(MiniUrlEntity.class);
            verify(repository).save(storedEntityArg.capture());

            assertThat(storedEntityArg.getValue().toModel().getMiniUrlCode()).isEqualTo(newCode);
            assertThat(storedEntityArg.getValue().toModel().getLongUrl()).isEqualTo(longUrl);
            assertThat(storedEntityArg.getValue().toModel().getOpenCount()).isEqualTo(0);
        }

        @Test
        void generatesCodeUntilUniqueOneFound() {
            // Given
            when(repository.findById(anyString())).thenReturn(Optional.of(new MiniUrlEntity()), Optional.empty());
            when(codeGenerator.generateNewCode()).thenReturn("EXISTS", "DOES NOT EXIST");

            // When
            testSubject.createNewFromLongUrl("anyLongUrl");

            // Then
            final ArgumentCaptor<MiniUrlEntity> storedEntityArg = ArgumentCaptor.forClass(MiniUrlEntity.class);
            verify(repository).save(storedEntityArg.capture());

            assertThat(storedEntityArg.getValue().toModel().getMiniUrlCode()).isEqualTo("DOES NOT EXIST");
        }
    }

    @Nested
    class FindById {

        @Test
        void findsByIdInDatabase() {
            // Given
            final String someCodeToFind = "someCodeToFind";

            // When
            testSubject.findById(someCodeToFind);

            // Then
            verify(repository).findById(someCodeToFind);
        }

        @Test
        void returnsEmptyWhenCodeNotFound() {
            // Given
            when(repository.findById(anyString())).thenReturn(Optional.empty());

            // When
            final Optional<MiniUrl> result = testSubject.findById("anyCode");

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        void returnsFoundEntity() {
            // Given
            final MiniUrlEntity dbResult = new MiniUrlEntity("code", "longUrl", 123);
            when(repository.findById(anyString())).thenReturn(Optional.of(dbResult));

            // When
            final Optional<MiniUrl> result = testSubject.findById("anyCode");

            // Then
            assertThat(result.get()).usingRecursiveComparison().isEqualTo(dbResult);
        }
    }

    @Nested
    class Save {
        @BeforeEach
        void repositoryReturnsSavedItem() {
            when(repository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        }

        @Test
        void storesGivenEntityInDatabase() {
            // Given
            final String code = "someCode";
            final String longUrl = "someLongUrl";
            final int count = 123;

            // When
            testSubject.save(code, longUrl, count);

            // Then
            final ArgumentCaptor<MiniUrlEntity> storedEntityArg = ArgumentCaptor.forClass(MiniUrlEntity.class);
            verify(repository).save(storedEntityArg.capture());

            assertThat(storedEntityArg.getValue().toModel().getMiniUrlCode()).isEqualTo(code);
            assertThat(storedEntityArg.getValue().toModel().getLongUrl()).isEqualTo(longUrl);
            assertThat(storedEntityArg.getValue().toModel().getOpenCount()).isEqualTo(count);
        }

        @Test
        void returnsStoredEntity() {
            // Given
            final MiniUrlEntity dbResult = new MiniUrlEntity("code", "longUrl", 123);
            when(repository.save(any())).thenReturn(dbResult);

            // When
            final MiniUrl result = testSubject.save("anyCode", "anyLongUrl", 0);

            // Then
            assertThat(result).usingRecursiveComparison().isEqualTo(dbResult);
        }
    }

    @Nested
    class Fetch {

        @Test
        void fetchesWithPageAndDefaultPageSize() {
            // Given
            final int page = 2;

            final Page<MiniUrlEntity> pagedResult = dbPageOf();
            when(repository.findAll(any(PageRequest.class))).thenReturn(pagedResult);

            // When
            testSubject.fetchAllMiniUrlsOnPage(page);

            // Then
            final ArgumentCaptor<PageRequest> pageRequestArg = ArgumentCaptor.forClass(PageRequest.class);
            verify(repository).findAll(pageRequestArg.capture());

            assertThat(pageRequestArg.getValue().getPageNumber()).isEqualTo(page);
            assertThat(pageRequestArg.getValue().getPageSize()).isEqualTo(10);
        }

        @Test
        void returnsItemsOnPage() {
            // Given
            final int anyPageNumber = 123;

            final MiniUrlEntity dbResult = new MiniUrlEntity("code", "longUrl", 12);

            final Page<MiniUrlEntity> dbPage = dbPageOf(dbResult);
            when(repository.findAll(any(PageRequest.class))).thenReturn(dbPage);

            // When
            final Stream<MiniUrl> result = testSubject.fetchAllMiniUrlsOnPage(anyPageNumber);

            // Then
            assertThat(result).usingRecursiveFieldByFieldElementComparator().contains(dbResult.toModel());
        }

        private Page<MiniUrlEntity> dbPageOf(MiniUrlEntity... dbResult) {
            final Page<MiniUrlEntity> dbPage = mock(Page.class);
            when(dbPage.stream()).thenReturn(Stream.of(dbResult));
            return dbPage;
        }
    }
}