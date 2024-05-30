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
 * total pages, and total elements of a given Pageable and Page. The class provides
 * a constructor for creating instances of the class from a Pageable and a Page object,
 * and it also includes methods for generating a `PageInfo` object containing information
 * about the current page being processed, including the page number and size, as
 * well as the total number of pages and elements in the input.
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
   * generates a `PageInfo` object containing information about the number of pages,
   * page size, total pages, and total elements for a given `Pageable` and `Page`.
   * 
   * @param pageable page number and size of the page being queried, which are used to
   * calculate the total pages and elements in the PageInfo object returned by the function.
   * 
   * The first parameter, `pageable`, is an instance of the `Pageable` class. It has
   * several attributes, including `getPageNumber()`, `getPageSize()`, and others that
   * are used in constructing the output object `PageInfo`.
   * 
   * @param page current page of data being processed, providing the total number of
   * elements on that page.
   * 
   * 	- `pageNumber`: The number of the page being returned.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing page-related metadata.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements in each page.
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
