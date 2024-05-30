package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * is a data structure that contains information about a page of results in a larger
 * dataset, including the current page number, page size, total pages, and total
 * elements. The class provides methods for generating instances of the class from a
 * Pageable and Page objects.
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
   * transforms a `Pageable` object and a `Page` object into a `PageInfo` object,
   * providing information on the page number, size, total pages, and total elements.
   * 
   * @param pageable Pageable object that provides the paging information for the current
   * page of data.
   * 
   * 	- `getPageNumber()`: The page number of the current page being processed.
   * 	- `getPageSize()`: The size of each page in the paginated result.
   * 	- `getTotalPages()`: The total number of pages in the result set.
   * 	- `getTotalElements()`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing information on its position
   * within the total number of pages and the number of elements it contains.
   * 
   * 	- `pageNumber`: The number of the current page being served.
   * 	- `pageSize`: The number of elements per page.
   * 	- `totalPages`: The total number of pages in the data set.
   * 	- `totalElements`: The total number of elements in the data set.
   * 
   * @returns a `PageInfo` object containing information about the page of elements.
   * 
   * 	- The first element is `pageNumber`, which represents the page number that the
   * current page belongs to.
   * 	- The second element is `pageSize`, which denotes the number of elements in a
   * single page.
   * 	- The third element is `totalPages`, which signifies the total number of pages
   * in the dataset.
   * 	- The fourth element is `totalElements`, which represents the total number of
   * elements in the entire dataset.
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
