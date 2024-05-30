package com.myhome.utils;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * from the provided file is a data structure that contains information about a page
 * of results in a larger dataset. It has four fields: currentPage, pageLimit,
 * totalPages, and totalElements. The class also provides constructors for creating
 * instances of the class from a Pageable object and a Page object.
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
   * takes a `pageable` and a `page` object as input and returns a `PageInfo` object
   * containing information about the page of elements.
   * 
   * @param pageable pagination information for the current page of data, providing the
   * page number, size, and total pages and elements in the dataset.
   * 
   * 	- `pageNumber`: The page number that contains the element.
   * 	- `pageSize`: The size of each page in the result set.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @param page current page being processed, providing the total number of elements
   * on that page.
   * 
   * 	- `pageNumber`: The page number of the paginated result.
   * 	- `pageSize`: The size of each page in the paginated result.
   * 	- `totalPages`: The total number of pages in the paginated result.
   * 	- `totalElements`: The total number of elements in the paginated result.
   * 
   * @returns a `PageInfo` object containing information about the page number, size,
   * total pages, and total elements of a Pageable.
   * 
   * 	- pageable.getPageNumber(): The zero-based index of the current page being accessed.
   * 	- pageable.getPageSize(): The number of elements in a single page.
   * 	- page.getTotalPages(): The total number of pages available in the result set.
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
