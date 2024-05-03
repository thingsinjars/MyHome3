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
   * generates a `PageInfo` object that contains information about the number of pages,
   * page size, total pages, and total elements of a pageable input.
   * 
   * @param pageable pagination information for the current page of data, which includes
   * the page number, page size, total pages, and total elements.
   * 
   * 	- The first parameter is `pageable`, which is an instance of `Pageable`. This
   * class contains information about the current page being processed, such as the
   * page number and size.
   * 	- The second parameter is `page`, which is an instance of a specific page type.
   * This parameter represents the page that is being processed.
   * 
   * The function returns a `PageInfo` object containing information about the current
   * page and the overall set of pages.
   * 
   * @param page current page being processed, providing information on its position
   * within the overall result set.
   * 
   * 	- `pageNumber`: The number of the page being returned.
   * 	- `pageSize`: The size of each page in terms of the number of elements.
   * 	- `totalPages`: The total number of pages available in the result set.
   * 	- `totalElements`: The total number of elements in the result set across all pages.
   * 
   * @returns a `PageInfo` object containing information about the number of pages,
   * size of each page, total number of pages, and total elements.
   * 
   * 	- `pageNumber`: The page number of the current page being retrieved.
   * 	- `pageSize`: The number of elements in each page of the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the entire result set.
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
