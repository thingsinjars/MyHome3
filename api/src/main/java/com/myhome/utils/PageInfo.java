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
 * totalElements. The class also includes methods for creating instances of the class
 * from a Pageable object and a Page object.
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
   * creates a `PageInfo` object containing information about the number of pages, page
   * size, total pages, and total elements of a given pageable and page.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and size.
   * 
   * 	- `getPageNumber()`: Returns the page number of the current page being processed.
   * 	- `getPageSize()`: Returns the size of a page in the collection.
   * 	- `getTotalPages()`: Returns the total number of pages in the collection.
   * 	- `getTotalElements()`: Returns the total number of elements in the collection.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number of the result set.
   * 	- `pageSize`: The number of elements in each page of the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements returned by the query.
   * 
   * @returns a `PageInfo` object containing information about the number of pages,
   * page size, total pages, and total elements.
   * 
   * 	- The first field, `pageNumber`, represents the page number of the result set.
   * 	- `pageSize` is the number of elements in each page of the result set.
   * 	- `totalPages` indicates the total number of pages in the result set.
   * 	- `totalElements` represents the total number of elements in the entire result set.
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
