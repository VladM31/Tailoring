package sigma.tailoring.service;

import sigma.tailoring.repository.UserCodeRepository;

import java.util.Random;

public class UserCodeServiceImpl implements UserCodeService {
    private static final Random RANDOM = new Random();
    private static final int TYPES_CODE_SYMBOL = 3;
    private static final int SIZE_CODE = 20;
    private static final int NUMBER_RANGE = 10;
    private static final int LOWER_CHARACTERS_RANGE_FROM = 'a';
    private static final int LOWER_CHARACTERS_RANGE_TO = 'z';
    private static final int UPPER_CHARACTERS_RANGE_FROM = 'A';
    private static final int UPPER_CHARACTERS_RANGE_TO = 'Z';

    private final UserCodeRepository userCodeRepository;

    public UserCodeServiceImpl(UserCodeRepository userCodeRepository) {
        this.userCodeRepository = userCodeRepository;
    }

    @Override
    public boolean updateCode(Long userId, String codeValue) {
        return userCodeRepository.updateCode(userId, codeValue);
    }

    @Override
    public boolean insertCode(Long userId, String codeValue) {
        return userCodeRepository.insertCode(userId, codeValue);
    }

    @Override
    public String generateCode() {
        StringBuilder codeBuilder = new StringBuilder(SIZE_CODE);

        for (int i = 0; i < SIZE_CODE; i++) {

            switch (RANDOM.nextInt(TYPES_CODE_SYMBOL)) {
                case 0:
                    codeBuilder.append(RANDOM.nextInt(NUMBER_RANGE));
                    break;
                case 1:
                    codeBuilder.append((char) RANDOM.nextInt(LOWER_CHARACTERS_RANGE_FROM, LOWER_CHARACTERS_RANGE_TO));
                    break;
                case 2:
                    codeBuilder.append((char) RANDOM.nextInt(UPPER_CHARACTERS_RANGE_FROM, UPPER_CHARACTERS_RANGE_TO));
                    break;
            }
        }

        return codeBuilder.toString();
    }
}
