package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that contains information about a page of results in a larger
 * dataset. It provides four fields: currentPage, pageLimit, totalPages, and
 * totalElements. The class also includes constructors for creating instances of the
 * class from a Pageable object and a Page object.
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
   * generates a `PageInfo` object containing information about the current page of a
   * paginated dataset, including the page number, size, total pages, and total elements.
   * 
   * @param pageable pageable interface, which provides information about the pagination
   * of the data being processed.
   * 
   * 	- `getPageNumber()`: The page number that corresponds to the input `pageable`.
   * 	- `getPageSize()`: The number of elements in a page for the input `pageable`.
   * 	- `getTotalPages()`: The total number of pages available for the input `pageable`.
   * 	- `getTotalElements()`: The total number of elements that can be retrieved from
   * the input `pageable`.
   * 
   * @param page current page being processed, providing information on its position,
   * size, total pages, and total elements.
   * 
   * 	- `pageNumber`: The page number of the input `page`.
   * 	- `pageSize`: The size of each page in the input `page`.
   * 	- `totalPages`: The total number of pages in the input `page`.
   * 	- `totalElements`: The total number of elements in the input `page`.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- `pageNumber`: The number of the page being retrieved.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
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
