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
 * total pages, and total elements of a given Pageable and Page. The class offers a
 * constructor for creating instances from a Pageable and a Page object, as well as
 * a static method for generating a PageInfo object based on these inputs.
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
   * generates a `PageInfo` object containing information about the specified pageable
   * and page, including the current page number, page size, total pages, and total elements.
   * 
   * @param pageable pagination information for the given page of data.
   * 
   * 	- The method takes a `Pageable` object `pageable` as input and returns a `PageInfo`
   * object.
   * 	- `pageable.getPageNumber()` represents the current page number being displayed.
   * 	- `pageable.getPageSize()` indicates the number of elements per page.
   * 	- `page.getTotalPages()` denotes the total number of pages in the result set.
   * 	- `page.getTotalElements()` shows the overall number of elements returned by the
   * query.
   * 
   * @param page current page being processed, providing information on its number,
   * size, total pages, and total elements.
   * 
   * 	- `getPageNumber()` represents the page number as an integer value.
   * 	- `getPageSize()` denotes the page size as an integer value.
   * 	- `getTotalPages()` returns a total number of pages as an integer value.
   * 	- `getTotalElements()` gives the total number of elements on each page as an
   * integer value.
   * 
   * @returns a `PageInfo` object containing page number, page size, total pages, and
   * total elements.
   * 
   * 	- The first argument, `pageable`, is an instance of `Pageable`.
   * 	- The second argument, `page`, is an instance of `Page`.
   * 	- The return value is a `PageInfo` object, which contains information about the
   * page and the elements on that page.
   * 	- The `pageNumber` property of the `PageInfo` object represents the number of the
   * current page being displayed.
   * 	- The `pageSize` property represents the number of elements in each page.
   * 	- The `totalPages` property represents the total number of pages available for display.
   * 	- The `totalElements` property represents the total number of elements in the
   * result set.
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
