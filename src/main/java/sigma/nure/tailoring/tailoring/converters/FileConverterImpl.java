package sigma.nure.tailoring.tailoring.converters;

import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class FileConverterImpl implements FileConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileConverterImpl.class);
    private static final int START_VALUE = 0;
    private static final Supplier<Function<String, String>> getDefaultFileNameGenerator = () -> {
        IntIncrement intIncrement = new IntIncrement();

        return (name) -> setNumberToName(intIncrement.increment(), name);
    };

    @Override
    public List<File> toFiles(String directory, List<MultipartFile> multipartFiles) {
        return this.toFiles(directory, multipartFiles, (name) -> getDefaultFileNameGenerator.get().apply(name));
    }

    @Override
    public List<File> toFiles(String directory, List<MultipartFile> multipartFiles, Function<String, String> fileNameGenerator) {
        if (multipartFiles == null) {
            return List.of();
        }

        List<File> files = new ArrayList<>(multipartFiles.size());
        try {
            for (int i = 0, size = multipartFiles.size(); i < size; i++) {
                try {
                    files.add(changNameIfExist(
                            directory,
                            multipartFiles.get(i).getOriginalFilename(),
                            fileNameGenerator)
                    );

                    Files.copy(multipartFiles.get(i).getInputStream(), files.get(i).toPath());
                } catch (FileExistsException existsException) {
                    LOGGER.debug(existsException.getMessage());
                    files.remove(i);
                    --i;
                }

            }
        } catch (IOException exception) {
            LOGGER.error("Files not saved", exception);
            return List.of();
        }

        return files;
    }

    private File changNameIfExist(String directory, String name, Function<String, String> fileNameGenerator) {
        File file = new File(directory, fileNameGenerator.apply(name));
        while (file.exists()) {
            file = new File(directory, fileNameGenerator.apply(name));

        }
        return file;
    }

    private static String setNumberToName(int number, String name) {
        if (number == START_VALUE) {
            return name;
        }
        return name.substring(0, name.indexOf(".")) + " (" + number + ")" + name.substring(name.indexOf("."));
    }

    private static class IntIncrement {
        int value = START_VALUE;

        public int increment() {
            return value++;
        }
    }
}
