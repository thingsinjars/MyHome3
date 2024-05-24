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
 * a PageInfo object containing these values through its constructor.
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
   * takes a `pageable` and a `page` as input, returning a `PageInfo` object containing
   * information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * page number, page size, total pages, and total elements.
   * 
   * 	- `pageNumber`: The number of the current page being served.
   * 	- `pageSize`: The size of each page being served.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall pageable result.
   * 
   * 	- `pageNumber`: The page number of the result.
   * 	- `pageSize`: The size of each page in the result.
   * 	- `totalPages`: The total number of pages in the result.
   * 	- `totalElements`: The total number of elements returned in the result.
   * 
   * @returns a `PageInfo` object containing page-related metadata.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements on each page of the result set.
   * 	- page.getTotalPages(): The total number of pages in the result set.
   * 	- page.getTotalElements(): The total number of elements in the result set.
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
