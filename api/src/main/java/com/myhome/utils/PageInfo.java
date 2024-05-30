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
 * a `PageInfo` object containing these values through a constructor that takes in
 * the Pageable and Page objects as parameters. The class also provides a method for
 * creating instances of the class from a Pageable and Page object.
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
   * takes a `pageable` and a `page` as input and returns a `PageInfo` object containing
   * information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pageable object containing information about the pagination of the
   * data, such as the current page number and size.
   * 
   * 	- `getPageNumber()` returns the page number of the current page being processed.
   * 	- `getPageSize()` returns the number of elements per page.
   * 	- `getTotalPages()` returns the total number of pages in the dataset.
   * 	- `getTotalElements()` returns the total number of elements in the dataset.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number, which is a positive integer indicating the
   * position of the current page in the paginated sequence.
   * 	- `pageSize`: The page size, which is an integer representing the number of
   * elements in a single page.
   * 	- `totalPages`: The total number of pages in the paginated sequence, which is
   * also a positive integer.
   * 	- `totalElements`: The total number of elements returned by the pagination, which
   * is an integer as well.
   * 
   * @returns a `PageInfo` object containing information about the current page and
   * total pages of a paginated dataset.
   * 
   * 	- The first element of the PageInfo object is the page number, which represents
   * the number of the current page being displayed.
   * 	- The second element is the page size, which indicates the number of elements
   * that can be displayed on a single page.
   * 	- The third element is the total number of pages, which represents the total
   * number of pages in the paginated collection.
   * 	- The fourth element is the total number of elements, which represents the total
   * number of elements in the paginated collection.
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
