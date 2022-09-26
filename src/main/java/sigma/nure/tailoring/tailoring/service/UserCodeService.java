package sigma.nure.tailoring.tailoring.service;

public interface UserCodeService {
    boolean updateCode(Long userId, String codeValue);

    boolean insertCode(Long userId, String codeValue);

    String generateCode();
}
