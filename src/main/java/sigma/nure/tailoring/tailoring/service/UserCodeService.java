package sigma.nure.tailoring.tailoring.service;

public interface UserCodeService {
    boolean updateCodeToInactiveByUserIdAndCodValue(Long userId, String codeValue);

    boolean save(Long userId, String codeValue);
}
