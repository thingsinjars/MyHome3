package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that holds information about a page of results in a larger
 * dataset, including the current page number, page size, total pages, and total
 * elements. The class provides a constructor for creating instances of the class
 * from a Pageable and a Page object, allowing for efficient creation of PageInfos
 * based on pagination data.
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
   * generates a `PageInfo` object that contains information about the current page and
   * total number of pages and elements.
   * 
   * @param pageable pagination information for the page being processed, providing the
   * page number, size, total pages, and total elements.
   * 
   * The PageNumber property is given by `pageable.getPageNumber()`.
   * The PageSize property is given by `pageable.getPageSize()`.
   * The TotalPages property is given by `page.getTotalPages()`.
   * The TotalElements property is given by `page.getTotalElements()`.
   * 
   * @param page current page being processed, providing information on its position
   * within the total pages and the number of elements it contains.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements in a page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements returned by the query.
   * 
   * @returns a `PageInfo` object containing various page-related metrics.
   * 
   * 	- The first parameter is the `Pageable` object representing the pagination state.
   * 	- The second parameter is the `Page` object representing the current page being
   * processed.
   * 	- The return value is a `PageInfo` object containing information about the
   * pagination state, including the current page number, page size, total pages, and
   * total elements.
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
