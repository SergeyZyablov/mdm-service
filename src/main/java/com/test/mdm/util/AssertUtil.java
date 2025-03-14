package com.test.mdm.util;

import com.test.mdm.exception.NotFoundException;

import java.util.Optional;

public class AssertUtil {

    public static <T> T notNull(Optional<T> object, String errorMessage) {
        if (object.isEmpty()) {
            throw new NotFoundException(errorMessage);
        }
        return object.get();
    }
}
