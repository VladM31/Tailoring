package sigma.nure.tailoring.tailoring.converters;

import org.apache.commons.io.FileExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileConverterImpl implements FileConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileConverterImpl.class);

    @Override
    public List<File> toFiles(String directory, List<MultipartFile> multipartFiles) {
        return this.toFiles(directory, multipartFiles, (name) -> name);
    }

    @Override
    public List<File> toFiles(String directory, List<MultipartFile> multipartFiles, Function<String, String> fileNameGenerator) {
        if (multipartFiles == null) {
            return List.of();
        }

        List<File> files = multipartFiles
                .stream()
                .map(this.getFileConverter(directory, fileNameGenerator))
                .filter(file -> file != null)
                .toList();

        boolean hasNotError = files.size() == multipartFiles.size();

        if (hasNotError) {
            return files;
        }

        files.forEach(FileConverterImpl::removedFile);
        throw new UncheckedIOException(new FileExistsException(getNameExistFiles(multipartFiles, files)));
    }


    public Function<MultipartFile, File> getFileConverter(String directory, Function<String, String> fileNameGenerator) {
        return (multipartFile -> {
            File file = new File(directory, fileNameGenerator.apply(multipartFile.getOriginalFilename()));
            try {
                Files.copy(multipartFile.getInputStream(), file.toPath());
            } catch (IOException exception) {
                LOGGER.error("Files not saved", exception);
                return null;
            }
            return file;
        });
    }

    public static void removedFile(File file) {
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            LOGGER.error("Files not delete", e);
        }
    }

    public String getNameExistFiles(List<MultipartFile> multipartFiles, List<File> files) {
        List<String> nameExistFiles = multipartFiles.stream().map(mf -> mf.getOriginalFilename()).toList();

        nameExistFiles.removeAll(files.stream().map(f -> f.getName()).toList());

        return nameExistFiles.stream().collect(Collectors.joining(", "));
    }
}
