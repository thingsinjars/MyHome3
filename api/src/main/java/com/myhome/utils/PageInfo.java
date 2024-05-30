package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * provides a structured representation of information about a page of data, including
 * the current page number, page size, total pages, and total elements. The class
 * offers constructors for creating instances from a Pageable object and a Page object,
 * allowing for easy generation of PageInfo objects based on input data.
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
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and size.
   * 
   * 	- `getPageNumber()` returns the current page number being displayed.
   * 	- `getPageSize()` returns the total number of elements in the page being displayed.
   * 	- `getTotalPages()` returns the total number of pages in the result set.
   * 	- `getTotalElements()` returns the total number of elements in the result set.
   * 
   * @param page current page being processed, providing information on its number,
   * size, total pages, and total elements.
   * 
   * 	- `getPageNumber()`: returns the page number of the current page being processed
   * 	- `getPageSize()`: returns the number of elements in a single page of the sequence
   * 	- `getTotalPages()`: returns the total number of pages in the entire sequence
   * 	- `getTotalElements()`: returns the total number of elements in the entire sequence
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
   * 
   * 	- The first argument, `pageable`, is an instance of `Pageable`. This represents
   * the pagination configuration for the page being processed.
   * 	- The second argument, `page`, is an instance of `Page`. This represents the
   * specific page being processed, including its position in the overall sequence of
   * pages and the number of elements it contains.
   * 	- The returned object, `PageInfo`, encapsulates information about the page number,
   * size, total pages, and total elements. This allows for easy access and manipulation
   * of these properties without having to refer to the original `Page` or `Pageable`
   * objects.
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
