package com.kanga.kickrushapi.common;

import java.util.Collections;
import java.util.List;

public final class PageUtils {

    private PageUtils() {
    }

    public static <T> PageResponse<T> paginate(List<T> items, int page, int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size는 1 이상이어야 합니다.");
        }
        int fromIndex = page * size;
        if (fromIndex >= items.size()) {
            return PageResponse.of(Collections.emptyList(), page, size, items.size());
        }
        int toIndex = Math.min(fromIndex + size, items.size());
        return PageResponse.of(items.subList(fromIndex, toIndex), page, size, items.size());
    }
}
