package james.richardson.miniurl.database.repository;

import james.richardson.miniurl.database.repository.entity.MiniUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MiniUrlRepository extends JpaRepository<MiniUrlEntity, String> {

}
