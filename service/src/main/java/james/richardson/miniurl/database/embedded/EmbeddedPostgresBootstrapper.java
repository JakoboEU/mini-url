package james.richardson.miniurl.database.embedded;

import io.zonky.test.db.postgres.embedded.EmbeddedPostgres;

import java.io.IOException;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public final class EmbeddedPostgresBootstrapper {

    public static final int DATABASE_PORT = 63745;

    private static EmbeddedPostgres INSTANCE;

    private EmbeddedPostgresBootstrapper() {

    }

    public static EmbeddedPostgres start() {
        try {
            if (isNull(INSTANCE)) {
                INSTANCE = EmbeddedPostgres.builder()
                        .setPort(DATABASE_PORT)
                        .setCleanDataDirectory(true)
                        .start();
            }

            return INSTANCE;
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong starting the EmbeddedPostgres instance");
        }
    }

    public static void stop() {
        if (nonNull(INSTANCE)) {
            try {
                INSTANCE.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
