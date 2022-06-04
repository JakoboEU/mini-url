package james.richardson.miniurl.service;

import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.stream.Collectors;

@Service
class MiniUrlCodeGenerator {
    private static final Random RANDOM = new Random();

    private static final int LEFT_LIMIT = 48; // numeral '0'
    private static final int RIGHT_LIMIT = 122; // letter 'z'

    private static final int MIN_CODE_LENGTH = 5;
    private static final int MAX_CODE_LENGTH = 8;

    String generateNewCode() {
        final int targetStringLength = RANDOM.nextInt(MAX_CODE_LENGTH - MIN_CODE_LENGTH) + MIN_CODE_LENGTH;

        return RANDOM.ints(LEFT_LIMIT, RIGHT_LIMIT + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .boxed()
                .map(i -> Character.toString(i))
                .collect(Collectors.joining());
    }
}
