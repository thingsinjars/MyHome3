package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that represents a page of elements and provides information
 * about the current page, total pages, and total elements. It is constructed using
 * the `of()` method and can be used to easily access and manipulate page-related information.
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
   * generates a `PageInfo` object from a `Pageable` and a `Page`. It returns information
   * about the number of pages, page size, total pages, and total elements in the page.
   * 
   * @param pageable page number and size of the page being processed, which are used
   * to calculate the total pages and elements for the `PageInfo` object returned by
   * the function.
   * 
   * 	- `getPageNumber()`: Returns the current page number.
   * 	- `getPageSize()`: Returns the total number of elements in this page.
   * 	- `getTotalPages()`: Returns the total number of pages available for the requested
   * range.
   * 	- `getTotalElements()`: Returns the total number of elements returned by this page.
   * 
   * @param page current page of data being processed, providing the total number of
   * elements on that page.
   * 
   * 	- `pageNumber`: The page number that contains the element.
   * 	- `pageSize`: The number of elements in a single page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements returned by the query.
   * 
   * @returns a `PageInfo` object containing page metadata.
   * 
   * 	- PageNumber: The number of the page being returned.
   * 	- PageSize: The number of elements in each page being returned.
   * 	- TotalPages: The total number of pages in the result set.
   * 	- TotalElements: The total number of elements in the result set.
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
