package james.richardson.miniurl.service;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class MiniUrlCodeGeneratorTest {

    private final MiniUrlCodeGenerator testSubject = new MiniUrlCodeGenerator();

    @Test
    void createsNonNullCode() {
        // When
        final String result = testSubject.generateNewCode();

        // Then
        assertThat(result).isNotNull();
    }

    @Test
    void generatesUniqueCodes() {
        // Given
        final int numberOfGenerationsToCheck = 1000;

        // When
        final long numberOfUniqueCodes = IntStream.range(0, numberOfGenerationsToCheck).boxed()
                .map(i -> testSubject.generateNewCode())
                .count();

        // Then
        assertThat(numberOfUniqueCodes).isEqualTo(numberOfGenerationsToCheck);
    }

    @Test
    void generatesCodesBetweenFiveAndEightCharsLong() {
        // Given
        final int numberOfGenerationsToCheck = 1000;

        // When
        final Optional<String> codeWithIncorrectLength = IntStream.range(0, numberOfGenerationsToCheck).boxed()
                .map(i -> testSubject.generateNewCode())
                .filter(code -> code.length() < 5 || code.length() > 8)
                .findAny();

        // Then
        assertThat(codeWithIncorrectLength).isEmpty();
    }
}