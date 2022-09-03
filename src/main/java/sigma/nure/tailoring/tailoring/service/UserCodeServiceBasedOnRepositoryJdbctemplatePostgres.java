package sigma.nure.tailoring.tailoring.service;

import sigma.nure.tailoring.tailoring.repository.UserCodeRepository;

public class UserCodeServiceBasedOnRepositoryJdbctemplatePostgres implements UserCodeService{

    private final UserCodeRepository repository;

    public UserCodeServiceBasedOnRepositoryJdbctemplatePostgres(UserCodeRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean updateCodeToInactiveByUserIdAndCodValue(Long userId, String codeValue) {
        return repository.updateCodeToInactiveByUserIdAndCodValue(userId,codeValue);
    }

    @Override
    public boolean save(Long userId, String codeValue) {
        return repository.save(userId,codeValue);
    }
}
