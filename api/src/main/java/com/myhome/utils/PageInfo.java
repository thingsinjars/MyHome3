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
 * PageInfo object by passing in a Pageable and a Page, which contains the current
 * page number, page size, total pages, and total elements.
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
   * creates a `PageInfo` object containing page-related metadata, such as the current
   * page number, size, and total pages and elements count, given a `Pageable` object
   * and a `Page` object.
   * 
   * @param pageable pagination information for the current page being processed,
   * including its page number and size.
   * 
   * 	- `getPageNumber()` returns the page number of the input `pageable`.
   * 	- `getPageSize()` returns the size of each page in the input `pageable`.
   * 	- `getTotalPages()` returns the total number of pages in the input `pageable`.
   * 	- `getTotalElements()` returns the total number of elements in the input `pageable`.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall paginated result set.
   * 
   * 	- `pageNumber`: The page number of the result, as returned by the paginated data
   * source.
   * 	- `pageSize`: The number of elements in each page of the result, as returned by
   * the paginated data source.
   * 	- `totalPages`: The total number of pages of results, as returned by the paginated
   * data source.
   * 	- `totalElements`: The total number of elements in all pages of the result, as
   * returned by the paginated data source.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a given pageable and page.
   * 
   * 	- `pageNumber`: The page number associated with the pageable object.
   * 	- `pageSize`: The size of each page in the paginated sequence.
   * 	- `totalPages`: The total number of pages available in the sequence.
   * 	- `totalElements`: The total number of elements present in all the pages of the
   * sequence.
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
