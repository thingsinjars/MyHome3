package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * from the given file is a data structure that contains information about a page of
 * results in a larger dataset. It has four fields: currentPage, pageLimit, totalPages,
 * and totalElements. The class provides a constructor for creating instances of the
 * class from a Pageable object and a Page object.
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
   * takes a pageable and a page object as input, constructs a PageInfo object containing
   * information about the page number, size, total pages, and total elements, and
   * returns it.
   * 
   * @param pageable pageable object that contains information about the current page
   * being processed, including the page number and size.
   * 
   * The `pageNumber` property is an integer representing the page number.
   * The `pageSize` property is an integer denoting the size of a page.
   * The `totalPages` property is an integer indicating the total number of pages in
   * the data set.
   * The `totalElements` property is an integer representing the total number of elements
   * in the data set.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall paginated result.
   * 
   * 	- `pageNumber`: The page number that the pageable object represents.
   * 	- `pageSize`: The size of each page in the pageable object.
   * 	- `totalPages`: The total number of pages available for the pageable object.
   * 	- `totalElements`: The total number of elements in the pageable object.
   * 
   * @returns a `PageInfo` object containing pagination information.
   * 
   * 	- `pageNumber`: The number of the current page being displayed.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages available for display.
   * 	- `totalElements`: The total number of elements in the dataset.
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
