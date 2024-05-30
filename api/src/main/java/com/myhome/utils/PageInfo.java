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
 * total pages, and total elements of a given Pageable and Page. The class has four
 * fields: currentPage, pageLimit, totalPages, and totalElements. It also provides
 * constructors for creating instances from a Pageable and a Page object.
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
   * creates a `PageInfo` object from a pageable and a page, providing information on
   * the current page number, size, total pages, and total elements.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * page number, page size, total pages, and total elements.
   * 
   * 	- `getPageNumber()` returns the current page number of the result set.
   * 	- `getPageSize()` returns the number of elements in each page of the result set.
   * 	- `getTotalPages()` returns the total number of pages in the result set.
   * 	- `getTotalElements()` returns the total number of elements in the result set.
   * 
   * @param page current page being processed, providing information on its number,
   * size, total pages, and total elements.
   * 
   * 1/ `pageNumber`: The page number of the result.
   * 2/ `pageSize`: The size of each page in the result.
   * 3/ `totalPages`: The total number of pages in the result.
   * 4/ `totalElements`: The total number of elements in the result.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The `PageNumber` field represents the current page number being viewed by the
   * user.
   * 	- The `PageSize` field denotes the number of elements per page displayed to the
   * user.
   * 	- The `TotalPages` field indicates the total number of pages in the result set.
   * 	- The `TotalElements` field represents the total number of elements returned by
   * the query.
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
