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
 * dataset. It has four fields: currentPage, pageLimit, totalPages, and totalElements.
 * The class provides constructors for creating instances of the class from a Pageable
 * object and a Page object.
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
   * takes a `pageable` and a `page` as input, returns a `PageInfo` object containing
   * information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including its page number and size.
   * 
   * 	- `getPageNumber()`: The page number being acted upon.
   * 	- `getPageSize()`: The number of elements in each page.
   * 	- `getTotalPages()`: The total number of pages available for processing.
   * 	- `getTotalElements()`: The total number of elements returned by the query.
   * 
   * @param page current page of results being processed, providing the total number
   * of elements on that page.
   * 
   * 	- `pageNumber`: The number of the current page.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of the input `pageable` and `page`.
   * 
   * 	- `pageNumber`: The page number that the given pageable is currently on.
   * 	- `pageSize`: The size of each page in the pageable.
   * 	- `totalPages`: The total number of pages in the pageable.
   * 	- `totalElements`: The total number of elements in the pageable.
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
