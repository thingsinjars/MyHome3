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
 * a constructor for creating instances of the class from a Pageable and a Page object.
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
   * creates a `PageInfo` object containing information about the number of pages, size
   * of each page, total number of pages, and total elements in a given pageable and page.
   * 
   * @param pageable pagination state of the current query, providing the number of
   * pages and elements per page to calculate the current page and total pages.
   * 
   * 	- `pageNumber`: The number of the page being returned.
   * 	- `pageSize`: The size of each page in terms of how many elements it can hold.
   * 	- `totalPages`: The total number of pages available for the given input.
   * 	- `totalElements`: The total number of elements available across all pages.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall paginated result.
   * 
   * 	- `pageNumber`: The page number of the pageable.
   * 	- `pageSize`: The size of each page in the pageable.
   * 	- `totalPages`: The total number of pages in the pageable.
   * 	- `totalElements`: The total number of elements in the pageable.
   * 
   * @returns a `PageInfo` object containing information about the pagination of the
   * input `pageable` and `page`.
   * 
   * 	- pageable.getPageNumber(): The number of the current page being displayed.
   * 	- pageable.getPageSize(): The number of elements in each page being displayed.
   * 	- page.getTotalPages(): The total number of pages in the dataset.
   * 	- page.getTotalElements(): The total number of elements in the dataset.
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
