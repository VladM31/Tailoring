package sigma.tailoring.converters;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.function.Function;

public interface FileConverter {
    List<File> toFiles(String directory, List<MultipartFile> multipartFiles);

    List<File> toFiles(String directory, List<MultipartFile> multipartFiles, Function<String, String> fileNameGenerator);
}
