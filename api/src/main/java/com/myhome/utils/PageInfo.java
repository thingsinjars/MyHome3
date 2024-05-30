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
 * dataset. It has four fields: currentPage, pageLimit, totalPages, and totalElements.
 * The class also provides a constructor for creating instances of the class from a
 * Pageable object and a Page object.
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
   * transforms a `Pageable` object and a `Page` object into a `PageInfo` object
   * containing information about the page number, size, total pages, and total elements.
   * 
   * @param pageable pagination state, providing the current page number and total pages
   * available for retrieval.
   * 
   * 1/ The first argument is `Pageable pageable`. This object contains information
   * about the pagination of results, such as the current page number and the total
   * number of pages in the result set.
   * 2/ The second argument is `Page<?> page`. This object represents a single page of
   * results, with properties for the total number of elements on the page and the total
   * number of pages in the result set.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall paginated result.
   * 
   * 	- `pageNumber`: The page number associated with the input `page`.
   * 	- `pageSize`: The size of each page associated with the input `page`.
   * 	- `totalPages`: The total number of pages associated with the input `page`.
   * 	- `totalElements`: The total number of elements associated with the input `page`.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of the given `Pageable` and `Page`.
   * 
   * 	- `pageNumber`: The number of the page being referred to.
   * 	- `pageSize`: The size of the page being referred to.
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
