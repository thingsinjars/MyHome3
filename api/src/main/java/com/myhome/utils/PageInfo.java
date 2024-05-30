package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that stores information about a page of results in a larger
 * dataset. It contains four fields: currentPage, pageLimit, totalPages, and
 * totalElements. The class also provides constructors for creating instances of the
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
   * takes a `pageable` and a `page` object as input, and returns a `PageInfo` object
   * containing information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pagination information for the current page of data, including the
   * number of pages and the size of each page.
   * 
   * 	- `pageable.getPageNumber()`: The page number of the current page being processed.
   * 	- `pageable.getPageSize()`: The number of elements in a page of the data being processed.
   * 	- `page.getTotalPages()`: The total number of pages in the dataset being processed.
   * 	- `page.getTotalElements()`: The total number of elements in the dataset being processed.
   * 
   * @param page current page being processed, providing the number of elements on that
   * page and the total number of pages in the result set.
   * 
   * 	- `getPageNumber()`: The page number of the current page being processed.
   * 	- `getPageSize()`: The size of each page in terms of the number of elements it contains.
   * 	- `getTotalPages()`: The total number of pages in the input data.
   * 	- `getTotalElements()`: The total number of elements in the input data.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first parameter, `pageable`, represents the pageable object that contains
   * information about the current page being processed.
   * 	- The second parameter, `page`, represents the page object that contains information
   * about a specific page.
   * 	- The `PageNumber` property represents the number of the current page being processed.
   * 	- The `PageSize` property represents the number of elements in each page.
   * 	- The `TotalPages` property represents the total number of pages in the entire collection.
   * 	- The `TotalElements` property represents the total number of elements in the
   * entire collection.
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
