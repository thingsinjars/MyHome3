package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that encapsulates information about a page of results in a
 * larger dataset. It provides the current page number, page limit, total pages, and
 * total elements, which can be used to navigate and manipulate the larger dataset.
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
   * creates a `PageInfo` object containing information about the number of pages and
   * elements in a specified range.
   * 
   * @param pageable pagination information for the current page being processed,
   * providing the number of pages and elements per page.
   * 
   * 	- `pageNumber`: The page number of the result set.
   * 	- `pageSize`: The number of elements in each page of the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing the number of elements on that
   * page and the total number of pages in the result set.
   * 
   * 	- The `pageNumber` field indicates the number of the page being processed.
   * 	- The `pageSize` field represents the number of elements in each page.
   * 	- The `totalPages` field shows the total number of pages in the dataset.
   * 	- The `totalElements` field displays the overall number of elements in the dataset.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first argument, `pageable`, is an instance of `Pageable`.
   * 	- The second argument, `page`, is an instance of `Page`.
   * 	- The method returns a `PageInfo` object, which contains four properties:
   * 	+ `pageNumber`: the number of the current page being displayed.
   * 	+ `pageSize`: the number of elements on each page.
   * 	+ `totalPages`: the total number of pages in the result set.
   * 	+ `totalElements`: the total number of elements in the result set.
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
