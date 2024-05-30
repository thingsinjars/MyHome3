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
 * constructor for creating instances of the class from a Pageable and a Page object.
 * A high-level summary of this class is that it provides a way to represent and
 * manipulate information about pagination, including the number of pages, page size,
 * total pages, and total elements.
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
   * generates a `PageInfo` object containing information about a given pageable and
   * page, such as the page number, size, total pages, and total elements.
   * 
   * @param pageable Pageable object that contains information about the current page
   * of data being processed, including the page number and size.
   * 
   * 	- `getPageNumber()` returns the page number of the input `pageable`.
   * 	- `getPageSize()` returns the size of each page in the input `pageable`.
   * 	- `getTotalPages()` returns the total number of pages in the input `pageable`.
   * 	- `getTotalElements()` returns the total number of elements in the input `pageable`.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageable`: The pageable object containing information about the current page
   * being processed.
   * 	- `page`: The page object representing the data to be paginated.
   * 	- `pageNumber`: The number of the current page being processed.
   * 	- `pageSize`: The size of a page in elements.
   * 	- `totalPages`: The total number of pages available for processing.
   * 	- `totalElements`: The total number of elements that can be displayed on each page.
   * 
   * @returns a `PageInfo` object containing information about the number of pages,
   * page size, total pages, and total elements.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements in each page being displayed.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
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
