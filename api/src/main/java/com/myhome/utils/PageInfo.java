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
 * PageInfo object containing these values and can be constructed using a Pageable
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
   * creates a `PageInfo` object containing information about a pageable and a page,
   * including the page number, size, total pages, and total elements.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and size.
   * 
   * 	- `getPageNumber()`: The page number of the resultant page.
   * 	- `getPageSize()`: The size of each page in the resultant page.
   * 	- `getTotalPages()`: The total number of pages that can be displayed.
   * 	- `getTotalElements()`: The total number of elements in the resultant page.
   * 
   * @param page current page being processed, providing the number of elements on that
   * page and the total number of pages in the result set.
   * 
   * 	- `getPageNumber()`: Returns the page number as an integer value.
   * 	- `getPageSize()`: Returns the page size as an integer value.
   * 	- `getTotalPages()`: Returns the total number of pages as an integer value.
   * 	- `getTotalElements()`: Returns the total number of elements in the page as an
   * integer value.
   * 
   * @returns a `PageInfo` object containing page metadata.
   * 
   * 	- pageable.getPageNumber(): The page number of the current page being processed.
   * 	- pageable.getPageSize(): The number of elements in a single page of the data source.
   * 	- page.getTotalPages(): The total number of pages in the data source.
   * 	- page.getTotalElements(): The total number of elements in the data source.
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
