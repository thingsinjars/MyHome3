package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that provides information about a page of results from a larger
 * dataset. It consists of four fields: currentPage, pageLimit, totalPages, and
 * totalElements. The class also includes a static method for creating a new instance
 * of the class based on a pageable and a page.
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
   * takes a pageable and a page object as input, returning a `PageInfo` object that
   * contains information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pagination context for the page being processed, providing information
   * on the total number of pages and elements available for processing.
   * 
   * 	- `getPageNumber()`: The page number that corresponds to the current page being
   * rendered.
   * 	- `getPageSize()`: The number of elements in each page of the data set.
   * 	- `getTotalPages()`: The total number of pages in the entire data set.
   * 	- `getTotalElements()`: The total number of elements in the data set.
   * 
   * @param page current page being processed, providing information on its position
   * within the total number of pages and the number of elements it contains.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements in each page of the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- PageNumber: The number of the current page being displayed.
   * 	- PageSize: The number of elements on each page.
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
