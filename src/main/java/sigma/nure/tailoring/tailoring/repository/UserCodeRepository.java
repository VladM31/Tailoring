package sigma.nure.tailoring.tailoring.repository;

import java.time.LocalDateTime;

public interface UserCodeRepository {

    boolean updateCodeToInactiveByUserIdAndCodValue(Long userId, String codeValue);

    boolean save(Long userId, String codeValue);
}
