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
 * total pages, and total elements of a given Pageable and Page object. It generates
 * a PageInfo object containing page number, size, total pages, and total elements
 * based on the provided Pageable and Page objects.
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
   * generates a `PageInfo` object containing information about the number of pages,
   * size of each page, total number of pages, and total elements in a given pageable
   * and page.
   * 
   * @param pageable pagination information of the current page, providing the number
   * of pages and the size of each page.
   * 
   * 	- `pageable.getPageNumber()` represents the current page number being processed.
   * 	- `pageable.getPageSize()` indicates the number of elements per page.
   * 	- `page.getTotalPages()` signifies the total number of pages available for processing.
   * 	- `page.getTotalElements()` reveals the overall number of elements in the collection
   * being processed.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageable`: The Pageable object that contains information about the page being
   * processed.
   * 	- `page`: The Page object that contains information about the current page and
   * its elements.
   * 
   * The `pageable` object contains information such as the page number, size, total
   * pages, and total elements. These values are used to construct the `PageInfo` object
   * returned by the function.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first element is the page number (0-based) represented as an integer value.
   * 	- The second element is the page size represented as an integer value.
   * 	- The third element is the total number of pages represented as an integer value.
   * 	- The fourth and final element represents the total number of elements in the
   * paginated collection, also represented as an integer value.
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
