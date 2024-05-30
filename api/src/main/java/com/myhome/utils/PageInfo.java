package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about the number of pages, page size,
 * total pages, and total elements of a given Pageable and Page. It generates a
 * `PageInfo` object containing these values through a constructor that takes a
 * Pageable and Page as input. The class also provides a method for creating instances
 * of the class from a Pageable and Page.
 */
@EqualsAndHashCode
@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PageInfo {
  private final int currentPage;
  private final int pageLimit;
  private final int totalPages;
  private final long totalElements;

  /**
   * creates a `PageInfo` object containing information about the number of pages, page
   * size, total pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable pagination state, including the current page number and the total
   * number of pages, which are used to compute the paginated view of the data.
   * 
   * 	- `pageable.getPageNumber()` represents the current page number.
   * 	- `pageable.getPageSize()` signifies the number of elements per page.
   * 	- `page.getTotalPages()` indicates the total number of pages in the dataset.
   * 	- `page.getTotalElements()` shows the overall number of elements in the dataset.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number in which the element is located (1-based).
   * 	- `pageSize`: The size of the page (1-based).
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first parameter is `pageable`, which represents the pageable object that
   * contains information about the current page being processed.
   * 	- The second parameter is `page`, which represents the specific page being processed.
   * 	- The return value is a `PageInfo` object, which contains information about the
   * current page, including its page number, size, total pages, and total elements.
   */
  public static PageInfo of(Pageable pageable, Page<?> page) {
    return new PageInfo(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        page.getTotalPages(),
        page.getTotalElements()
    );
  }
}
