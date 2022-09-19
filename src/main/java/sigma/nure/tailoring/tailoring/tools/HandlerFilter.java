package sigma.nure.tailoring.tailoring.tools;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HandlerFilter {
    public static final int EMPTY_ARRAY_SIZE = 0;
    @Nullable
    public <T> List<T> toList(T[] array){
        if(array == null || array.length == EMPTY_ARRAY_SIZE){
            return null;
        }
        return List.of(array);
    }

    public String checkString(String string){
        return string.isBlank() ? null : string;
    }
}
