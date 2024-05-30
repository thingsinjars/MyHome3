package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about a page of results in a larger
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
   * creates a `PageInfo` object containing information about a given pageable and page,
   * including the page number, size, total pages, and total elements.
   * 
   * @param pageable Pageable interface, which provides methods for retrieving a page
   * of elements from a data source.
   * 
   * 	- `getPageNumber()` represents the page number in the paginated sequence.
   * 	- `getPageSize()` denotes the number of elements per page.
   * 	- `getTotalPages()` signifies the total number of pages available for rendering.
   * 	- `getTotalElements()` indicates the total number of elements that can be displayed
   * in a single page.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number as returned by the pagination mechanism.
   * 	- `pageSize`: The size of each page returned by the pagination mechanism.
   * 	- `totalPages`: The total number of pages available in the collection or dataset.
   * 	- `totalElements`: The total number of elements within the collection or dataset,
   * including those across all pages.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- PageNumber: The number of the page being referred to.
   * 	- PageSize: The size of the page being referred to.
   * 	- TotalPages: The total number of pages in the collection.
   * 	- TotalElements: The total number of elements in the collection.
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
