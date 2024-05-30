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
 * dataset. It provides four fields: currentPage, pageLimit, totalPages, and
 * totalElements. Additionally, it offers a constructor for creating instances from
 * a Pageable object and a Page object.
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
   * page size, total pages, and total elements of a given pageable and page.
   * 
   * @param pageable pageable object containing information about the current page being
   * processed, which is used to compute the page number, size, and total pages and elements.
   * 
   * 	- The PageNumber property is obtained from pageable's getPageNumber() method.
   * 	- The PageSize property is obtained from pageable's getPageSize() method.
   * 	- The TotalPages property is obtained from pageable's getTotalPages() method.
   * 	- The TotalElements property is obtained from pageable's getTotalElements() method.
   * 
   * @param page current page being processed, providing information on its position
   * and size within the overall pagination sequence.
   * 
   * 	- `pageNumber`: The number of the page being returned.
   * 	- `pageSize`: The number of elements in each page.
   * 	- `totalPages`: The total number of pages in the result set.
   * 	- `totalElements`: The total number of elements in the result set.
   * 
   * @returns a `PageInfo` object containing various pagination-related information.
   * 
   * 1/ `pageNumber`: The page number of the resultant page, indicating the position
   * of the page in the paginated sequence.
   * 2/ `pageSize`: The number of elements in a single page of the resultant page.
   * 3/ `totalPages`: The total number of pages in the paginated sequence.
   * 4/ `totalElements`: The total number of elements returned by the pagination.
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
