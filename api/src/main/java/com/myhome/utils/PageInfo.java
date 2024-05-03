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
 * The class provides a constructor for creating instances of the class from a Pageable
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
   * creates a `PageInfo` object containing various pagination-related information,
   * such as the current page number, size, total pages, and total elements, given a
   * `Pageable` and a `Page`.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and size.
   * 
   * 	- `getPageNumber()`: The page number of the current page being processed.
   * 	- `getPageSize()`: The size of each page returned by the paginated data source.
   * 	- `getTotalPages()`: The total number of pages in the dataset, including any
   * partial pages.
   * 	- `getTotalElements()`: The total number of elements in the entire dataset,
   * including those on all pages.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number of the deserialized input.
   * 	- `pageSize`: The size of each page in the deserialized input.
   * 	- `totalPages`: The total number of pages in the deserialized input.
   * 	- `totalElements`: The total number of elements in the deserialized input.
   * 
   * @returns a `PageInfo` object containing various pagination-related metrics.
   * 
   * 	- Page number: The page number of the pageable that generated the page.
   * 	- Page size: The number of elements in each page of the pageable.
   * 	- Total pages: The total number of pages in the pageable.
   * 	- Total elements: The total number of elements in the entire pageable.
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
