package sigma.nure.tailoring.tailoring.repository;

public interface UserCodeRepository {

    boolean updateCode(Long userId, String codeValue);

    boolean insertCode(Long userId, String codeValue);
}
