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
   * Generates a `PageInfo` object containing information about the number of pages,
   * size of each page, total number of pages, and total elements in the result set of
   * a given `Pageable`.
   * 
   * @param pageable pagination information of the page being processed, providing the
   * page number, page size, total pages, and total elements.
   * 
   * @param page current page being processed, providing information on its position
   * within the total number of pages and the number of elements it contains.
   * 
   * @returns a `PageInfo` object containing page number, size, total pages, and total
   * elements.
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
