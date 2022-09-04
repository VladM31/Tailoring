package sigma.nure.tailoring.tailoring.repository;

public interface UserCodeRepository {

    boolean updateCodeToInactiveByUserIdAndCodValue(Long userId, String codeValue);

    boolean save(Long userId, String codeValue);
}
